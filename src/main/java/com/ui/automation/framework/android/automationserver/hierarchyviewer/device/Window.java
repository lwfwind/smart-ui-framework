/*
 * Copyright (C) 2008 The Android Open Source Project
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

/**
 * Used for storing a window from the window manager service on the device.
 * These are the windows that the device selector shows.
 */
public class Window {

    private String title;

    private int hashCode;

    private IDevice device;

    /**
     * Instantiates a new Window.
     *
     * @param device   the device
     * @param title    the title
     * @param hashCode the hash code
     */
    public Window(IDevice device, String title, int hashCode) {
        this.device = device;
        this.title = title;
        this.hashCode = hashCode;
    }

    /**
     * Gets focused window.
     *
     * @param device the device
     * @return the focused window
     */
    public static Window getFocusedWindow(IDevice device) {
        return new Window(device, "<Focused Window>", -1);
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets hash code.
     *
     * @return the hash code
     */
    public int getHashCode() {
        return hashCode;
    }

    /**
     * Encode string.
     *
     * @return the string
     */
    public String encode() {
        return Integer.toHexString(hashCode);
    }

    @Override
    public String toString() {
        return title;
    }

    /**
     * Gets device.
     *
     * @return the device
     */
    public IDevice getDevice() {
        return device;
    }

    /*
     * After each refresh of the windows in the device selector, the windows are
     * different instances and automatically reselecting the same window doesn't
     * work in the device selector unless the equals method is defined here.
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Window) {
            return hashCode == ((Window) other).hashCode
                    && device.getSerialNumber().equals(((Window) other).device.getSerialNumber());
        }
        return false;
    }
}
