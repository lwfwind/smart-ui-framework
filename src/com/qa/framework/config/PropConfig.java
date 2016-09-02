package com.qa.framework.config;


import com.qa.framework.library.base.ReflectHelper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * The type Prop config.
 */
public class PropConfig {
    private static Properties props = new Properties();
    //代理配置
    private static boolean useProxy = false;
    private static String localhost = "127.0.0.1";
    private static String localport = "8888";
    private static String timeout = "10000";
    //测试服务器配置
    private static String webPath;
    private static String dbPoolName;
    //失败重试次数
    private static int retryCount = 1;
    //自定义report
    private static String sourceCodeEncoding = "UTF-8";
    private static String sourceCodeDir = "src";
    //浏览器配置
    private static String coreType;
    private static String htmlUnitEmulationType;
    private static String htmlUnitProxy;

    //base package name
    private static String basePackage;

    //Android
    private static String appBin;
    private static String appiumServerUrl;
    private static String deviceName;
    //OCR
    private static String tessPath;
    //image compare
    private static String benchmarkImagePath;
    private static String actualImagePath;
    private static String diffImagePath;
    private static int maxColorThreshold;

    //Highlight element for android
    private static boolean highlight = false;

    //Load chrome extensions or not
    private static boolean debug = false;

    static {
        File file = new File(System.getProperty("user.dir") + File.separator + "config.properties");
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
            props.load(fileReader);
            Field[] fields = PropConfig.class.getDeclaredFields();
            for (Field field : fields) {
                if (!field.getName().equals("props") && props.getProperty(field.getName()) != null) {
                    ReflectHelper.setMethod(PropConfig.class, field.getName(), props.getProperty(field.getName()), String.class);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

}
