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

import com.android.ddmlib.*;
import com.qa.framework.library.android.uiautomator.tree.BasicTreeNode;
import org.apache.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class UiAutomatorHelper {
    public static final int UIAUTOMATOR_MIN_API_LEVEL = 16;

    private static final String UIAUTOMATOR = "/system/bin/uiautomator";    //$NON-NLS-1$
    private static final String UIAUTOMATOR_DUMP_COMMAND = "dump";          //$NON-NLS-1$
    private static final String UIDUMP_DEVICE_PATH = "/data/local/tmp/uidump.xml";  //$NON-NLS-1$
    private static final int XML_CAPTURE_TIMEOUT_SEC = 40;
    private static Logger logger = Logger.getLogger(UiAutomatorHelper.class);

    public static void main(String[] args) throws UiAutomatorException, IOException {
        DebugBridge.init();
        try {
            logger.info(searchUiHierarchyContent("登录"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DebugBridge.terminate();
        }
    }

    private static boolean supportsUiAutomator(IDevice device) {
        String apiLevelString = device.getProperty(IDevice.PROP_BUILD_API_LEVEL);
        int apiLevel;
        try {
            apiLevel = Integer.parseInt(apiLevelString);
        } catch (NumberFormatException e) {
            apiLevel = UIAUTOMATOR_MIN_API_LEVEL;
        }

        return apiLevel >= UIAUTOMATOR_MIN_API_LEVEL;
    }

    @SuppressWarnings("deprecation")
	private static void getUiHierarchyFile(IDevice device, File dst, boolean compressed) {

        String command = "rm " + UIDUMP_DEVICE_PATH;

        try {
            device.executeShellCommand(command,
                    new NullOutputReceiver(), 5 * 1000);
        } catch (Exception e1) {
            // ignore exceptions while deleting stale files
        }

        if (compressed){
            command = String.format("%s %s --compressed %s", UIAUTOMATOR,
                UIAUTOMATOR_DUMP_COMMAND,
                UIDUMP_DEVICE_PATH);
        } else {
            command = String.format("%s %s %s", UIAUTOMATOR,
                    UIAUTOMATOR_DUMP_COMMAND,
                    UIDUMP_DEVICE_PATH);
        }

        try {
            device.executeShellCommand(
                    command,
                    new NullOutputReceiver(),
                    XML_CAPTURE_TIMEOUT_SEC * 1000);
            device.getSyncService().pullFile(UIDUMP_DEVICE_PATH,
                    dst.getAbsolutePath(), SyncService.getNullProgressMonitor());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //to maintain a backward compatible api, use non-compressed as default snapshot type
    public static BufferedImage takeSnapshot()
            throws UiAutomatorException, IOException {
        IDevice device = DebugBridge.pickDevice();
        return takeSnapshot(device);
    }

    public static BufferedImage takeSnapshot(IDevice device) throws UiAutomatorException {
        if (!supportsUiAutomator(device)) {
            String msg = "UI Automator requires a device with API Level "
                                + UIAUTOMATOR_MIN_API_LEVEL;
            throw new UiAutomatorException(msg, null);
        }

        RawImage rawImage;
        try {
            rawImage = device.getScreenshot();
        } catch (Exception e) {
            String msg = "Error taking device screenshot: " + e.getMessage();
            throw new UiAutomatorException(msg, e);
        }

        BufferedImage image = new BufferedImage(rawImage.width, rawImage.height,
                BufferedImage.TYPE_INT_ARGB);

        int index = 0;
        int IndexInc = rawImage.bpp >> 3;
        for (int y = 0 ; y < rawImage.height ; y++) {
            for (int x = 0 ; x < rawImage.width ; x++) {
                int value = rawImage.getARGB(index);
                index += IndexInc;
                image.setRGB(x, y, value);
            }
        }

        return image;
    }

    public static boolean searchUiHierarchyContent(String tofind) throws UiAutomatorException, IOException {
        IDevice device = DebugBridge.pickDevice();
        return searchUiHierarchyContent(device, tofind);
    }

    public static boolean searchUiHierarchyContent(IDevice device,String tofind) throws UiAutomatorException {
        if (!supportsUiAutomator(device)) {
            String msg = "UI Automator requires a device with API Level "
                    + UIAUTOMATOR_MIN_API_LEVEL;
            throw new UiAutomatorException(msg, null);
        }

        File tmpDir = null;
        File xmlDumpFile = null;

        try {
            tmpDir = File.createTempFile("uiautomatorviewer_", "");
            tmpDir.delete();
            if (!tmpDir.mkdirs())
                throw new IOException("Failed to mkdir");
            xmlDumpFile = File.createTempFile("dump_", ".uix", tmpDir);
        } catch (Exception e) {
            String msg = "Error while creating temporary file to save snapshot: "
                    + e.getMessage();
            throw new UiAutomatorException(msg, e);
        }

        tmpDir.deleteOnExit();
        xmlDumpFile.deleteOnExit();

        try {
            UiAutomatorHelper.getUiHierarchyFile(device, xmlDumpFile, false);
        } catch (Exception e) {
            String msg = "Error while obtaining UI hierarchy XML file: " + e.getMessage();
            throw new UiAutomatorException(msg, e);
        }

        UiAutomatorModel model;
        try {
            model = new UiAutomatorModel(xmlDumpFile);
        } catch (Exception e) {
            String msg = "Error while parsing UI hierarchy XML file: " + e.getMessage();
            throw new UiAutomatorException(msg, e);
        }
        List<BasicTreeNode> lists = model.searchNode(tofind);
        return lists.size() > 0;
    }

    @SuppressWarnings("serial")
    public static class UiAutomatorException extends Exception {
        public UiAutomatorException(String msg, Throwable t) {
            super(msg, t);
        }
    }
}
