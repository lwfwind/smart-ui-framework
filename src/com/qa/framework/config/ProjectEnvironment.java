package com.qa.framework.config;

import java.io.File;
import java.util.Properties;

/**
 * The type Project environment.
 */
public class ProjectEnvironment {

    private static final String SRC = "src";

    private static String basePath;

    static {
        basePath = System.getProperty("user.dir") + File.separator;
    }

    /**
     * Resource path string.
     *
     * @return the string
     */
    public static String resourcePath() {
        return basePath + "res" + File.separator;
    }

    /**
     * Src path string.
     *
     * @return the string
     */
    public static String srcPath() {
        return basePath + SRC + File.separator;
    }

    /**
     * Lib path string.
     *
     * @return the string
     */
    public static String libPath() {
        return resourcePath() + "lib" + File.separator;
    }

    /**
     * Config path string.
     *
     * @return the string
     */
    public static String configPath() {
        return basePath + "config" + File.separator;
    }

    /**
     * Db config file string.
     *
     * @return the string
     */
    public static String dbConfigFile() {
        return configPath() + "DBConfig.xml";
    }

    /**
     * Admin config file string.
     *
     * @return the string
     */
    public static String adminConfigFile() {
        return configPath() + "AdminConfig.xml";
    }

    /**
     * Data files path string.
     *
     * @return the string
     */
    public static String dataFilesPath() {
        return basePath + "dataFiles" + File.separator;
    }

    /**
     * Test suites path string.
     *
     * @return the string
     */
    public static String testSuitesPath() {
        return dataFilesPath() + "testSuites" + File.separator;
    }

    /**
     * Test cases path string.
     *
     * @return the string
     */
    public static String testCasesPath() {
        return dataFilesPath() + "testCases" + File.separator;
    }

    /**
     * Reports path string.
     *
     * @return the string
     */
    public static String reportsPath() {
        return basePath + "reports" + File.separator;
    }

    /**
     * Ui objects map path string.
     *
     * @return the string
     */
    public static String uiObjectsMapPath() {
        return basePath + "uimaps" + File.separator;
    }

    /**
     * Reports link to file path string.
     *
     * @return the string
     */
    public static String reportsLinkToFilePath() {
        return reportsPath() + "_filepath" + File.separator;
    }

    /**
     * Reports screenshot path string.
     *
     * @return the string
     */
    public static String reportsScreenshotPath() {
        return reportsPath() + "_Screenshots" + File.separator;
    }

    /**
     * Auto it x file string.
     *
     * @return the string
     */
    public static String autoItXFile() {
        return libPath() + "AutoItX3" + File.separator + "AutoItX3.dll";
    }

    /**
     * Auto it x 64 file string.
     *
     * @return the string
     */
    public static String autoItX64File() {
        return libPath() + "AutoItX3" + File.separator + "AutoItX3_x64.dll";
    }

    /**
     * Pw matrix path string.
     *
     * @return the string
     */
    public static String pwMatrixPath() {
        return libPath() + "PWMatrix";
    }

    /**
     * Ftp config file string.
     *
     * @return the string
     */
    public static String ftpConfigFile() {
        return configPath() + "FTPConfig.xml";
    }

    /**
     * Gets chrome driver location.
     *
     * @return the chrome driver location
     */
    public static String getChromeDriverLocation() {
        Properties sysProp = System.getProperties();
        String os = sysProp.getProperty("os.name");
        if (os.startsWith("Win")) {
            return resourcePath() + "chromedriver" + File.separator + "chromedriver_for_win.exe";
        } else {
            return resourcePath() + "chromedriver" + File.separator + "chromedriver";
        }
    }

    /**
     * Gets ie driver location.
     *
     * @return the ie driver location
     */
    public static String getIEDriverLocation() {
        Properties sysProp = System.getProperties();
        String arch = sysProp.getProperty("os.arch");
        if (arch.contains("64")) {
            return resourcePath() + "IEDriver" + File.separator + "64" + File.separator + "IEDriverServer.exe";
        }
        return resourcePath() + "IEDriver" + File.separator + "32" + File.separator + "IEDriverServer.exe";
    }

}
