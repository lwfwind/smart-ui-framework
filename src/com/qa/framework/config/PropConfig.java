package com.qa.framework.config;


import com.library.common.ReflectHelper;
import com.qa.framework.ioc.annotation.Value;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

import static com.qa.framework.ioc.ValueHelp.initConfigFields;

/**
 * The type Prop config.
 */
public class PropConfig {
    //代理配置
    @Value("useProxy")
    private static boolean useProxy = false;
    @Value("localhost")
    private static String localhost = "127.0.0.1";
    @Value("localport")
    private static String localport = "8888";
    @Value("timeout")
    private static String timeout = "30000";
    //测试服务器配置
    @Value("webPath")
    private static String webPath;
    @Value("dbPoolName")
    private static String dbPoolName;
    //失败重试次数
    @Value("retryCount")
    private static int retryCount = 1;
    //自定义report
    @Value("sourceCodeEncoding")
    private static String sourceCodeEncoding = "UTF-8";
    @Value("sourceCodeDir")
    private static String sourceCodeDir = "src";
    //浏览器配置
    @Value("coreType")
    private static String coreType;
    @Value("htmlUnitEmulationType")
    private static String htmlUnitEmulationType;
    @Value("htmlUnitProxy")
    private static String htmlUnitProxy;

    //base package name
    @Value("basePackage")
    private static String basePackage;

    //Android
    @Value("appBin")
    private static String appBin;
    @Value("appiumServerUrl")
    private static String appiumServerUrl;
    @Value("deviceName")
    private static String deviceName;
    //OCR
    @Value("tessPath")
    private static String tessPath;
    //image compare
    @Value("benchmarkImagePath")
    private static String benchmarkImagePath;
    @Value("actualImagePath")
    private static String actualImagePath;
    @Value("diffImagePath")
    private static String diffImagePath;
    @Value("maxColorThreshold")
    private static int maxColorThreshold;

    //Highlight element for android
    @Value("highlight")
    private static boolean highlight = false;

    //Load chrome extensions or not
    @Value("debug")
    private static boolean debug = false;

    //ios
    @Value("plantfromVersion")
    private static String plantfromVersion;
    @Value("uuid")
    private static String uuid;


    private static PropConfig propConfig;
    private PropConfig (){
        initConfigFields(this);
    }
    public static PropConfig getInstance() {
        if (propConfig == null) {
            synchronized (PropConfig.class) {
                propConfig = new PropConfig();
            }
        }
        return propConfig;
    }
    static {
        PropConfig prop=new PropConfig();
    }
    /**
     * Is debug boolean.
     *
     * @return the boolean
     */
    public static boolean isDebug() {
        return debug;
    }

    /**
     * Sets debug.
     *
     * @param val the val
     */
    public static void setDebug(String val) {
        debug = !"false".equalsIgnoreCase(val);
    }

    /**
     * Is highlight boolean.
     *
     * @return the boolean
     */
    public static boolean isHighlight() {
        return highlight;
    }

    /**
     * Sets highlight.
     *
     * @param val the val
     */
    public static void setHighlight(String val) {
        highlight = !"false".equalsIgnoreCase(val);
    }

    /**
     * Gets base package.
     *
     * @return the base package
     */
    public static String getBasePackage() {
        return basePackage;
    }

    /**
     * Sets base package.
     *
     * @param basePackage the base package
     */
    public static void setBasePackage(String basePackage) {
        PropConfig.basePackage = basePackage;
    }

    /**
     * Gets db pool name.
     *
     * @return the db pool name
     */
    public static String getDbPoolName() {
        return dbPoolName;
    }

    /**
     * Sets db pool name.
     *
     * @param dbPoolName the db pool name
     */
    public static void setDbPoolName(String dbPoolName) {
        PropConfig.dbPoolName = dbPoolName;
    }

    /**
     * Gets app bin.
     *
     * @return the app bin
     */
    public static String getAppBin() {
        return appBin;
    }

    /**
     * Sets app bin.
     *
     * @param appBin the app bin
     */
    public static void setAppBin(String appBin) {
        PropConfig.appBin = appBin;
    }

    /**
     * Gets device name.
     *
     * @return the device name
     */
    public static String getDeviceName() {
        return deviceName;
    }

    /**
     * Sets device name.
     *
     * @param deviceName the device name
     */
    public static void setDeviceName(String deviceName) {
        PropConfig.deviceName = deviceName;
    }

    /**
     * Gets appium server url.
     *
     * @return the appium server url
     */
    public static String getAppiumServerUrl() {
        return appiumServerUrl;
    }

    /**
     * Sets appium server url.
     *
     * @param appiumServerUrl the appium server url
     */
    public static void setAppiumServerUrl(String appiumServerUrl) {
        PropConfig.appiumServerUrl = appiumServerUrl;
    }

    /**
     * Gets core type.
     *
     * @return the core type
     */
    public static String getCoreType() {
        return coreType;
    }

    /**
     * Sets core type.
     *
     * @param val the val
     */
    public static void setCoreType(String val) {
        coreType = val;
    }

    /**
     * Gets html unit proxy.
     *
     * @return the html unit proxy
     */
    public static String getHtmlUnitProxy() {
        return htmlUnitProxy;
    }

    /**
     * Sets html unit proxy.
     *
     * @param val the val
     */
    public static void setHtmlUnitProxy(String val) {
        htmlUnitProxy = val;
    }

    /**
     * Gets html unit emulation type.
     *
     * @return the html unit emulation type
     */
    public static String getHtmlUnitEmulationType() {
        return htmlUnitEmulationType;
    }

    /**
     * Sets html unit emulation type.
     *
     * @param val the val
     */
    public static void setHtmlUnitEmulationType(String val) {
        htmlUnitEmulationType = val;
    }

    /**
     * Gets web path.
     *
     * @return the web path
     */
    public static String getWebPath() {
        return webPath;
    }

    /**
     * Sets web path.
     *
     * @param val the val
     */
    public static void setWebPath(String val) {
        webPath = val;
    }

    /**
     * Is use proxy boolean.
     *
     * @return the boolean
     */
    public static boolean isUseProxy() {
        return useProxy;
    }

    /**
     * Sets use proxy.
     *
     * @param val the val
     */
    public static void setUseProxy(String val) {
        if ("false".equalsIgnoreCase(val)) {
            useProxy = false;
        } else {
            useProxy = true;
        }
    }

    /**
     * Gets localhost.
     *
     * @return the localhost
     */
    public static String getLocalhost() {
        return localhost;
    }

    /**
     * Sets localhost.
     *
     * @param val the val
     */
    public static void setLocalhost(String val) {
        localhost = val;
    }

    /**
     * Gets localport.
     *
     * @return the localport
     */
    public static String getLocalport() {
        return localport;
    }

    /**
     * Sets localport.
     *
     * @param val the val
     */
    public static void setLocalport(String val) {
        localport = val;
    }

    /**
     * Gets timeout.
     *
     * @return the timeout
     */
    public static String getTimeout() {
        return timeout;
    }

    /**
     * Sets timeout.
     *
     * @param val the val
     */
    public static void setTimeout(String val) {
        timeout = val;
    }

    /**
     * Gets retry count.
     *
     * @return the retry count
     */
    public static int getRetryCount() {
        return retryCount;
    }

    /**
     * Sets retry count.
     *
     * @param val the val
     */
    public static void setRetryCount(String val) {
        retryCount = Integer.parseInt(val);
    }

    /**
     * Gets source code encoding.
     *
     * @return the source code encoding
     */
    public static String getSourceCodeEncoding() {
        return sourceCodeEncoding;
    }

    /**
     * Sets source code encoding.
     *
     * @param val the val
     */
    public static void setSourceCodeEncoding(String val) {
        sourceCodeEncoding = val;
    }

    /**
     * Gets source code dir.
     *
     * @return the source code dir
     */
    public static String getSourceCodeDir() {
        return sourceCodeDir;
    }

    /**
     * Sets source code dir.
     *
     * @param val the val
     */
    public static void setSourceCodeDir(String val) {
        sourceCodeDir = val;
    }


    /**
     * Gets tess path.
     *
     * @return the tess path
     */
    public static String getTessPath() {
        return tessPath;
    }

    /**
     * Sets tess path.
     *
     * @param val the val
     */
    public static void setTessPath(String val) {
        tessPath = val;
    }

    /**
     * Gets benchmark image path.
     *
     * @return the benchmark image path
     */
    public static String getBenchmarkImagePath() {
        return benchmarkImagePath;
    }

    /**
     * Sets benchmark image path.
     *
     * @param val the val
     */
    public static void setBenchmarkImagePath(String val) {
        benchmarkImagePath = benchmarkImagePath;
    }

    /**
     * Gets actual image path.
     *
     * @return the actual image path
     */
    public static String getActualImagePath() {
        return actualImagePath;
    }

    /**
     * Sets actual image path.
     *
     * @param val the val
     */
    public static void setActualImagePath(String val) {
        actualImagePath = val;
    }

    /**
     * Gets diff image path.
     *
     * @return the diff image path
     */
    public static String getDiffImagePath() {
        return diffImagePath;
    }

    /**
     * Sets diff image path.
     *
     * @param val the val
     */
    public static void setDiffImagePath(String val) {
        diffImagePath = val;
    }

    /**
     * Gets max color threshold.
     *
     * @return the max color threshold
     */
    public static int getMaxColorThreshold() {
        return maxColorThreshold;
    }

    /**
     * Sets max color threshold.
     *
     * @param val the val
     */
    public static void setMaxColorThreshold(String val) {
        maxColorThreshold = Integer.parseInt(val);
    }

    public static String getUuid() {
        return uuid;
    }

    public static void setUuid(String val) {
        uuid = val;
    }

    public static String getPlantfromVersion() {
        return plantfromVersion;
    }

    public static void setPlantfromVersion(String val) {
        plantfromVersion = val;
    }

}
