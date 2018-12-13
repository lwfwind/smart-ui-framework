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

package com.ui.automation.framework.android.automationserver.hierarchyviewer.device;

import com.android.ddmlib.IDevice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class handles automatic updating of the list of windows in the device
 * selector for device with protocol version 3 or above of the view server. It
 * connects to the devices, keeps the connection open and listens for messages.
 * It notifies all it's listeners of changes.
 */
public class WindowUpdater {
    private static HashMap<IDevice, ArrayList<IWindowChangeListener>> windowChangeListeners =
            new HashMap<IDevice, ArrayList<IWindowChangeListener>>();

    private static HashMap<IDevice, Thread> listeningThreads = new HashMap<IDevice, Thread>();

    /**
     * Terminate.
     */
    public static void terminate() {
        synchronized (listeningThreads) {
            for (IDevice device : listeningThreads.keySet()) {
                listeningThreads.get(device).interrupt();

            }
        }
    }

    /**
     * Start listen for window changes.
     *
     * @param listener the listener
     * @param device   the device
     */
    public static void startListenForWindowChanges(IWindowChangeListener listener, IDevice device) {
        synchronized (windowChangeListeners) {
            // In this case, a listening thread already exists, so we don't need
            // to create another one.
            if (windowChangeListeners.containsKey(device)) {
                windowChangeListeners.get(device).add(listener);
                return;
            }
            ArrayList<IWindowChangeListener> listeners = new ArrayList<IWindowChangeListener>();
            listeners.add(listener);
            windowChangeListeners.put(device, listeners);
        }
        // Start listening
        Thread listeningThread = new Thread(new WindowChangeMonitor(device));
        synchronized (listeningThreads) {
            listeningThreads.put(device, listeningThread);
        }
        listeningThread.start();
    }

    /**
     * Stop listen for window changes.
     *
     * @param listener the listener
     * @param device   the device
     */
    public static void stopListenForWindowChanges(IWindowChangeListener listener, IDevice device) {
        synchronized (windowChangeListeners) {
            ArrayList<IWindowChangeListener> listeners = windowChangeListeners.get(device);
            listeners.remove(listener);
            // There are more listeners, so don't stop the listening thread.
            if (listeners.size() != 0) {
                return;
            }
            windowChangeListeners.remove(device);
        }
        // Everybody left, so the party's over!
        Thread listeningThread;
        synchronized (listeningThreads) {
            listeningThread = listeningThreads.get(device);
            listeningThreads.remove(device);
        }
        listeningThread.interrupt();
    }

    private static IWindowChangeListener[] getWindowChangeListenersAsArray(IDevice device) {
        IWindowChangeListener[] listeners;
        synchronized (windowChangeListeners) {
            ArrayList<IWindowChangeListener> windowChangeListenerList =
                    windowChangeListeners.get(device);
            if (windowChangeListenerList == null) {
                return null;
            }
            listeners =
                    windowChangeListenerList
                            .toArray(new IWindowChangeListener[windowChangeListenerList.size()]);
        }
        return listeners;
    }

    /**
     * Notify windows changed.
     *
     * @param device the device
     */
    public static void notifyWindowsChanged(IDevice device) {
        IWindowChangeListener[] listeners = getWindowChangeListenersAsArray(device);
        if (listeners != null) {
            for (int i = 0; i < listeners.length; i++) {
                listeners[i].windowsChanged(device);
            }
        }
    }

    /**
     * Notify focus changed.
     *
     * @param device the device
     */
    public static void notifyFocusChanged(IDevice device) {
        IWindowChangeListener[] listeners = getWindowChangeListenersAsArray(device);
        if (listeners != null) {
            for (int i = 0; i < listeners.length; i++) {
                listeners[i].focusChanged(device);
            }
        }
    }

    /**
     * The interface Window change listener.
     */
    public static interface IWindowChangeListener {
        /**
         * Windows changed.
         *
         * @param device the device
         */
        public void windowsChanged(IDevice device);

        /**
         * Focus changed.
         *
         * @param device the device
         */
        public void focusChanged(IDevice device);
    }

    private static class WindowChangeMonitor implements Runnable {
        private IDevice device;

        /**
         * Instantiates a new Window change monitor.
         *
         * @param device the device
         */
        public WindowChangeMonitor(IDevice device) {
            this.device = device;
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                DeviceConnection connection = null;
                try {
                    connection = new DeviceConnection(device);
                    connection.sendCommand("AUTOLIST");
                    String line;
                    while (!Thread.currentThread().isInterrupted()
                            && (line = connection.getInputStream().readLine()) != null) {
                        if (line.equalsIgnoreCase("LIST UPDATE")) {
                            notifyWindowsChanged(device);
                        } else if (line.equalsIgnoreCase("FOCUS UPDATE")) {
                            notifyFocusChanged(device);
                        }
                    }

                } catch (IOException e) {
                } finally {
                    if (connection != null) {
                        connection.close();
                    }
                }
            }
        }
    }
}
