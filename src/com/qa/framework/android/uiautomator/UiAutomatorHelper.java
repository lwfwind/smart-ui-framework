package com.qa.framework.android.uiautomator;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.NullOutputReceiver;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.SyncService;
import com.library.common.IOHelper;
import com.qa.framework.android.DebugBridge;
import com.qa.framework.android.automationserver.hierarchyviewer.device.DeviceBridge;
import com.qa.framework.android.uiautomator.tree.BasicTreeNode;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * The type Ui automator helper.
 */
public class UiAutomatorHelper {
    /**
     * The constant UIAUTOMATOR_MIN_API_LEVEL.
     */
    public static final int UIAUTOMATOR_MIN_API_LEVEL = 16;

    private static final String UIAUTOMATOR = "/system/bin/uiautomator";    //$NON-NLS-1$
    private static final String UIAUTOMATOR_DUMP_COMMAND = "dump";          //$NON-NLS-1$
    private static final String UIDUMP_DEVICE_PATH = "/data/local/tmp/uidump.xml";  //$NON-NLS-1$
    private static final int XML_CAPTURE_TIMEOUT_SEC = 40;
    private static IDevice device = null;
    private static Logger logger = Logger.getLogger(UiAutomatorHelper.class);

    static {
        DebugBridge.init();
        try {
            device = DebugBridge.getDevice();
            if (device != null && device.isOnline()) {
                DeviceBridge.setupDeviceForward(device);
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
            logger.info(getUiHierarchyContent());
            logger.info(searchUiHierarchyContent("QQ"));

            logger.info("start screenshot");
            BufferedImage bufferedImage = UiAutomatorHelper.takeSnapshot();
            File outputfile = new File("test.png");
            ImageIO.write(bufferedImage, "png", outputfile);
            logger.info("end screenshot");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
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

    /**
     * Take snapshot buffered image.
     *
     * @return the buffered image
     * @throws UiAutomatorException the ui automator exception
     * @throws IOException          the io exception
     */
//to maintain a backward compatible api, use non-compressed as default snapshot type
    public static BufferedImage takeSnapshot()
            throws UiAutomatorException, IOException {
        return takeSnapshot(device);
    }

    /**
     * Take snapshot buffered image.
     *
     * @param device the device
     * @return the buffered image
     * @throws UiAutomatorException the ui automator exception
     */
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
        for (int y = 0; y < rawImage.height; y++) {
            for (int x = 0; x < rawImage.width; x++) {
                int value = rawImage.getARGB(index);
                index += IndexInc;
                image.setRGB(x, y, value);
            }
        }

        return image;
    }

    /**
     * Gets ui hierarchy content.
     *
     * @return the ui hierarchy content
     */
    public static String getUiHierarchyContent() {
        try {
            return getUiHierarchyContent(device);
        } catch (UiAutomatorException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Search ui hierarchy content boolean.
     *
     * @param tofind the tofind
     * @return the boolean
     * @throws UiAutomatorException the ui automator exception
     * @throws IOException          the io exception
     */
    public static boolean searchUiHierarchyContent(String tofind) throws UiAutomatorException, IOException {
        return searchUiHierarchyContent(device, tofind);
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

        if (compressed) {
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

    private static boolean searchUiHierarchyContent(IDevice device, String tofind) throws UiAutomatorException {
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

    private static String getUiHierarchyContent(IDevice device) throws UiAutomatorException {
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
        return IOHelper.readFileToString(xmlDumpFile.getAbsolutePath());
    }

    /**
     * The type Ui automator exception.
     */
    @SuppressWarnings("serial")
    public static class UiAutomatorException extends Exception {
        /**
         * Instantiates a new Ui automator exception.
         *
         * @param msg the msg
         * @param t   the t
         */
        public UiAutomatorException(String msg, Throwable t) {
            super(msg, t);
        }
    }
}
