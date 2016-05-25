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

package com.qa.framework.android.automationserver;

import com.android.ddmlib.*;

import java.io.IOException;
import java.util.HashMap;

/**
 * A bridge to the device.
 */
public class DeviceBridge {

    public static final String TAG = "automationserver";
    private static final int DEFAULT_SERVER_PORT = 4000;
    private static final HashMap<IDevice, Integer> devicePortMap = new HashMap<IDevice, Integer>();
    private static int nextLocalPort = DEFAULT_SERVER_PORT;


    /**
     * Sets up a just-connected device to work with the automation server.
     * <p/>
     * This starts a port forwarding between a local port and a port on the
     * device.
     * 
     * @param device
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


}
