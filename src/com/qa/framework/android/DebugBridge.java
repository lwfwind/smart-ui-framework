package com.qa.framework.android;

import com.android.SdkConstants;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class DebugBridge {
    private static AndroidDebugBridge sDebugBridge;

    @SuppressWarnings("unused")
    private static String getAdbLocation() throws Exception {
        String sdkDir = System.getenv("ANDROID_HOME");
        if (sdkDir == null || sdkDir.equalsIgnoreCase("")) {
            throw new Exception("ENV Variables ANDROID_HOME IS NOT SETTED");
        }

        String toolsDir = sdkDir + File.separator + "tools";
        File sdk = new File(toolsDir).getParentFile();

        // check if adb is present in platform-tools
        File platformTools = new File(sdk, "platform-tools");
        File adb = new File(platformTools, SdkConstants.FN_ADB);
        if (adb.exists()) {
            return adb.getAbsolutePath();
        }

        // check if adb is present in the tools directory
        adb = new File(toolsDir, SdkConstants.FN_ADB);
        if (adb.exists()) {
            return adb.getAbsolutePath();
        }

        // check if we're in the Android source tree where adb is in $ANDROID_HOST_OUT/bin/adb
        String androidOut = System.getenv("ANDROID_HOST_OUT");
        if (androidOut != null) {
            String adbLocation = androidOut + File.separator + "bin" + File.separator +
                    SdkConstants.FN_ADB;
            if (new File(adbLocation).exists()) {
                return adbLocation;
            }
        }

        return null;
    }

    public static void init() {
        if (sDebugBridge == null) {
            String adbLocation = null;
            try {
                adbLocation = getAdbLocation();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (adbLocation != null) {
                AndroidDebugBridge.init(false);
                sDebugBridge = AndroidDebugBridge.createBridge(adbLocation, false);
                waitDeviceList(sDebugBridge);
            }
        }
    }

    public static void terminate() {
        if (sDebugBridge != null) {
            sDebugBridge = null;
            AndroidDebugBridge.terminate();
        }
    }

    public static List<IDevice> getDevices() {
        return Arrays.asList(sDebugBridge.getDevices());
    }

    private static void waitDeviceList(AndroidDebugBridge bridge) {
        int count = 0;
        while (!bridge.hasInitialDeviceList()) {
            try {
                Thread.sleep(500);
                count++;
            } catch (InterruptedException ignored) {
            }
            if (count > 60) {
                System.err.print("等待获取设备超时");
                break;
            }
        }
    }

    public static IDevice getDevice() throws IOException {
        List<IDevice> devices = getDevices();
        if (devices.size() == 0) {
            throw new IOException("No Android devices were detected by adb.");
        } else if (devices.size() >= 1) {
            return devices.get(0);
        }
        return null;
    }

    public static IDevice getDevice(int index) throws IOException {
        List<IDevice> devices = getDevices();
        if (devices.size() == 0) {
            throw new IOException("No Android devices were detected by adb.");
        } else if (devices.size() >= 1) {
            return devices.get(index);
        }
        return null;
    }
}
