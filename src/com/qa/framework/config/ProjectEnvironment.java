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

    public static String getGeckoDriverLocation() {
        Properties sysProp = System.getProperties();
        String osName = sysProp.getProperty("os.name");
        String osArch = sysProp.getProperty("os.arch");
        if (osName.startsWith("Win") && osArch.contains("64")) {
            return resourcePath() + "geckodriver" + File.separator + "geckodriver_win64.exe";
        } else if (osName.startsWith("Win") && !osArch.contains("64")) {
            return resourcePath() + "geckodriver" + File.separator + "geckodriver_win32.exe";
        } else {
            return resourcePath() + "geckodriver" + File.separator + "geckodriver";
        }
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
     * Gets chrome extensions location.
     *
     * @return the chrome extensions location
     */
    public static String getChromeExtensionsLocation() {
        return resourcePath() + "chromextensions";
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
