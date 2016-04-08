package com.qa.framework.config;

import java.io.File;
import java.util.Properties;

public class ProjectEnvironment {

    private static final String SRC = "src";

    private static String basePath;

    static {
        basePath = System.getProperty("user.dir") + File.separator;
    }

    public static String resourcePath() {
        return basePath + "res" + File.separator;
    }

    public static String srcPath() {
        return basePath + SRC + File.separator;
    }

    public static String libPath() {
        return resourcePath() + "lib" + File.separator;
    }

    public static String configPath() {
        return basePath + "config" + File.separator;
    }

    public static String dbConfigFile() {
        return configPath() + "DBConfig.xml";
    }

    public static String adminConfigFile() {
        return configPath() + "AdminConfig.xml";
    }

    public static String dataFilesPath() {
        return basePath + "dataFiles" + File.separator;
    }

    public static String testSuitesPath() {
        return dataFilesPath() + "testSuites" + File.separator;
    }

    public static String testCasesPath() {
        return dataFilesPath() + "testCases" + File.separator;
    }

    public static String reportsPath() {
        return basePath + "reports" + File.separator;
    }

    public static String uiObjectsMapPath() {
        return basePath + "uimaps" + File.separator;
    }

    public static String reportsLinkToFilePath() {
        return reportsPath() + "_filepath" + File.separator;
    }

    public static String reportsScreenshotPath() {
        return reportsPath() + "_Screenshots" + File.separator;
    }

    public static String autoItXFile() {
        return libPath() + "AutoItX3" + File.separator + "AutoItX3.dll";
    }

    public static String autoItX64File() {
        return libPath() + "AutoItX3" + File.separator + "AutoItX3_x64.dll";
    }

    public static String pwMatrixPath() {
        return libPath() + "PWMatrix";
    }

    public static String ftpConfigFile() {
        return configPath() + "FTPConfig.xml";
    }

    public static String getChromeDriverLocation() {
        Properties sysProp = System.getProperties();
        String os = sysProp.getProperty("os.name");
        if (os.startsWith("Win")) {
            return resourcePath() + "chromedriver" + File.separator + "chromedriver_for_win.exe";
        } else {
            return resourcePath() + "chromedriver" + File.separator + "chromedriver";
        }
    }

    public static String getIEDriverLocation() {
        Properties sysProp = System.getProperties();
        String arch = sysProp.getProperty("os.arch");
        if (arch.contains("64")) {
            return resourcePath() + "IEDriver" + File.separator + "64" + File.separator + "IEDriverServer.exe";
        }
        return resourcePath() + "IEDriver" + File.separator + "32" + File.separator + "IEDriverServer.exe";
    }

}
