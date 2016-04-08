package com.qa.framework.config;


import com.qa.framework.library.reflect.ReflectHelper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Created by apple on 15/11/18.
 */
public class PropConfig {
    private static Properties props = new Properties();
    //代理配置
    private static boolean useProxy = false;
    private static String localhost;
    private static String localport;
    private static String timeout;
    //测试服务器配置
    private static String webPath;
    private static String dbPoolName;
    //失败重试次数
    private static int retryCount;
    //自定义report
    private static String sourceCodeEncoding;
    private static String sourceCodeDir;
    //浏览器配置
    private static String coreType;
    private static String htmlUnitEmulationType;
    private static String htmlUnitProxy;

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

    public static String getDbPoolName() {
        return dbPoolName;
    }

    public static void setDbPoolName(String dbPoolName) {
        PropConfig.dbPoolName = dbPoolName;
    }

    public static String getAppBin() {
        return appBin;
    }

    public static void setAppBin(String appBin) {
        PropConfig.appBin = appBin;
    }

    public static String getDeviceName() {
        return deviceName;
    }

    public static void setDeviceName(String deviceName) {
        PropConfig.deviceName = deviceName;
    }

    public static String getAppiumServerUrl() {
        return appiumServerUrl;
    }

    public static void setAppiumServerUrl(String appiumServerUrl) {
        PropConfig.appiumServerUrl = appiumServerUrl;
    }

    public static String getCoreType() {
        return coreType;
    }

    public static void setCoreType(String val) {
        coreType = val;
    }

    public static String getHtmlUnitProxy() {
        return htmlUnitProxy;
    }

    public static void setHtmlUnitProxy(String val) {
        htmlUnitProxy = val;
    }

    public static String getHtmlUnitEmulationType() {
        return htmlUnitEmulationType;
    }

    public static void setHtmlUnitEmulationType(String val) {
        htmlUnitEmulationType = val;
    }

    public static String getWebPath() {
        return webPath;
    }

    public static void setWebPath(String val) {
        webPath = val;
    }

    public static boolean isUseProxy() {
        return useProxy;
    }

    public static void setUseProxy(String val) {
        if ("false".equalsIgnoreCase(val)) {
            useProxy = false;
        }
        useProxy = true;
    }

    public static String getLocalhost() {
        return localhost;
    }

    public static void setLocalhost(String val) {
        localhost = val;
    }

    public static String getLocalport() {
        return localport;
    }

    public static void setLocalport(String val) {
        localport = val;
    }

    public static String getTimeout() {
        return timeout;
    }

    public static void setTimeout(String val) {
        timeout = val;
    }

    public static int getRetryCount() {
        return retryCount;
    }

    public static void setRetryCount(String val) {
        retryCount = Integer.parseInt(val);
    }

    public static String getSourceCodeEncoding() {
        return sourceCodeEncoding;
    }

    public static void setSourceCodeEncoding(String val) {
        sourceCodeEncoding = val;
    }

    public static String getSourceCodeDir() {
        return sourceCodeDir;
    }

    public static void setSourceCodeDir(String val) {
        sourceCodeDir = val;
    }


    public static String getTessPath() {
        return tessPath;
    }

    public static void setTessPath(String val) {
        tessPath = val;
    }

    public static String getBenchmarkImagePath() {
        return benchmarkImagePath;
    }

    public static void setBenchmarkImagePath(String val) {
        benchmarkImagePath = benchmarkImagePath;
    }

    public static String getActualImagePath() {
        return actualImagePath;
    }

    public static void setActualImagePath(String val) {
        actualImagePath = val;
    }

    public static String getDiffImagePath() {
        return diffImagePath;
    }

    public static void setDiffImagePath(String val) {
        diffImagePath = val;
    }

    public static int getMaxColorThreshold() {
        return maxColorThreshold;
    }

    public static void setMaxColorThreshold(String val) {
        maxColorThreshold = Integer.parseInt(val);
    }

}
