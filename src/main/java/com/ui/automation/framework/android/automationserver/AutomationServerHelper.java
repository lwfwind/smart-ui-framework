package com.ui.automation.framework.android.automationserver;

import com.android.ddmlib.IDevice;
import com.ui.automation.framework.android.DebugBridge;
import com.ui.automation.framework.android.automationserver.hierarchyviewer.HierarchyViewer;
import com.ui.automation.framework.android.automationserver.hierarchyviewer.device.DeviceBridge;
import com.ui.automation.framework.android.automationserver.hierarchyviewer.device.DeviceConnection;
import com.ui.automation.serializable.Point;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.ObjectInputStream;


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
            logger.error(e.getMessage(), e);
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

            Point point1 = AutomationServerHelper.getViewCenter("editUsername");
            logger.info(point1.getX());
            logger.info(point1.getY());

            Point point2 = AutomationServerHelper.getViewCenter("欧美老师", 0);
            logger.info(point2.getX());
            logger.info(point2.getY());

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            DebugBridge.terminate();
        }
    }

    /**
     * highlight boolean.
     *
     * @param flag the flag
     * @return the boolean
     */
    public static boolean highlightElement(boolean flag) {
        boolean returnValue = false;
        DeviceConnection connection = null;
        try {
            connection = new DeviceConnection(device);
            if (flag) {
                connection.sendCommand("highlight 1");
            } else {
                connection.sendCommand("highlight 0");
            }
            String line = connection.getInputStream().readLine();
            if (line != null) {
                returnValue = Boolean.valueOf(line);
            }
        } catch (Exception e) {
            logger.error(e);
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
            logger.error(e);
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
                logger.error(e.getMessage(), e);
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
            logger.error(e);
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
            connection.sendCommand("toast -t 20000 -ex " + excludeText);
            String line = connection.getInputStream().readLine();
            if (line != null) {
                returnValue = line;
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return returnValue;
    }

    /**
     * Gets view center.
     *
     * @param id the id
     * @return the view center
     */
    public static Point getViewCenter(String id) {
        Point returnValue = null;
        DeviceConnection connection = null;
        try {
            connection = new DeviceConnection(device);
            connection.sendCommand("center -t " + id);
            ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(connection.getInStream()));
            Object obj = is.readObject();
            if (obj != null) {
                returnValue = (Point) obj;
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return returnValue;
    }

    /**
     * Gets view center.
     *
     * @param text  the text
     * @param index the index
     * @return the view center
     */
    public static Point getViewCenter(String text, int index) {
        Point returnValue = null;
        DeviceConnection connection = null;
        try {
            connection = new DeviceConnection(device);
            connection.sendCommand("center -t " + text + " -i " + index);
            ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(connection.getInStream()));
            Object obj = is.readObject();
            if (obj != null) {
                returnValue = (Point) obj;
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return returnValue;
    }
}
