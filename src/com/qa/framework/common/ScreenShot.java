package com.qa.framework.common;

import com.library.common.IOHelper;
import com.qa.framework.config.PropConfig;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The type Screen shot.
 */
public class ScreenShot {

    private final static Logger logger = Logger
            .getLogger(ScreenShot.class);
    /**
     * The constant time.
     */
    public static String time = new SimpleDateFormat("MM-dd-HH-mm-ss").format(new Date());
    /**
     * The constant dir.
     */
    public static String dir = "screenshots";


    /**
     * Capture fail string.
     *
     * @param driver            the driver
     * @param currentMethodName the current method name
     * @param fileName          the file name
     * @return the string
     */
    public static String captureFail(WebDriver driver, String currentMethodName, String fileName) {
        String t = new SimpleDateFormat("MM-dd-HH-mm-ss").format(new Date());
        String fail = dir + File.separator + "Fail" + File.separator + time + File.separator + currentMethodName;
        IOHelper.createNestDirectory(fail);
        String screenShotPath = fail + File.separator + t + "-" + fileName + ".jpg";

        try {
            if (PropConfig.get().getCoreType().equalsIgnoreCase("GOOGLECHROME")) {
                FullScreen fullScreen = new FullScreen(driver);
                fullScreen.take(screenShotPath);
            } else {
                File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(sourceFile, new File(screenShotPath));
            }
        } catch (Exception e) {
            logger.error("captureFail exception:" + e.toString());
        }
        if (System.getProperty("screenshotBaseURL") != null) {
            return System.getProperty("screenshotBaseURL") + File.separator + screenShotPath;
        } else {
            return screenShotPath;
        }
    }

    /**
     * Capture action string.
     *
     * @param driver            the driver
     * @param currentMethodName the current method name
     * @param fileName          the file name
     * @return the string
     */
    public static String captureAction(WebDriver driver, String currentMethodName, String fileName) {
        String t = new SimpleDateFormat("MM-dd-HH-mm-ss").format(new Date());
        String allActions = dir + File.separator + "Actions" + File.separator + time + File.separator + currentMethodName;
        File f = new File(allActions);
        if (f.exists() && f.isDirectory()) {
            String screenShotPath = allActions + File.separator + t + "-" + fileName + ".jpg";
            try {
                File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(sourceFile, new File(screenShotPath));
            } catch (Exception e) {
                logger.error("captureAction exception:" + e.toString());
            }
            return screenShotPath;
        }
        return "dir not existed";
    }

    private static class FullScreen {
        private long visibleWindowHeight;
        private long actureContentHeight;
        private long scrollTop;
        private WebDriver driver;
        private JavascriptExecutor js;
        private TakesScreenshot screenShot;

        /**
         * Instantiates a new Full screen.
         *
         * @param driver the driver
         */
        public FullScreen(WebDriver driver) {
            this.driver = driver;
            js = (JavascriptExecutor) driver;
            screenShot = (TakesScreenshot) driver;
        }

        /**
         * Custom action.
         */
        public void customAction() {
            js.executeScript("$(window).unbind();"
                    + "if($('.fix-box')){"
                    + "$('.fix-box').removeAttr('style');"
                    + "}");
        }

        /**
         * Init.
         */
        public void init() {
            actureContentHeight = (long) js.executeScript("return $(document).height();");
            visibleWindowHeight = (long) js.executeScript("return $(window).height();");
            scrollTop = (long) js.executeScript("return $(document).scrollTop()");
        }


        private void injectJQueryIfNeeded() {
            if (!isJQueryLoaded()) {
                injectJQuery();
            }
        }

        /**
         * Is j query loaded boolean.
         *
         * @return the boolean
         */
        public Boolean isJQueryLoaded() {
            Boolean isLoaded;
            try {
                isLoaded = (Boolean) js.executeScript("return typeof jQuery != 'undefined'");
            } catch (WebDriverException e) {
                isLoaded = false;
                logger.error("isJQueryLoaded exception:" + e.toString());
            }
            return isLoaded;
        }

        /**
         * Wait a second.
         */
        public void waitASecond() {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                logger.error(e.toString());
            }

        }

        /**
         * Inject j DBHelper.
         */
        public void injectJQuery() {
            js.executeScript(" var headID = document.getElementsByTagName('head')[0];"
                    + "var newScript = document.createElement('script');"
                    + "newScript.type = 'text/javascript';"
                    + "newScript.src = 'http://libs.baidu.com/jquery/1.9.1/jquery.min.js';"
                    + "headID.appendChild(newScript);");
            long end = System.currentTimeMillis() + 10000;
            while (System.currentTimeMillis() < end) {
                if (js.executeScript("return document.readyState").equals("complete") && (Boolean) js.executeScript("return typeof jQuery != 'undefined'")) {
                    break;
                }
                waitASecond();
            }
            logger.info("injectJQuery done");
        }

        /**
         * Create image from bytes buffered image.
         *
         * @param imageData the image data
         * @return the buffered image
         */
        public BufferedImage createImageFromBytes(byte[] imageData) {

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageData);
            BufferedImage bf = null;
            try {
                bf = ImageIO.read(byteArrayInputStream);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
            return bf;
        }

        /**
         * Take.
         *
         * @param path the path
         */
        public void take(String path) {
            injectJQueryIfNeeded();
            customAction();
            //将滚动条移动到顶端
            js.executeScript("scrollTo(0,0)");
            int totalHeight = 0;
            int totalWidth = 0;
            List<BufferedImage> bufferedImages = new ArrayList<BufferedImage>();
            do {
                init();
                byte[] bytes = screenShot.getScreenshotAs(OutputType.BYTES);
                BufferedImage bf = createImageFromBytes(bytes);
                bufferedImages.add(bf);
                if (bf.getWidth() > totalWidth) {
                    totalWidth = bf.getWidth();
                }
                totalHeight += bf.getHeight();
                js.executeScript("scrollBy(arguments[0], arguments[1])", 0, visibleWindowHeight);
            } while (scrollTop < actureContentHeight - visibleWindowHeight);
            BufferedImage finalBuf = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = finalBuf.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int height = 0;
            for (BufferedImage bui : bufferedImages) {
                graphics.drawImage(bui, 0, height, bui.getWidth(), bui.getHeight(), null);
                height += bui.getHeight();
            }
            graphics.dispose();
            try {
                ImageIO.write(finalBuf, "jpg", new File(path));
            } catch (IOException e) {
                logger.error("FullScreen.take exception:" + e.toString());
            }
        }

    }
}
