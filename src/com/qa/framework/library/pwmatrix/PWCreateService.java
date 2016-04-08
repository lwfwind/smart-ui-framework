package com.qa.framework.library.pwmatrix;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The type Pw create service.
 */
public class PWCreateService implements PWMatrixConstants {

    /**
     * pwCreate Description: This method create the encrypt file from the source
     * file
     *
     * @param source        It is the source file name
     * @param encryptedFile It is the encrypt file name
     * @throws CoreException <br>                       <p/>                       <table border=1 CELLPADDING="3" CELLSPACING="0">                       <tr BGCOLOR="#CCCCFF">                       <td>Date</td>                       <td>Author</td>                       <td>Details</td>                       </tr>                       <tr>                       <td>04/12/2012</td>                       <td>Weifeng Lu</td>                       <td>Initial Version</td>                       </tr>                       <br>                       </table>
     */
    public void pwCreate(String source, String encryptedFile) {

        try {
            ProcessBuilder pbuilder = new ProcessBuilder(PW_CREATE_EXE, source,
                    encryptedFile);
            pbuilder.directory(new File(PW_EXE_PATH));
            Process process = pbuilder.start();
            process.waitFor();
        } catch (Exception e) {
            // appLogger.error(LOG_MSG_EXCEPTION + e.getMessage(), e);
            // throw new CoreException(ExceptionCoreConstants.PW_FAILURE_CREATE,
            // ExceptionCoreConstants.PW_FAILURE_CREATE_MSG + ": pwCreate");
            // ExceptionHandler.handleCoreExp(e,
            // ExceptionCoreConstants.PW_FAILURE_CREATE,
            // ExceptionCoreConstants.PW_FAILURE_CREATE_MSG+"pwCreate(%s,%s)",
            // source,encryptedFile);
        }
    }

    /**
     * generateSourceFile Description: This method generate the source file
     * based on the given key and value
     *
     * @param key    It is the key
     * @param value  It is the value
     * @param source It is the source file name
     * @throws CoreException <br>                       <p/>                       <table border=1 CELLPADDING="3" CELLSPACING="0">                       <tr BGCOLOR="#CCCCFF">                       <td>Date</td>                       <td>Author</td>                       <td>Details</td>                       </tr>                       <tr>                       <td>04/12/2012</td>                       <td>Weifeng Lu</td>                       <td>Initial Version</td>                       </tr>                       <br>                       </table>
     */
    public void generateSourceFile(String key, String value, String source) {
        PrintWriter filePr = null;
        try {
            File file = new File(source);
            if (!file.exists()) {
                file.createNewFile();
            }
            // if the file exist, continue the following codes
            filePr = new PrintWriter(new FileWriter(file));
            filePr.println((new StringBuffer().append(key).append(":")
                    .append(value)).toString());
        } catch (Exception e) {
            // appLogger.error(LOG_MSG_EXCEPTION + e.getMessage(), e);
            // throw new CoreException(ExceptionCoreConstants.PW_FAILURE_CREATE,
            // ExceptionCoreConstants.PW_FAILURE_CREATE_MSG +
            // ": generateSourceFile");
            // ExceptionHandler.handleCoreExp(e,ExceptionCoreConstants.PW_FAILURE_CREATE,
            // ExceptionCoreConstants.PW_FAILURE_CREATE_MSG +
            // ": generateSourceFile(%s,%s,%s)",key, value, source);

        } finally {
            if (filePr != null) {
                filePr.close();
            }
        }
    }

    /**
     * createKeysFile Description: This method create the key file by invoking
     * the createKeysFile(List<String> keys, String keyFile) throws
     * CoreException
     *
     * @param key     It is the key
     * @param keyFile It is the key file name
     * @throws CoreException <br>                       <p/>                       <table border=1 CELLPADDING="3" CELLSPACING="0">                       <tr BGCOLOR="#CCCCFF">                       <td>Date</td>                       <td>Author</td>                       <td>Details</td>                       </tr>                       <tr>                       <td>04/12/2012</td>                       <td>Weifeng Lu</td>                       <td>Initial Version</td>                       </tr>                       <br>                       </table>
     */
    public void createKeysFile(String key, String keyFile) {
        ArrayList<String> keys = new ArrayList<String>();
        keys.add(key);
        createKeysFile(keys, keyFile);
    }

    /**
     * createKeysFile Description: This method create the key file
     *
     * @param keys    It is the key list
     * @param keyFile It is the key file name
     * @throws CoreException <br>                       <p/>                       <table border=1 CELLPADDING="3" CELLSPACING="0">                       <tr BGCOLOR="#CCCCFF">                       <td>Date</td>                       <td>Author</td>                       <td>Details</td>                       </tr>                       <tr>                       <td>04/12/2012</td>                       <td>Weifeng Lu</td>                       <td>Initial Version</td>                       </tr>                       <br>                       </table>
     */
    public void createKeysFile(List<String> keys, String keyFile) {
        PrintWriter filePr = null;
        try {
            File file = new File(keyFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            // if the file exist, continue the following codes
            filePr = new PrintWriter(new FileWriter(file));
            Iterator<String> iter = keys.iterator();
            while (iter.hasNext()) {
                filePr.println(iter.next());
            }
        } catch (Exception e) {
            // appLogger.error(LOG_MSG_EXCEPTION + e.getMessage(), e);
            // throw new CoreException(ExceptionCoreConstants.PW_FAILURE_CREATE,
            // ExceptionCoreConstants.PW_FAILURE_CREATE_MSG +
            // ": createKeysFile");
            // ExceptionHandler.handleCoreExp(e,ExceptionCoreConstants.PW_FAILURE_CREATE,
            // ExceptionCoreConstants.PW_FAILURE_CREATE_MSG +
            // ": createKeysFile, the file is %s",keyFile);

        } finally {
            if (filePr != null) {
                filePr.close();
            }
        }
    }

    /**
     * deleteSrcFile Description: This method delete the source file
     *
     * @param source It is the source file name
     * @throws CoreException <br>                       <p/>                       <table border=1 CELLPADDING="3" CELLSPACING="0">                       <tr BGCOLOR="#CCCCFF">                       <td>Date</td>                       <td>Author</td>                       <td>Details</td>                       </tr>                       <tr>                       <td>04/12/2012</td>                       <td>Weifeng Lu</td>                       <td>Initial Version</td>                       </tr>                       <br>                       </table>
     */
    public void deleteSrcFile(String source) {
        try {
            // new File(source).deleteOnExit();
            new File(source).delete();
        } catch (Exception e) {
            // appLogger.error(LOG_MSG_EXCEPTION + e.getMessage(), e);
            // throw new CoreException(ExceptionCoreConstants.PW_FAILURE_CREATE,
            // ExceptionCoreConstants.PW_FAILURE_CREATE_MSG +
            // ": createKeysFile");
            // ExceptionHandler.handleCoreExp(e,ExceptionCoreConstants.PW_FAILURE_CREATE,
            // ExceptionCoreConstants.PW_FAILURE_CREATE_MSG +
            // ": deleteSrcFile, the file is %s",source);

        }
    }

}
