package com.qa.framework.android.automationserver;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.qa.framework.android.DebugBridge;
import org.apache.log4j.Logger;

import java.awt.*;
import java.io.IOException;

/**
 * Created by kcgw001 on 2016/5/25.
 */
public class AndroidAutomationHelper {
    private static Logger logger = Logger.getLogger(AndroidAutomationHelper.class);
    public static final String TAG = "AndroidAutomationHelper";

    public static void main(String[] args) {
        DebugBridge.init();
        try {
            logger.info("isMusicActive:" + (isMusicActive() ? "true" : "false"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DebugBridge.terminate();
        }
    }

    public static boolean isMusicActive() {
        boolean returnValue = false;
        IDevice device = null;
        try {
            device = DebugBridge.getDevice();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (device != null && device.isOnline()) {
            DeviceBridge.setupDeviceForward(device);
            DeviceConnection connection = null;
            try {
                connection = new DeviceConnection(device);
                connection.sendCommand("isMusicActive");
                String line = connection.getInputStream().readLine();
                if (line != null) {
                    returnValue = Boolean.valueOf(line);
                }
            } catch (Exception e) {
                Log.e(TAG, "Unable to get automation server info from device " + device);
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        }
        return returnValue;
    }
}
