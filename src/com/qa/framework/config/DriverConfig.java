package com.qa.framework.config;

import com.opera.core.systems.OperaDriver;
import com.qa.framework.library.base.IOHelper;
import com.qa.framework.library.base.ProcessHelper;
import com.qa.framework.library.base.StringHelper;
import com.qa.framework.library.mobile.Adb;
import com.qa.framework.library.mobile.AppiumServer;
import io.appium.java_client.android.AndroidDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ThreadGuard;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.List;
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

    static {
        if (PropConfig.getCoreType().contains(",")) {
            String[] browsers = PropConfig.getCoreType().split(",");
            for (String browser : browsers) {
                stack.push(browser.trim());
            }
        } else {
            setBrowser(PropConfig.getCoreType());
        }
        if (PropConfig.getHtmlUnitEmulationType() != null) {
            setHTMLUnitEmulation(PropConfig.getHtmlUnitEmulationType());
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
        setHTMLUnitEmulation(htmlUnitEmulation.NONE);
        logger.error("'" + value + UNKNOWN_BROWSER_TYPE);
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
            DesiredCapabilities capabilities = null;
            switch (browserType) {
                case FIREFOX:
                    FirefoxProfile fp = new FirefoxProfile();
                    fp.setPreference("browser.startup.homepage", "about:blank");
                    fp.setPreference("startup.homepage_welcome_url", "about:blank");
                    fp.setPreference("startup.homepage_welcome_url.additional", "about:blank");
                    driverObject = ThreadGuard.protect(new FirefoxDriver(fp));
                    logger.info("Using FIREFOX Driver...");
                    break;
                case IE:
                    System.setProperty("webdriver.ie.driver", ProjectEnvironment.getIEDriverLocation());
                    capabilities = DesiredCapabilities.internetExplorer();
                    capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                    capabilities.setCapability("requireWindowFocus", true);
                    driverObject = ThreadGuard.protect(new InternetExplorerDriver(capabilities));
                    logger.info("Using INTERNET EXPLORER Driver...");
                    break;
                case GOOGLECHROME:
                    System.setProperty("webdriver.chrome.driver",
                            ProjectEnvironment.getChromeDriverLocation());
                    capabilities = DesiredCapabilities.chrome();
                    LoggingPreferences loggingprefs = new LoggingPreferences();
                    loggingprefs.enable(LogType.BROWSER, Level.ALL);
                    capabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingprefs);
                    driverObject = ThreadGuard.protect(new ChromeDriver(capabilities));
                    logger.info("Using GOOGLECHROME Driver...");
                    break;
                case HTMLUNIT:
                    if (PropConfig.getHtmlUnitProxy() != null) {
                        driverObject = new HtmlUnitDriver(
                                setHTMLUnitCapabilitiesWithProxy(
                                        PropConfig.getHtmlUnitProxy(),
                                        emulationType));
                        logger.info("Using HTMLUNIT Driver... with proxy "
                                + PropConfig.getHtmlUnitProxy());
                    } else {
                        driverObject = new HtmlUnitDriver(
                                setHTMLUnitCapabilities(emulationType));
                        logger.info("Using HTMLUNIT Driver...");
                    }
                    break;
                case SAFARI:
                    // FUTURE
                    break;
                case OPERA:
                    driverObject = ThreadGuard.protect(new OperaDriver());
                    logger.info("Using Opera Driver...");
                    break;
                case ANDROIDAPP:
                    String appiumServerUrl = PropConfig.getAppiumServerUrl();
                    if (appiumServerUrl == null) {
                        AppiumServer.start("127.0.0.1", 4723);
                        appiumServerUrl = "http://127.0.0.1:4723/wd/hub";
                    } else {
                        String url = StringHelper.getTokensList(appiumServerUrl.substring(7), "/").get(0);
                        String address = StringHelper.getTokensList(url, ":").get(0);
                        int port = Integer.parseInt(StringHelper.getTokensList(url, ":").get(1));
                        if ((address.equalsIgnoreCase("127.0.0.1") || address.equalsIgnoreCase("localhost")) && !ProcessHelper.portIsUsed(port)) {
                            AppiumServer.start("127.0.0.1", port);
                        }
                    }
                    File app = null;
                    if (PropConfig.getAppBin().contains("http")) {
                        if (PropConfig.getAppBin().endsWith("apk") || PropConfig.getAppBin().endsWith("ipa")) {
                            String appBinName = IOHelper.getName(PropConfig.getAppBin());
                            IOHelper.downFileFromUrl(PropConfig.getAppBin(), ProjectEnvironment.resourcePath() + appBinName);
                            app = new File(ProjectEnvironment.resourcePath(), appBinName);
                        } else {
                            String source = IOHelper.getSourceFromUrl(PropConfig.getAppBin());
                            List<String> lines = StringHelper.getTokensList(source, "\n");
                            Collections.reverse(lines);
                            String matchedNumber = "";
                            String appBinName = "";
                            for (String line : lines) {
                                Pattern r = Pattern.compile("\\d\\d-\\d\\d-\\d\\d-\\d\\d-\\d\\d");
                                Matcher m = r.matcher(line);
                                if (m.find()) {
                                    matchedNumber = m.group(0);
                                    String nextSource;
                                    if (PropConfig.getAppBin().endsWith("/")) {
                                        nextSource = IOHelper.getSourceFromUrl(PropConfig.getAppBin() + matchedNumber);
                                    } else {
                                        nextSource = IOHelper.getSourceFromUrl(PropConfig.getAppBin() + "/" + matchedNumber);
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
                                }
                            }
                            String fullUrl;
                            if (PropConfig.getAppBin().endsWith("/")) {
                                fullUrl = PropConfig.getAppBin() + matchedNumber + "/" + appBinName;
                            } else {
                                fullUrl = PropConfig.getAppBin() + "/" + matchedNumber + "/" + appBinName;
                            }
                            logger.info("download file:" + fullUrl);
                            IOHelper.downFileFromUrl(fullUrl, ProjectEnvironment.resourcePath() + appBinName);
                            app = new File(ProjectEnvironment.resourcePath(), appBinName);
                        }
                    } else {
                        app = new File(ProjectEnvironment.resourcePath(), PropConfig.getAppBin());
                    }

                    Adb adb;
                    if (PropConfig.getDeviceName() != null && isFirstLaunch) {
                        adb = new Adb(PropConfig.getDeviceName());
                        adb.restartEmulator();
                        isFirstLaunch = false;
                    } else {
                        adb = new Adb();
                    }
                    capabilities = new DesiredCapabilities();
                    capabilities.setCapability("deviceName", adb.getDeviceName());
                    capabilities.setCapability("app", app.getAbsolutePath());
                    driverObject = ThreadGuard.protect(new AndroidDriver<>(new URL(appiumServerUrl), capabilities));
                    logger.info("Using Android Driver...");
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
        FIREFOX, /**
         * Ie selected browser.
         */
        IE, /**
         * Safari selected browser.
         */
        SAFARI, /**
         * Opera selected browser.
         */
        OPERA, /**
         * Googlechrome selected browser.
         */
        GOOGLECHROME, /**
         * Androidapp selected browser.
         */
        ANDROIDAPP, /**
         * Iosapp selected browser.
         */
        IOSAPP, /**
         * Htmlunit selected browser.
         */
        HTMLUNIT
    }

    private static enum htmlUnitEmulation {

        /**
         * None html unit emulation.
         */
        NONE, /**
         * Firefox html unit emulation.
         */
        FIREFOX, /**
         * Ie 6 html unit emulation.
         */
        IE6, /**
         * Ie 7 html unit emulation.
         */
        IE7, /**
         * Ie 8 html unit emulation.
         */
        IE8
    }

}
