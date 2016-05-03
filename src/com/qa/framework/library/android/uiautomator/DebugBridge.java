/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qa.framework.library.android.uiautomator;

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
	private static String getAdbLocation() {
        String sdkDir = System.getenv("ANDROID_HOME");
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
        String adbLocation = getAdbLocation();
        if (adbLocation != null) {
            AndroidDebugBridge.init(false);
            sDebugBridge = AndroidDebugBridge.createBridge(adbLocation, false);
            waitDevicesList(sDebugBridge);
        }
    }

    public static void terminate() {
        if (sDebugBridge != null) {
            sDebugBridge = null;
            AndroidDebugBridge.terminate();
        }
    }

    public static boolean isInitialized() {
        return sDebugBridge != null;
    }

    public static List<IDevice> getDevices() {
        return Arrays.asList(sDebugBridge.getDevices());
    }

    private static void waitDevicesList(AndroidDebugBridge bridge) {
        int count = 0;
        while (bridge.hasInitialDeviceList() == false) {
            try {
                Thread.sleep(500);
                count++;
            } catch (InterruptedException e) {
            }
            if (count > 60) {
                System.err.print("等待获取设备超时");
                break;
            }
        }
    }

    public static IDevice pickDevice() throws IOException {
        List<IDevice> devices = getDevices();

        if (devices.size() == 0) {
            throw new IOException("No Android devices were detected by adb.");
        } else if (devices.size() >= 1) {
            return devices.get(0);
        }
        return null;
    }
}
