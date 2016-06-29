/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.qa.framework.android.automationserver.hierarchyviewer.device;

import com.android.ddmlib.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A bridge to the device.
 */
public class DeviceBridge {

    /**
     * The constant TAG.
     */
    public static final String TAG = "hierarchyviewer";

    private static final int DEFAULT_SERVER_PORT = 4939;

    // These codes must match the auto-generated codes in IWindowManager.java
    // See IWindowManager.aidl as well
    private static final int SERVICE_CODE_START_SERVER = 1;

    private static final int SERVICE_CODE_STOP_SERVER = 2;

    private static final int SERVICE_CODE_IS_SERVER_RUNNING = 3;
    private static final HashMap<IDevice, Integer> devicePortMap = new HashMap<IDevice, Integer>();
    private static final HashMap<IDevice, ViewServerInfo> viewServerInfo =
            new HashMap<IDevice, ViewServerInfo>();
    private static int nextLocalPort = DEFAULT_SERVER_PORT;


    /**
     * Start listen for devices.
     *
     * @param listener the listener
     */
/*
     * This adds a listener to the debug bridge. The listener is notified of
     * connecting/disconnecting devices, devices coming online, etc.
     */
    public static void startListenForDevices(AndroidDebugBridge.IDeviceChangeListener listener) {
        AndroidDebugBridge.addDeviceChangeListener(listener);
    }

    /**
     * Stop listen for devices.
     *
     * @param listener the listener
     */
    public static void stopListenForDevices(AndroidDebugBridge.IDeviceChangeListener listener) {
        AndroidDebugBridge.removeDeviceChangeListener(listener);
    }

    /**
     * Sets up a just-connected device to work with the view server.
     * This starts a port forwarding between a local port and a port on the
     * device.
     *
     * @param device the device
     */
    public static void setupDeviceForward(IDevice device) {
        synchronized (devicePortMap) {
            if (device.getState() == IDevice.DeviceState.ONLINE) {
                int localPort = nextLocalPort++;
                try {
                    device.createForward(localPort, DEFAULT_SERVER_PORT);
                    devicePortMap.put(device, localPort);
                } catch (TimeoutException e) {
                    Log.e(TAG, "Timeout setting up port forwarding for " + device);
                } catch (AdbCommandRejectedException e) {
                    Log.e(TAG, String.format("Adb rejected forward command for device %1$s: %2$s",
                            device, e.getMessage()));
                } catch (IOException e) {
                    Log.e(TAG, String.format("Failed to create forward for device %1$s: %2$s",
                            device, e.getMessage()));
                }
            }
        }
    }

    /**
     * Remove device forward.
     *
     * @param device the device
     */
    public static void removeDeviceForward(IDevice device) {
        synchronized (devicePortMap) {
            final Integer localPort = devicePortMap.get(device);
            if (localPort != null) {
                try {
                    device.removeForward(localPort, DEFAULT_SERVER_PORT);
                    devicePortMap.remove(device);
                } catch (TimeoutException e) {
                    Log.e(TAG, "Timeout removing port forwarding for " + device);
                } catch (AdbCommandRejectedException e) {
                    // In this case, we want to fail silently.
                } catch (IOException e) {
                    Log.e(TAG, String.format("Failed to remove forward for device %1$s: %2$s",
                            device, e.getMessage()));
                }
            }
        }
    }

    /**
     * Gets device local port.
     *
     * @param device the device
     * @return the device local port
     */
    public static int getDeviceLocalPort(IDevice device) {
        synchronized (devicePortMap) {
            Integer port = devicePortMap.get(device);
            if (port != null) {
                return port;
            }

            Log.e(TAG, "Missing forwarded port for " + device.getSerialNumber());
            return -1;
        }

    }

    /**
     * Is view server running boolean.
     *
     * @param device the device
     * @return the boolean
     */
    public static boolean isViewServerRunning(IDevice device) {
        final boolean[] result = new boolean[1];
        try {
            if (device.isOnline()) {
                device.executeShellCommand(buildIsServerRunningShellCommand(),
                        new BooleanResultReader(result));
            }
        } catch (TimeoutException e) {
            Log.e(TAG, "Timeout checking status of view server on device " + device);
        } catch (IOException e) {
            Log.e(TAG, "Unable to check status of view server on device " + device);
        } catch (AdbCommandRejectedException e) {
            Log.e(TAG, "Adb rejected command to check status of view server on device " + device);
        } catch (ShellCommandUnresponsiveException e) {
            Log.e(TAG, "Unable to execute command to check status of view server on device "
                    + device);
        }
        return result[0];
    }

    /**
     * Start view server boolean.
     *
     * @param device the device
     * @return the boolean
     */
    public static boolean startViewServer(IDevice device) {
        return startViewServer(device, DEFAULT_SERVER_PORT);
    }

    /**
     * Start view server boolean.
     *
     * @param device the device
     * @param port   the port
     * @return the boolean
     */
    public static boolean startViewServer(IDevice device, int port) {
        final boolean[] result = new boolean[1];
        try {
            if (device.isOnline()) {
                device.executeShellCommand(buildStartServerShellCommand(port),
                        new BooleanResultReader(result));
            }
        } catch (TimeoutException e) {
            Log.e(TAG, "Timeout starting view server on device " + device);
        } catch (IOException e) {
            Log.e(TAG, "Unable to start view server on device " + device);
        } catch (AdbCommandRejectedException e) {
            Log.e(TAG, "Adb rejected command to start view server on device " + device);
        } catch (ShellCommandUnresponsiveException e) {
            Log.e(TAG, "Unable to execute command to start view server on device " + device);
        }
        return result[0];
    }

    /**
     * Stop view server boolean.
     *
     * @param device the device
     * @return the boolean
     */
    public static boolean stopViewServer(IDevice device) {
        final boolean[] result = new boolean[1];
        try {
            if (device.isOnline()) {
                device.executeShellCommand(buildStopServerShellCommand(), new BooleanResultReader(
                        result));
            }
        } catch (TimeoutException e) {
            Log.e(TAG, "Timeout stopping view server on device " + device);
        } catch (IOException e) {
            Log.e(TAG, "Unable to stop view server on device " + device);
        } catch (AdbCommandRejectedException e) {
            Log.e(TAG, "Adb rejected command to stop view server on device " + device);
        } catch (ShellCommandUnresponsiveException e) {
            Log.e(TAG, "Unable to execute command to stop view server on device " + device);
        }
        return result[0];
    }

    private static String buildStartServerShellCommand(int port) {
        return String.format("service call window %d i32 %d", SERVICE_CODE_START_SERVER, port);
    }

    private static String buildStopServerShellCommand() {
        return String.format("service call window %d", SERVICE_CODE_STOP_SERVER);
    }

    private static String buildIsServerRunningShellCommand() {
        return String.format("service call window %d", SERVICE_CODE_IS_SERVER_RUNNING);
    }

    /**
     * Load view server info view server info.
     *
     * @param device the device
     * @return the view server info
     */
    public static ViewServerInfo loadViewServerInfo(IDevice device) {
        int server = -1;
        int protocol = -1;
        DeviceConnection connection = null;
        try {
            connection = new DeviceConnection(device);
            connection.sendCommand("SERVER");
            String line = connection.getInputStream().readLine();
            if (line != null) {
                server = Integer.parseInt(line);
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable to get view server version from device " + device + e.getMessage());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        connection = null;
        try {
            connection = new DeviceConnection(device);
            connection.sendCommand("PROTOCOL");
            String line = connection.getInputStream().readLine();
            if (line != null) {
                protocol = Integer.parseInt(line);
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable to get view server protocol version from device " + device);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        if (server == -1 || protocol == -1) {
            return null;
        }
        ViewServerInfo returnValue = new ViewServerInfo(server, protocol);
        synchronized (viewServerInfo) {
            viewServerInfo.put(device, returnValue);
        }
        return returnValue;
    }

    /**
     * Gets view server info.
     *
     * @param device the device
     * @return the view server info
     */
    public static ViewServerInfo getViewServerInfo(IDevice device) {
        synchronized (viewServerInfo) {
            return viewServerInfo.get(device);
        }
    }

    /**
     * Remove view server info.
     *
     * @param device the device
     */
    public static void removeViewServerInfo(IDevice device) {
        synchronized (viewServerInfo) {
            viewServerInfo.remove(device);
        }
    }

    /**
     * Load windows window [ ].
     *
     * @param device the device
     * @return the window [ ]
     */
/*
     * This loads the list of windows from the specified device. The format is:
     * hashCode1 title1 hashCode2 title2 ... hashCodeN titleN DONE.
     */
    public static Window[] loadWindows(IDevice device) {
        ArrayList<Window> windows = new ArrayList<Window>();
        DeviceConnection connection = null;
        ViewServerInfo serverInfo = getViewServerInfo(device);
        try {
            connection = new DeviceConnection(device);
            connection.sendCommand("LIST");
            BufferedReader in = connection.getInputStream();
            String line;
            while ((line = in.readLine()) != null) {
                if ("DONE.".equalsIgnoreCase(line)) {
                    break;
                }

                int index = line.indexOf(' ');
                if (index != -1) {
                    String windowId = line.substring(0, index);

                    int id;
                    if (serverInfo.serverVersion > 2) {
                        id = (int) Long.parseLong(windowId, 16);
                    } else {
                        id = Integer.parseInt(windowId, 16);
                    }

                    Window w = new Window(device, line.substring(index + 1), id);
                    windows.add(w);
                }
            }
            // Automatic refreshing of windows was added in protocol version 3.
            // Before, the user needed to specify explicitly that he wants to
            // get the focused window, which was done using a special type of
            // window with hash code -1.
            if (serverInfo.protocolVersion < 3) {
                windows.add(Window.getFocusedWindow(device));
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable to load the window list from device " + device + e.getMessage());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        // The server returns the list of windows from the window at the bottom
        // to the top. We want the reverse order to put the top window on top of
        // the list.
        Window[] returnValue = new Window[windows.size()];
        for (int i = windows.size() - 1; i >= 0; i--) {
            returnValue[returnValue.length - i - 1] = windows.get(i);
        }
        return returnValue;
    }

    /**
     * Gets focused window.
     *
     * @param device the device
     * @return the focused window
     */
/*
     * This gets the hash code of the window that has focus. Only works with
     * protocol version 3 and above.
     */
    public static int getFocusedWindow(IDevice device) {
        DeviceConnection connection = null;
        try {
            connection = new DeviceConnection(device);
            connection.sendCommand("GET_FOCUS");
            String line = connection.getInputStream().readLine();
            if (line == null || line.length() == 0) {
                return -1;
            }
            return (int) Long.parseLong(line.substring(0, line.indexOf(' ')), 16);
        } catch (Exception e) {
            Log.e(TAG, "Unable to get the focused window from device " + device);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return -1;
    }

    /**
     * Load window data view node.
     *
     * @param window the window
     * @return the view node
     */
    public static ViewNode loadWindowData(Window window) {
        DeviceConnection connection = null;
        try {
            connection = new DeviceConnection(window.getDevice());
            connection.sendCommand("DUMP " + window.encode());
            BufferedReader in = connection.getInputStream();
            ViewNode currentNode = null;
            int currentDepth = -1;
            String line;
            while ((line = in.readLine()) != null) {
                if ("DONE.".equalsIgnoreCase(line)) {
                    break;
                }
                int depth = 0;
                while (line.charAt(depth) == ' ') {
                    depth++;
                }
                while (depth <= currentDepth) {
                    currentNode = currentNode.parent;
                    currentDepth--;
                }
                currentNode = new ViewNode(window, currentNode, line.substring(depth));
                currentDepth = depth;
            }
            if (currentNode == null) {
                return null;
            }
            while (currentNode.parent != null) {
                currentNode = currentNode.parent;
            }
            ViewServerInfo serverInfo = getViewServerInfo(window.getDevice());
            if (serverInfo != null) {
                currentNode.protocolVersion = serverInfo.protocolVersion;
            }
            return currentNode;
        } catch (Exception e) {
            Log.e(TAG, "Unable to load window data for window " + window.getTitle() + " on device "
                    + window.getDevice());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return null;
    }

    private static boolean loadProfileData(ViewNode node, BufferedReader in) throws IOException {
        String line;
        if ((line = in.readLine()) == null || line.equalsIgnoreCase("-1 -1 -1")
                || line.equalsIgnoreCase("DONE.")) {
            return false;
        }
        String[] data = line.split(" ");
        node.measureTime = (Long.parseLong(data[0]) / 1000.0) / 1000.0;
        node.layoutTime = (Long.parseLong(data[1]) / 1000.0) / 1000.0;
        node.drawTime = (Long.parseLong(data[2]) / 1000.0) / 1000.0;
        return true;
    }

    private static boolean loadProfileDataRecursive(ViewNode node, BufferedReader in)
            throws IOException {
        if (!loadProfileData(node, in)) {
            return false;
        }
        for (int i = 0; i < node.children.size(); i++) {
            if (!loadProfileDataRecursive(node.children.get(i), in)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Invalidate view.
     *
     * @param viewNode the view node
     */
    public static void invalidateView(ViewNode viewNode) {
        DeviceConnection connection = null;
        try {
            connection = new DeviceConnection(viewNode.window.getDevice());
            connection.sendCommand("INVALIDATE " + viewNode.window.encode() + " " + viewNode);
        } catch (Exception e) {
            Log.e(TAG, "Unable to invalidate view " + viewNode + " in window " + viewNode.window
                    + " on device " + viewNode.window.getDevice());
        } finally {
            if(connection !=null)
            connection.close();
        }
    }

    /**
     * Request layout.
     *
     * @param viewNode the view node
     */
    public static void requestLayout(ViewNode viewNode) {
        DeviceConnection connection = null;
        try {
            connection = new DeviceConnection(viewNode.window.getDevice());
            connection.sendCommand("REQUEST_LAYOUT " + viewNode.window.encode() + " " + viewNode);
        } catch (Exception e) {
            Log.e(TAG, "Unable to request layout for node " + viewNode + " in window "
                    + viewNode.window + " on device " + viewNode.window.getDevice());
        } finally {
            if(connection !=null)
            connection.close();
        }
    }

    /**
     * The type View server info.
     */
    public static class ViewServerInfo {
        /**
         * The Protocol version.
         */
        public final int protocolVersion;

        /**
         * The Server version.
         */
        public final int serverVersion;

        /**
         * Instantiates a new View server info.
         *
         * @param serverVersion   the server version
         * @param protocolVersion the protocol version
         */
        ViewServerInfo(int serverVersion, int protocolVersion) {
            this.protocolVersion = protocolVersion;
            this.serverVersion = serverVersion;
        }
    }

    private static class BooleanResultReader extends MultiLineReceiver {
        private final boolean[] mResult;

        /**
         * Instantiates a new Boolean result reader.
         *
         * @param result the result
         */
        public BooleanResultReader(boolean[] result) {
            mResult = result;
        }

        @Override
        public void processNewLines(String[] strings) {
            if (strings.length > 0) {
                Pattern pattern = Pattern.compile(".*?\\([0-9]{8} ([0-9]{8}).*");
                Matcher matcher = pattern.matcher(strings[0]);
                if (matcher.matches()) {
                    if (Integer.parseInt(matcher.group(1)) == 1) {
                        mResult[0] = true;
                    }
                }
            }
        }

        public boolean isCancelled() {
            return false;
        }
    }

}
