package com.ui.automation.framework.config;

import com.android.ddmlib.IDevice;
import com.library.common.IOHelper;
import com.library.common.StringHelper;
import com.ui.automation.framework.android.AppiumServer;
import com.ui.automation.framework.android.DebugBridge;
import com.ui.automation.framework.android.Emulator;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ThreadGuard;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Stack;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Driver config.
 */
public class DriverConfig {
    private static final String UNKNOWN_BROWSER_TYPE = "' is an unknown browser type!";
    private static final String INTERNET_EXPLORER = "internet explorer";
    private final static Logger logger = Logger
            .getLogger(DriverConfig.class);
    private static WebDriver driverObject = null;
    private transient static Stack<String> stack = new Stack<String>();
    private transient static selectedBrowser browserType;
    private transient static htmlUnitEmulation emulationType;
    private static boolean isFirstLaunch = true;
    private static boolean isDownloaded = false;
    private static String appBinName = "";

    static {
        if (PropConfig.get().getCoreType().contains(",")) {
            String[] browsers = PropConfig.get().getCoreType().split(",");
            for (String browser : browsers) {
                stack.push(browser.trim());
            }
        } else {
            setBrowser(PropConfig.get().getCoreType());
        }
        if (PropConfig.get().getHtmlUnitEmulationType() != null) {
            setHTMLUnitEmulation(PropConfig.get().getHtmlUnitEmulationType());
        } else {
            setHTMLUnitEmulation(htmlUnitEmulation.FIREFOX);
        }
    }

    /**
     * Set selected browser via Enum
     */
    private static void setBrowser(final selectedBrowser value) {
        browserType = value;
    }

    /**
     * Set selected browser If emulation type is not recognised it will default
     * to none.
     */
    private static void setBrowser(String value) {
        for (selectedBrowser browser : selectedBrowser.values()) {
            if (browser.toString().equalsIgnoreCase(value)) {
                setBrowser(browser);
                return;
            }
        }
        logger.error("'" + value + UNKNOWN_BROWSER_TYPE);
    }

    /**
     * Set HTMLUnit emulation via Enum
     */
    private static void setHTMLUnitEmulation(htmlUnitEmulation value) {
        emulationType = value;
    }

    /**
     * Set HTMLUnit emulation. If emulation type is not recognised it will
     * default to none.
     */
    private static void setHTMLUnitEmulation(String value) {
        for (htmlUnitEmulation emulation : htmlUnitEmulation.values()) {
            if (emulation.toString().equalsIgnoreCase(value)) {
                setHTMLUnitEmulation(emulation);
                return;
            }
        }
        setHTMLUnitEmulation(htmlUnitEmulation.FIREFOX);
    }

    /**
     * Create and get the corresponding driver object based upon the selected
     * browserType Initialized in construct.
     *
     * @param browserType the browser type
     * @return WebDriver driver object
     * @throws Exception the exception
     */
    public static synchronized WebDriver getDriverObject(selectedBrowser browserType) throws Exception {
        try {
            String appiumServerUrl;
            File app;
            DesiredCapabilities capabilities = null;
            File file;
            Resource resource;
            Properties sysProp = System.getProperties();
            String osName = sysProp.getProperty("os.name");
            String osArch = sysProp.getProperty("os.arch");
            switch (browserType) {
                case FIREFOX:
                    if (osName.startsWith("Win") && osArch.contains("64")) {
                        resource = new ClassPathResource("static"+File.separator+"geckodriver"+File.separator+"geckodriver_win64.exe");
                    } else if (osName.startsWith("Win") && !osArch.contains("64")) {
                        resource = new ClassPathResource("static"+File.separator+"geckodriver"+File.separator+"geckodriver_win32.exe");
                    } else {
                        resource = new ClassPathResource("static"+File.separator+"geckodriver"+File.separator+"geckodriver");
                    }
                    file = resource.getFile();
                    System.setProperty("webdriver.gecko.driver",
                            file.getAbsolutePath());
                    FirefoxProfile fp = new FirefoxProfile();
                    fp.setPreference("browser.startup.homepage", "about:blank");
                    fp.setPreference("startup.homepage_welcome_url", "about:blank");
                    fp.setPreference("startup.homepage_welcome_url.additional", "about:blank");
                    if (PropConfig.get().isDebug()) {
                        resource = new ClassPathResource("static"+File.separator+"firefoxextensions");
                        List<String> xpiFiles = IOHelper.listFilesInDirectory(resource.getFile().getAbsolutePath(), "*.xpi");
                        for (String xpi : xpiFiles) {
                            fp.addExtension(new File(xpi));
                        }
                    }
                    driverObject = ThreadGuard.protect(new FirefoxDriver(fp));
                    logger.info("Using FIREFOX Driver...");
                    break;
                case IE:
                    if (osArch.contains("64")) {
                        resource = new ClassPathResource("static"+File.separator+"iedriver"+File.separator+"64"+File.separator+"IEDriverServer.exe");
                    } else {
                        resource = new ClassPathResource("static"+File.separator+"iedriver"+File.separator+"32"+File.separator+"IEDriverServer.exe");
                    }
                    file = resource.getFile();
                    System.setProperty("webdriver.ie.driver",
                            file.getAbsolutePath());

                    capabilities = DesiredCapabilities.internetExplorer();
                    capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                    capabilities.setCapability("requireWindowFocus", true);
                    driverObject = ThreadGuard.protect(new InternetExplorerDriver(capabilities));
                    logger.info("Using INTERNET EXPLORER Driver...");
                    break;
                case GOOGLECHROME:
                    if (osName.startsWith("Win")) {
                        resource = new ClassPathResource("static"+File.separator+"chromedriver"+File.separator+"chromedriver_for_win.exe");
                    } else {
                        resource = new ClassPathResource("static"+File.separator+"chromedriver"+File.separator+"chromedriver");
                    }
                    file = resource.getFile();
                    System.setProperty("webdriver.chrome.driver",
                            file.getAbsolutePath());
                    capabilities = DesiredCapabilities.chrome();
                    LoggingPreferences loggingprefs = new LoggingPreferences();
                    loggingprefs.enable(LogType.BROWSER, Level.ALL);
                    capabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingprefs);
                    if (PropConfig.get().isDebug()) {
                        ChromeOptions options = new ChromeOptions();
                        resource = new ClassPathResource("static"+File.separator+"chromextensions");
                        List<String> crxFiles = IOHelper.listFilesInDirectory(resource.getFile().getAbsolutePath(), "*.crx");
                        for (String crx : crxFiles) {
                            options.addExtensions(new File(crx));
                        }
                        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                    }
                    driverObject = ThreadGuard.protect(new ChromeDriver(capabilities));
                    logger.info("Using GOOGLECHROME Driver...");
                    break;
                case HTMLUNIT:
                    if (PropConfig.get().getHtmlUnitProxy() != null) {
                        driverObject = new HtmlUnitDriver(
                                setHTMLUnitCapabilitiesWithProxy(
                                        PropConfig.get().getHtmlUnitProxy(),
                                        emulationType));
                        logger.info("Using HTMLUNIT Driver... with proxy "
                                + PropConfig.get().getHtmlUnitProxy());
                    } else {
                        driverObject = new HtmlUnitDriver(
                                setHTMLUnitCapabilities(emulationType));
                        logger.info("Using HTMLUNIT Driver...");
                    }
                    break;
                case SAFARI:
                    driverObject = ThreadGuard.protect(new SafariDriver());
                    logger.info("Using Opera Driver...");
                    break;
                case OPERA:
                    driverObject = ThreadGuard.protect(new OperaDriver());
                    logger.info("Using Opera Driver...");
                    break;
                case ANDROIDAPP:
                    appiumServerUrl = getAppiumServerUrl();
                    app = getAppBin();
                    capabilities = new DesiredCapabilities();
                    if (PropConfig.get().getDeviceName() != null && isFirstLaunch) {
                        Emulator emulator = new Emulator(PropConfig.get().getDeviceName());
                        emulator.restartEmulator();
                        capabilities.setCapability("deviceName", emulator.getDeviceName());
                        isFirstLaunch = false;
                    } else {
                        IDevice device = DebugBridge.getDevice();
                        if (device != null) {
                            capabilities.setCapability("deviceName", device.getSerialNumber());
                        }
                    }
                    if (PropConfig.get().isNoReset()) {
                        capabilities.setCapability("noReset", true);
                    }
                    capabilities.setCapability("app", app.getAbsolutePath());
                    capabilities.setCapability("unicodeKeyboard", true);
                    capabilities.setCapability("resetKeyboard", true);
                    driverObject = new AndroidDriver<>(new URL(appiumServerUrl), capabilities);
                    logger.info("Using Android Driver...");
                    break;
                case IOSAPP:
                    appiumServerUrl = getAppiumServerUrl();
                    app = getAppBin();
                    capabilities = new DesiredCapabilities();
                    if (PropConfig.get().getDeviceName() != null && PropConfig.get().getUuid() != null & PropConfig.get().getPlantfromVersion() != null) {
                        capabilities.setCapability("deviceName", PropConfig.get().getDeviceName());
                        capabilities.setCapability("udid", PropConfig.get().getUuid());
                        capabilities.setCapability("plantfromVersion", PropConfig.get().getPlantfromVersion());
                    } else {
                        IDevice device = DebugBridge.getDevice();
                        if (device != null) {
                            capabilities.setCapability("deviceName", device.getSerialNumber());
                        }
                    }
                    capabilities.setCapability("platfromName", "ios");
                    capabilities.setCapability("unicodeKeyboard", true);
                    capabilities.setCapability("resetKeyboard", true);
                    capabilities.setCapability("nativeWebTap", true);
                    capabilities.setCapability("app", app.getAbsolutePath());
                    driverObject = new IOSDriver<>(new URL(appiumServerUrl), capabilities);
                    logger.info("Using IOS Driver...");
                    break;
                default:
                    logger.error("'" + browserType + UNKNOWN_BROWSER_TYPE);
                    throw new Exception("'" + browserType
                            + UNKNOWN_BROWSER_TYPE);
            }

        } catch (Exception x) {
            logger.error("Error in getDriverObject{}" + x.getMessage());
            throw new Exception("Error in getDriverObject{}" + x.getMessage());
        }
        return driverObject;
    }

    private static File getAppBin() throws IOException {
        File app = null;
        Resource resource = new ClassPathResource("static"+File.separator);
        String resourcePath = resource.getFile().getAbsolutePath();
        if (PropConfig.get().getAppBin().contains("http") && !isDownloaded)
            if (PropConfig.get().getAppBin().endsWith("apk") || PropConfig.get().getAppBin().endsWith("ipa") || PropConfig.get().getAppBin().endsWith("app")) {
                appBinName = IOHelper.getName(PropConfig.get().getAppBin());
                IOHelper.deleteFile(resourcePath + appBinName);
                IOHelper.downFileFromUrl(PropConfig.get().getAppBin(), resourcePath + appBinName);
                app = new File(resourcePath, appBinName);
            } else {
                String source = IOHelper.getSourceFromUrl(PropConfig.get().getAppBin());
                List<String> lines = StringHelper.getTokensList(source, "\n");
                Collections.reverse(lines);
                String matchedNumber = "";
                for (String line : lines) {
                    Pattern r = Pattern.compile("\\d\\d-\\d\\d-\\d\\d-\\d\\d-\\d\\d");
                    Matcher m = r.matcher(line);
                    if (m.find()) {
                        matchedNumber = m.group(0);
                        String nextSource;
                        if (PropConfig.get().getAppBin().endsWith("/")) {
                            nextSource = IOHelper.getSourceFromUrl(PropConfig.get().getAppBin() + matchedNumber);
                        } else {
                            nextSource = IOHelper.getSourceFromUrl(PropConfig.get().getAppBin() + "/" + matchedNumber);
                        }
                        if (nextSource.contains(".apk")) {
                            Pattern r1 = Pattern.compile("href=\"(.*\\.apk)");
                            Matcher m1 = r1.matcher(nextSource);
                            if (m1.find()) {
                                appBinName = m1.group(1);
                                break;
                            }
                        }
                        if (nextSource.contains(".ipa")) {
                            Pattern r1 = Pattern.compile("href=\"(.*\\.ipa)");
                            Matcher m1 = r1.matcher(nextSource);
                            if (m1.find()) {
                                appBinName = m1.group(1);
                                break;
                            }
                        }
                        if (nextSource.contains(".app")) {
                            Pattern r1 = Pattern.compile("href=\"(.*\\.app)");
                            Matcher m1 = r1.matcher(nextSource);
                            if (m1.find()) {
                                appBinName = m1.group(1);
                                break;
                            }
                        }
                    } else {
                        Pattern pattern = null;
                        if (line.contains(".apk")) {
                            pattern = Pattern.compile("href=\"(.*\\.apk)");
                        } else if (line.contains(".ipa")) {
                            pattern = Pattern.compile("href=\"(.*\\.ipa)");
                        } else if (line.contains(".app")) {
                            pattern = Pattern.compile("href=\"(.*\\.app)");
                        }
                        if (pattern != null) {
                            Matcher m1 = pattern.matcher(line);
                            if (m1.find()) {
                                appBinName = m1.group(1);
                                break;
                            }
                        }
                    }
                }
                String fullUrl;
                if (PropConfig.get().getAppBin().endsWith("/")) {
                    if (matchedNumber.equals("")) {
                        fullUrl = PropConfig.get().getAppBin() + appBinName;
                    } else {
                        fullUrl = PropConfig.get().getAppBin() + matchedNumber + "/" + appBinName;
                    }
                } else {
                    if (matchedNumber.equals("")) {
                        fullUrl = PropConfig.get().getAppBin() + "/" + appBinName;
                    } else {
                        fullUrl = PropConfig.get().getAppBin() + "/" + matchedNumber + "/" + appBinName;
                    }
                }
                logger.info("download file:" + fullUrl);
                IOHelper.deleteFile(resourcePath + appBinName);
                IOHelper.downFileFromUrl(fullUrl, resourcePath + appBinName);
                app = new File(resourcePath, appBinName);
                isDownloaded = true;
            }
        else {
            if (PropConfig.get().getAppBin().contains("http")) {
                app = new File(resourcePath, appBinName);
            } else {
                app = new File(resourcePath, PropConfig.get().getAppBin());
            }
        }
        return app;
    }

    /**
     * Gets appium server url.
     *
     * @return the appium server url
     */
    public static String getAppiumServerUrl() {
        String appiumServerUrl = PropConfig.get().getAppiumServerUrl();
        if (appiumServerUrl == null) {
            AppiumServer.start("127.0.0.1", 4723);
            appiumServerUrl = "http://127.0.0.1:4723/wd/hub";
        } else {
            String url = StringHelper.getTokensList(appiumServerUrl.substring(7), "/").get(0);
            String address = StringHelper.getTokensList(url, ":").get(0);
            int port = Integer.parseInt(StringHelper.getTokensList(url, ":").get(1));
            if ((address.equalsIgnoreCase("127.0.0.1") || address.equalsIgnoreCase("localhost")) && AppiumServer.available(port)) {
                AppiumServer.start("127.0.0.1", port);
            }
        }
        return appiumServerUrl;
    }

    /**
     * Gets driver object.
     *
     * @return the driver object
     * @throws Exception the exception
     */
    public static WebDriver getDriverObject() throws Exception {
        if (!stack.isEmpty()) {
            setBrowser(stack.pop());
        }
        return getDriverObject(browserType);
    }

    /**
     * Configure the HTMLUnit capabilities if using HTMLUnit Driver
     */
    private static DesiredCapabilities setHTMLUnitCapabilities(
            htmlUnitEmulation emulationType) {
        DesiredCapabilities capabilities = DesiredCapabilities.htmlUnit();
        capabilities.setJavascriptEnabled(true);
        switch (emulationType) {
            case FIREFOX:
                capabilities.setBrowserName("firefox");
                break;
            case IE6:
                capabilities.setBrowserName(INTERNET_EXPLORER);
                capabilities.setVersion("6");
                break;
            case IE7:
                capabilities.setBrowserName(INTERNET_EXPLORER);
                capabilities.setVersion("7");
                break;
            case IE8:
                capabilities.setBrowserName(INTERNET_EXPLORER);
                capabilities.setVersion("8");
                break;
            default:
                break;
        }
        return capabilities;
    }

    /**
     * Configure the HTMLUnit capabilities if using HTMLUnit Driver
     */
    private static DesiredCapabilities setHTMLUnitCapabilitiesWithProxy(
            String httpProxy, htmlUnitEmulation emulationType) {
        Proxy proxy = new Proxy();
        proxy.setAutodetect(false);
        proxy.setHttpProxy(httpProxy);
        DesiredCapabilities capabilities = DesiredCapabilities.htmlUnit();
        capabilities.setCapability(CapabilityType.PROXY, proxy);
        capabilities.setJavascriptEnabled(true);
        switch (emulationType) {
            case FIREFOX:
                capabilities.setBrowserName("firefox");
                break;
            case IE6:
                capabilities.setBrowserName(INTERNET_EXPLORER);
                capabilities.setVersion("6");
                break;
            case IE7:
                capabilities.setBrowserName(INTERNET_EXPLORER);
                capabilities.setVersion("7");
                break;
            case IE8:
                capabilities.setBrowserName(INTERNET_EXPLORER);
                capabilities.setVersion("8");
                break;
            default:
                break;
        }
        return capabilities;
    }

    /**
     * Gets browser type.
     *
     * @return the browser type
     */
    public static selectedBrowser getBrowserType() {
        return browserType;
    }

    /**
     * The enum Selected browser.
     */
    public static enum selectedBrowser {

        /**
         * Firefox selected browser.
         */
        FIREFOX,
        /**
         * Ie selected browser.
         */
        IE,
        /**
         * Safari selected browser.
         */
        SAFARI,
        /**
         * Opera selected browser.
         */
        OPERA,
        /**
         * Googlechrome selected browser.
         */
        GOOGLECHROME,
        /**
         * Androidapp selected browser.
         */
        ANDROIDAPP,
        /**
         * Iosapp selected browser.
         */
        IOSAPP,
        /**
         * Htmlunit selected browser.
         */
        HTMLUNIT
    }

    private static enum htmlUnitEmulation {

        /**
         * None html unit emulation.
         */
        NONE,
        /**
         * Firefox html unit emulation.
         */
        FIREFOX,
        /**
         * Ie 6 html unit emulation.
         */
        IE6,
        /**
         * Ie 7 html unit emulation.
         */
        IE7,
        /**
         * Ie 8 html unit emulation.
         */
        IE8
    }

}
