package com.qa.framework.config;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The type Prop config.
 */
@Component
public class PropConfig implements InitializingBean {

    private static PropConfig instance;

    @Override
    public void afterPropertiesSet() throws Exception {
        instance = this;
    }

    public static PropConfig get() {
        return instance;
    }

    //代理配置
    @Value("${localhost}")
    private String localhost = "127.0.0.1";
    @Value("${localport}")
    private String localport = "8888";
    @Value("${timeout}")
    private String timeout = "30000";
    //测试服务器配置
    @Value("${webPath}")
    private String webPath;
    @Value("${dbPoolName}")
    private String dbPoolName;
    //失败重试次数
    @Value("${retryCount}")
    private int retryCount = 1;
    //自定义report
    @Value("${sourceCodeEncoding}")
    private String sourceCodeEncoding = "UTF-8";
    @Value("${sourceCodeDir}")
    private String sourceCodeDir = "src";
    //浏览器配置
    @Value("${coreType}")
    private String coreType;
    @Value("${htmlUnitEmulationType}")
    private String htmlUnitEmulationType;
    @Value("${htmlUnitProxy}")
    private String htmlUnitProxy;
    //base package name
    @Value("${basePackage}")
    private String basePackage;
    //Android
    @Value("${appBin}")
    private String appBin;
    @Value("${appiumServerUrl}")
    private String appiumServerUrl;
    @Value("${deviceName}")
    private String deviceName;
    //OCR
    @Value("${tessPath}")
    private String tessPath;
    //image compare
    @Value("${benchmarkImagePath}")
    private String benchmarkImagePath;
    @Value("${actualImagePath}")
    private String actualImagePath;
    @Value("${diffImagePath}")
    private String diffImagePath;
    @Value("${maxColorThreshold}")
    private int maxColorThreshold;
    //Highlight element for android
    @Value("${highlight}")
    private boolean highlight = false;
    //Load chrome extensions or not
    @Value("${debug}")
    private boolean debug = false;
    //ios
    @Value("${plantfromVersion}")
    private String plantfromVersion;
    @Value("${uuid}")
    private String uuid;
    //SMS配置
    @Value("${sendMsg}")
    private boolean sendMsg = false;
    @Value("${SN}")
    private String SN;
    @Value("${SNPWD}")
    private String SNPWD;
    //单例测试
    @Value("${noReset}")
    private boolean noReset;

    public String getLocalhost() {
        return localhost;
    }

    public void setLocalhost(String localhost) {
        this.localhost = localhost;
    }

    public String getLocalport() {
        return localport;
    }

    public void setLocalport(String localport) {
        this.localport = localport;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getWebPath() {
        return webPath;
    }

    public void setWebPath(String webPath) {
        this.webPath = webPath;
    }

    public String getDbPoolName() {
        return dbPoolName;
    }

    public void setDbPoolName(String dbPoolName) {
        this.dbPoolName = dbPoolName;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public String getSourceCodeEncoding() {
        return sourceCodeEncoding;
    }

    public void setSourceCodeEncoding(String sourceCodeEncoding) {
        this.sourceCodeEncoding = sourceCodeEncoding;
    }

    public String getSourceCodeDir() {
        return sourceCodeDir;
    }

    public void setSourceCodeDir(String sourceCodeDir) {
        this.sourceCodeDir = sourceCodeDir;
    }

    public String getCoreType() {
        return coreType;
    }

    public void setCoreType(String coreType) {
        this.coreType = coreType;
    }

    public String getHtmlUnitEmulationType() {
        return htmlUnitEmulationType;
    }

    public void setHtmlUnitEmulationType(String htmlUnitEmulationType) {
        this.htmlUnitEmulationType = htmlUnitEmulationType;
    }

    public String getHtmlUnitProxy() {
        return htmlUnitProxy;
    }

    public void setHtmlUnitProxy(String htmlUnitProxy) {
        this.htmlUnitProxy = htmlUnitProxy;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getAppBin() {
        return appBin;
    }

    public void setAppBin(String appBin) {
        this.appBin = appBin;
    }

    public String getAppiumServerUrl() {
        return appiumServerUrl;
    }

    public void setAppiumServerUrl(String appiumServerUrl) {
        this.appiumServerUrl = appiumServerUrl;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getTessPath() {
        return tessPath;
    }

    public void setTessPath(String tessPath) {
        this.tessPath = tessPath;
    }

    public String getBenchmarkImagePath() {
        return benchmarkImagePath;
    }

    public void setBenchmarkImagePath(String benchmarkImagePath) {
        this.benchmarkImagePath = benchmarkImagePath;
    }

    public String getActualImagePath() {
        return actualImagePath;
    }

    public void setActualImagePath(String actualImagePath) {
        this.actualImagePath = actualImagePath;
    }

    public String getDiffImagePath() {
        return diffImagePath;
    }

    public void setDiffImagePath(String diffImagePath) {
        this.diffImagePath = diffImagePath;
    }

    public int getMaxColorThreshold() {
        return maxColorThreshold;
    }

    public void setMaxColorThreshold(int maxColorThreshold) {
        this.maxColorThreshold = maxColorThreshold;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getPlantfromVersion() {
        return plantfromVersion;
    }

    public void setPlantfromVersion(String plantfromVersion) {
        this.plantfromVersion = plantfromVersion;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isSendMsg() {
        return sendMsg;
    }

    public void setSendMsg(boolean sendMsg) {
        this.sendMsg = sendMsg;
    }

    public String getSN() {
        return SN;
    }

    public void setSN(String SN) {
        this.SN = SN;
    }

    public String getSNPWD() {
        return SNPWD;
    }

    public void setSNPWD(String SNPWD) {
        this.SNPWD = SNPWD;
    }

    public boolean isNoReset() {
        return noReset;
    }

    public void setNoReset(boolean noReset) {
        this.noReset = noReset;
    }


}
