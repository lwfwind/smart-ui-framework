package com.qa.framework.library.pwmatrix;

import com.qa.framework.config.ProjectEnvironment;

import java.io.File;

/**
 * The interface Pw matrix constants.
 */
public interface PWMatrixConstants {

    /**
     * The constant PW_EXE_PATH.
     */
    public static final String PW_EXE_PATH = ProjectEnvironment.pwMatrixPath();
    /**
     * The constant PW_ECHO_EXE.
     */
    public static final String PW_ECHO_EXE = PW_EXE_PATH + File.separator
            + "PwEcho.exe";
    /**
     * The constant PW_CREATE_EXE.
     */
    public static final String PW_CREATE_EXE = PW_EXE_PATH + File.separator
            + "PwCreate.exe";
    /**
     * The constant PW_SOURCE_NAME.
     */
    public static final String PW_SOURCE_NAME = "PW_SOURCE";
    /**
     * The constant PW_ENCRYPT_NAME.
     */
    public static final String PW_ENCRYPT_NAME = "ENCMATRIX";
    /**
     * The constant PW_KEYS_NAME.
     */
    public static final String PW_KEYS_NAME = "Keys";
    /**
     * The constant PW_ENV_MATRIX.
     */
    public static final String PW_ENV_MATRIX = "PW_MATRIX";

}
