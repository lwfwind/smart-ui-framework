package com.qa.framework.android.automationserver;

import com.android.ddmlib.IDevice;
import com.qa.framework.android.DebugBridge;
import com.qa.framework.android.automationserver.hierarchyviewer.HierarchyViewer;
import com.qa.framework.android.automationserver.hierarchyviewer.device.DeviceBridge;
import com.qa.framework.android.automationserver.hierarchyviewer.device.DeviceConnection;
import org.apache.log4j.Logger;

import java.awt.*;


/**
 * The type Automation server helper.
 */
public class AutomationServerHelper {
    private static Logger logger = Logger.getLogger(AutomationServerHelper.class);
    private static IDevice device = null;
    private static HierarchyViewer hierarchyViewer = null;

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
                hierarchyViewer = new HierarchyViewer(device);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        try {
            logger.info("isMusicActive:" + (isMusicActive() ? "true" : "false"));
            logger.info(getLastToast());

            Rectangle rectangle = getElementLocationByText("15:20", 2);
            if (rectangle != null) {
                logger.info("result left:" + rectangle.x + " top:" + rectangle.y + " width:" + rectangle.width + " height:" + rectangle.height);
            }
            logger.info(getElementTextById("id/editUsername"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DebugBridge.terminate();
        }
    }


    /**
     * Is music active boolean.
     *
     * @return the boolean
     */
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
            logger.error("Unable to get automation server info from device " + device);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return returnValue;
    }

    /**
     * Is music active boolean.
     *
     * @param timeout the timeout
     * @return the boolean
     */
    public static boolean isMusicActive(long timeout) {
        long currentTime = System.currentTimeMillis();
        long maxTime = currentTime + timeout;
        while (currentTime < maxTime) {
            if (isMusicActive()) {
                return true;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentTime = System.currentTimeMillis();
        }
        return false;
    }


    /**
     * Gets last toast.
     *
     * @return the last toast
     */
    public static String getLastToast() {
        String returnValue = "";
        DeviceConnection connection = null;
        try {
            connection = new DeviceConnection(device);
            connection.sendCommand("toast -t 20000");
            String line = connection.getInputStream().readLine();
            if (line != null) {
                returnValue = line;
            }
        } catch (Exception e) {
            logger.error("Unable to get toast from device " + device);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return returnValue;
    }

    /**
     * Gets last toast.
     *
     * @param excludeText the exclude text
     * @return the last toast
     */
    public static String getLastToast(String excludeText) {
        String returnValue = "";
        DeviceConnection connection = null;
        try {
            connection = new DeviceConnection(device);
            connection.sendCommand("toast -t 20000 -ex "+excludeText);
            String line = connection.getInputStream().readLine();
            if (line != null) {
                returnValue = line;
            }
        } catch (Exception e) {
            logger.error("Unable to get toast from device " + device);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return returnValue;
    }

    /**
     * Gets element location by text.
     *
     * @param text  the text
     * @param index the index
     * @return the element location by text
     */
    public static Rectangle getElementLocationByText(String text, int index) {
        return hierarchyViewer.getElementLocationByText(text, index);
    }

    /**
     * Gets element center by text.
     *
     * @param text  the text
     * @param index the index
     * @return the element center by text
     */
    public static Point getElementCenterByText(String text, int index) {
        return hierarchyViewer.getElementCenterByText(text, index);
    }

    /**
     * Gets element center by text.
     *
     * @param text the text
     * @return the element center by text
     */
    public static Point getElementCenterByText(String text) {
        return hierarchyViewer.getElementCenterByText(text, 0);
    }

    /**
     * Gets element text by id.
     *
     * @param id the id
     * @return the element text by id
     */
    public static String getElementTextById(String id) {
        return hierarchyViewer.getElementTextById(id);
    }
}
