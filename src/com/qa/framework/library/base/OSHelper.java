package com.qa.framework.library.base;

import org.apache.log4j.Logger;

/**
 * The type Os helper.
 */
public class OSHelper {
    private static Logger logger = Logger.getLogger(OSHelper.class);
    private static String os = System.getProperty("os.name").toLowerCase();

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        logger.info(isWindows());
        logger.info(isMac());
        logger.info(isUnix());
    }

    /**
     * Is windows boolean.
     *
     * @return the boolean
     */
    public static boolean isWindows() {
        return os.contains("win");
    }

    /**
     * Is mac boolean.
     *
     * @return the boolean
     */
    public static boolean isMac() {
        return os.contains("mac");
    }

    /**
     * Is unix boolean.
     *
     * @return the boolean
     */
    public static boolean isUnix() {
        return (os.contains("nix")) || (os.contains("nux")) || (os.indexOf("aix") > 0);
    }
}
