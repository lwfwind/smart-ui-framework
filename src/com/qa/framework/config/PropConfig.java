package com.qa.framework.config;


import com.library.common.IOHelper;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

/**
 * The type Prop config.
 */
public class PropConfig {
    private final static Logger logger = Logger
            .getLogger(PropConfig.class);
    //代理配置
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

    //SMS配置
    @Value("sendMsg")
    private static boolean sendMsg = false;
    @Value("SN")
    private static String SN;
    @Value("SNPWD")
    private static String SNPWD;

    //单例测试
    @Value("noReset")
    private static boolean noReset;


    private static PropConfig propConfig;

    static {
        PropConfig prop = new PropConfig();
    }

    private PropConfig() {
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

    public static String getSN() {
        return SN;
    }

    public static void setSN(String SN) {
        PropConfig.SN = SN;
    }

    public static String getSNPWD() {
        return SNPWD;
    }

    public static void setSNPWD(String SNPWD) {
        PropConfig.SNPWD = SNPWD;
    }

    public static boolean isSendMsg() {
        return sendMsg;
    }

    public static void setSendMsg(boolean sendMsg) {
        PropConfig.sendMsg = sendMsg;
    }

    public static boolean isNoReset() {
        return noReset;
    }

    public static void setNoReset(boolean noReset) {
        PropConfig.noReset = noReset;
    }

    private static void initConfigFields(Object obj) {
        Properties props = new Properties();
        Class<?> clazz = obj.getClass();
        props = getProperties();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldKey;
            String fieldValue;
            if (field.isAnnotationPresent(Value.class)) {
                Value value = field.getAnnotation(Value.class);
                fieldKey = value.value();
                if (!field.getName().equals("props") && props.getProperty(fieldKey) != null) {
                    fieldValue = props.getProperty(fieldKey);
                    field.setAccessible(true);
                    setValue(obj, field, fieldValue);
                }
            }
        }
    }

    private static void setValue(Object obj, Field method, String value) {
        Object fieldType = method.getType();
        try {
            if (String.class.equals(fieldType)) {
                method.set(obj, value);
            } else if (byte.class.equals(fieldType)) {
                method.set(obj, Byte.parseByte(value));
            } else if (Byte.class.equals(fieldType)) {
                method.set(obj, Byte.valueOf(value));
            } else if (boolean.class.equals(fieldType)) {
                method.set(obj, Boolean.parseBoolean(value));
            } else if (Boolean.class.equals(fieldType)) {
                method.set(obj, Boolean.valueOf(value));
            } else if (short.class.equals(fieldType)) {
                method.set(obj, Short.parseShort(value));
            } else if (Short.class.equals(fieldType)) {
                method.set(obj, Short.valueOf(value));
            } else if (int.class.equals(fieldType)) {
                method.set(obj, Integer.parseInt(value));
            } else if (Integer.class.equals(fieldType)) {
                method.set(obj, Integer.valueOf(value));
            } else if (long.class.equals(fieldType)) {
                method.set(obj, Long.parseLong(value));
            } else if (Long.class.equals(fieldType)) {
                method.set(obj, Long.valueOf(value));
            } else if (float.class.equals(fieldType)) {
                method.set(obj, Float.parseFloat(value));
            } else if (Float.class.equals(fieldType)) {
                method.set(obj, Float.valueOf(value));
            } else if (double.class.equals(fieldType)) {
                method.set(obj, Double.parseDouble(value));
            } else if (Double.class.equals(fieldType)) {
                method.set(obj, Double.valueOf(value));
            }
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
    }


    private static Properties getProperties() {
        Properties props = new Properties();
        List<String> configPathList = IOHelper.listFilesInDirectory(System.getProperty("user.dir"),"config.properties");
        if(configPathList.size() > 0) {
            File file = new File(configPathList.get(0));
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(file);
                props.load(fileReader);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            } finally {
                try {
                    if (fileReader != null) {
                        fileReader.close();
                    }
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            return props;
        }
        return null;
    }
}
