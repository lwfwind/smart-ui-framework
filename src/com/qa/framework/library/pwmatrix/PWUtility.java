package com.qa.framework.library.pwmatrix;

import java.io.File;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;
import java.util.Map;

/**
 * The type Pw utility.
 */
public class PWUtility implements PWMatrixConstants {

    private static PWMatrixEnvironment pwEnv = new PWMatrixEnvironment();

    /**
     * setPassword Description: This static method to set the password based on
     * the key and value
     *
     * @param key      It is the key, it is the userId and ServerName concatenation                 by semicolon, for example: userID:abc, ServerName:testPage,                 then the key is: abc:testPage
     * @param password It is the value                 <p/>                 <table border=1 CELLPADDING="3" CELLSPACING="0">                 <tr BGCOLOR="#CCCCFF">                 <td>Date</td>                 <td>Author</td>                 <td>Details</td>                 </tr>                 <tr>                 <td>04/12/2012</td>                 <td>Weifeng Lu</td>                 <td>Initial Version</td>                 </tr>                 <br>                 </table>
     */
    public static synchronized void setPassword(String key, String password) {
        PWCreateService pwCreateSer = null;
        PWUpdateService pwUpdateSer = null;
        String userId = verifyInput(key);
        // Each user will have it's own password and key files under the folder
        // named by userId, by doing so,
        // If one user failed to create the password file, it won't impact other
        // users.
        pwEnv.setPath(userId);
        File userPath = new File(pwEnv.getPW_FILE_PATH());
        if (!userPath.exists()) {
            userPath.mkdir();
        }

        // Very first time to see .PWMATRIX file
        if (!checkPWMatrix(pwEnv.getENCRYPT_FILE_PATH())) {
            pwCreateSer = new PWCreateService();
            pwCreateSer.generateSourceFile(key, password,
                    pwEnv.getSOURCE_FILE_PATH());
            // Create .PWMATRIX file from the source file
            pwCreateSer.pwCreate(pwEnv.getSOURCE_FILE_RELATIVE_PATH(),
                    pwEnv.getENCRYPT_FILE_RELATIVE_PATH());
            // Once .PWMATRIX file created delete source file
            pwCreateSer.deleteSrcFile(pwEnv.getSOURCE_FILE_PATH());
            // Create keys file for the key passed
            pwCreateSer.createKeysFile(key, pwEnv.getKEY_FILE());
        } else {
            pwCreateSer = new PWCreateService();
            pwUpdateSer = new PWUpdateService();
            // Read keys file and generate the key list
            List<String> keys = pwUpdateSer.readKeysFile(pwEnv.getKEY_FILE());
            // With key list get the value(pwd�s) from .PWMATRIX for each key
            // and form the Map
            Map<String, String> CredMap = pwUpdateSer.formCredentials(keys);
            // update the connectionPoolMap formed from above with passed key/value passed
            // If key found in connectionPoolMap update the value else set the new entry with
            // this key and value
            keys = pwUpdateSer.updateCredMap(key, password, CredMap);
            // After updating the connectionPoolMap generate the source file from this connectionPoolMap
            // used for generating .PWMATRIX file
            pwUpdateSer
                    .generateSourceFile(CredMap, pwEnv.getSOURCE_FILE_PATH());
            // create .PWMATRIX file from the source file
            pwCreateSer.pwCreate(pwEnv.getSOURCE_FILE_RELATIVE_PATH(),
                    pwEnv.getENCRYPT_FILE_RELATIVE_PATH());
            // Once .PWMATRIX file created delete source file
            pwCreateSer.deleteSrcFile(pwEnv.getSOURCE_FILE_PATH());
            // Create keys file from the key list
            pwCreateSer.createKeysFile(keys, pwEnv.getKEY_FILE());
        }
    }

    /**
     * getPassword Description: This static method get the password based on the
     * server and userId
     *
     * @param server It is the serverName,it is the one used to set the password.
     * @param userId It is the userId.
     * @return String it returns the password. <p/> <table border=1 CELLPADDING="3" CELLSPACING="0"> <tr BGCOLOR="#CCCCFF"> <td>Date</td> <td>Author</td> <td>Details</td> </tr> <tr> <td>04/12/2012</td> <td>Weifeng Lu</td> <td>Initial Version</td> </tr> <br> </table>
     */
    public static String getPassword(String server, String userId) {

        pwEnv.setPath(userId);
        File userPath = new File(pwEnv.getPW_FILE_PATH());
        if (!userPath.exists()) {
            // throw new CoreException(ExceptionCoreConstants.PW_FAILURE_GET,
            // ExceptionCoreConstants.PW_FAILURE_GET_MSG +
            // ": no user folder found");
        }
        String password = "";
        try {
            ProcessBuilder pbuilder = new ProcessBuilder(PW_ECHO_EXE, server,
                    userId);
            Map<String, String> env = pbuilder.environment();
            env.clear();
            env.put(PW_ENV_MATRIX, pwEnv.getENCRYPT_FILE_PATH());
            Process process = pbuilder.start();
            process.waitFor();

            LineNumberReader reader = new LineNumberReader(
                    new InputStreamReader(process.getInputStream()));
            password = reader.readLine();
        } catch (Exception e) {
            // appLogger.error("Exception>>>>>>>>> " + e.getMessage(), e);
            //
            // throw new CoreException(ExceptionCoreConstants.PW_FAILURE_GET,
            // ExceptionCoreConstants.PW_FAILURE_GET_MSG + ": getPassword");
            // ExceptionHandler.handleCoreExp(e,
            // ExceptionCoreConstants.PW_FAILURE_GET,
            // ExceptionCoreConstants.PW_FAILURE_GET_MSG + ": getPassword",
            // server);

        }

        // appLogger.debug("------------password:" + password);
        if ("NA".equalsIgnoreCase(password)) {
            // throw new CoreException(ExceptionCoreConstants.PW_FAILURE_GET,
            // ExceptionCoreConstants.PW_FAILURE_GET_MSG +
            // ": password set failed.");
            // ExceptionHandler.handleCoreExp(ExceptionCoreConstants.PW_FAILURE_GET,
            // ExceptionCoreConstants.PW_FAILURE_GET_MSG +
            // ": password set failed.");

        }

        return password;
    }

    /**
     * checkPWMatrix Description: This static method check if the encrypt file
     * exist
     *
     * @param encryptedFile It is the encrypt file name
     * @return true if the encrypt file exist, else false
     * <p/>
     * <table border=1 CELLPADDING="3" CELLSPACING="0">
     * <tr BGCOLOR="#CCCCFF">
     * <td>Date</td>
     * <td>Author</td>
     * <td>Details</td>
     * </tr>
     * <tr>
     * <td>04/12/2012</td>
     * <td>Weifeng Lu</td>
     * <td>Initial Version</td>
     * </tr>
     * <br>
     * </table>
     */
    private static boolean checkPWMatrix(String encryptedFile) {
        return new File(encryptedFile).exists() ? true : false;
    }

    /**
     * verifyInput Description: This static method verify the user input
     *
     * @param key It is the key for password
     * @return String return the user id.
     * <p/>
     * <table border=1 CELLPADDING="3" CELLSPACING="0">
     * <tr BGCOLOR="#CCCCFF">
     * <td>Date</td>
     * <td>Author</td>
     * <td>Details</td>
     * </tr>
     * <tr>
     * <td>04/12/2012</td>
     * <td>Weifeng Lu</td>
     * <td>Initial Version</td>
     * </tr>
     * <br>
     * </table>
     */
    private static String verifyInput(String key) {

        // Do some validations here to verify the input.
        if (key == null || key.equals("")) {
            // throw new CoreException(ExceptionCoreConstants.PW_FAILURE_GET,
            // ExceptionCoreConstants.PW_FAILURE_GET_MSG + ": key is invalid");

        }

        String[] keys = key.split(":");

        if (keys == null || keys.length != 2) {
            // throw new CoreException(ExceptionCoreConstants.PW_FAILURE_GET,
            // ExceptionCoreConstants.PW_FAILURE_GET_MSG + ": key is invalid");

        }
        // return the userId
        return keys[1];

    }

}
