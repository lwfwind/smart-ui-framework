package com.qa.framework.android.automationserver;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.qa.framework.android.DebugBridge;
import com.qa.framework.android.automationserver.hierarchyviewer.HierarchyViewer;
import com.qa.framework.android.automationserver.hierarchyviewer.device.DeviceBridge;
import com.qa.framework.android.automationserver.hierarchyviewer.device.DeviceConnection;
import org.apache.log4j.Logger;
import java.awt.*;


public class AutomationServerHelper {
    private static Logger logger = Logger.getLogger(AutomationServerHelper.class);
    public static final String TAG = "AndroidAutomationHelper";
    private static IDevice device = null;

    static {
        DebugBridge.init();
        try {
            device = DebugBridge.getDevice();
            if (device != null && device.isOnline()) {
                DeviceBridge.setupDeviceForward(device);
                DeviceBridge.ViewServerInfo viewServerInfo = DeviceBridge.loadViewServerInfo(device);
                if (viewServerInfo == null) {
                    throw new Exception("viewServerInfo is not correct!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            logger.info("isMusicActive:" + (isMusicActive() ? "true" : "false"));
            logger.info(getLastToast());
            Rectangle rectangle = HierarchyViewer.getElementLocationByText("15:20",2);
            if (rectangle != null) {
                logger.info("result left:" + rectangle.x + " top:" + rectangle.y + " width:" + rectangle.width + " height:" + rectangle.height);
            }
            logger.info(HierarchyViewer.getElementTextById("id/editUsername"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DebugBridge.terminate();
        }
    }


    public static boolean isMusicActive() {
        boolean returnValue = false;
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
        return returnValue;
    }

    public static String getLastToast() {
        String returnValue = "";
        DeviceConnection connection = null;
        try {
            connection = new DeviceConnection(device);
            connection.sendCommand("toast");
            String line = connection.getInputStream().readLine();
            if (line != null) {
                returnValue = line;
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable to get toast from device " + device);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return returnValue;
    }
}
