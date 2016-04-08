package com.qa.framework.library.pwmatrix;

import java.io.File;

/**
 * The type Pw matrix environment.
 */
public class PWMatrixEnvironment implements PWMatrixConstants {

    private transient String pwFilePath = "";
    private transient String keyFile = "";
    private transient String sourceFilePath = "";
    private transient String encryptFilePath = "";

    private transient String sourceFileRelativePath = "";
    private transient String encryptFileRelativePath = "";

    /**
     * setPath Description: This method set the file path used by pwmatrix
     *
     * @param userId <table border=1 CELLPADDING="3" CELLSPACING="0">               <tr BGCOLOR="#CCCCFF">               <td>Date</td>               <td>Author</td>               <td>Details</td>               </tr>               <tr>               <td>04/12/2012</td>               <td>Weifeng Lu</td>               <td>Initial Version</td>               </tr>               <br>               </table>
     */
    public void setPath(String userId) {

        setPW_FILE_PATH(userId);
        setKEY_FILE();
        setSOURCE_FILE_PATH();
        setENCRYPT_FILE_PATH();
        setSOURCE_FILE_RELATIVE_PATH(userId);
        setENCRYPT_FILE_RELATIVE_PATH(userId);
    }

    /**
     * getPW_FILE_PATH Description: This method get the file path used by
     * pwmatrix
     *
     * @param pwFilePath <table border=1 CELLPADDING="3" CELLSPACING="0">                   <tr BGCOLOR="#CCCCFF">                   <td>Date</td>                   <td>Author</td>                   <td>Details</td>                   </tr>                   <tr>                   <td>04/12/2012</td>                   <td>Weifeng Lu</td>                   <td>Initial Version</td>                   </tr>                   <br>                   </table>
     * @return the pw file path
     */
    public String getPW_FILE_PATH() {
        return pwFilePath;
    }

    /**
     * setPW_FILE_PATH Description: This method set the file path used by
     * pwmatrix
     *
     * @param userId the user id
     */
    public void setPW_FILE_PATH(String userId) {
        pwFilePath = new StringBuffer().append(PW_EXE_PATH)
                .append(File.separator).append(userId).append(File.separator)
                .toString();
    }

    /**
     * getKEY_FILE Description: This method get the key file used by pwmatrix
     *
     * @param keyFile <table border=1 CELLPADDING="3" CELLSPACING="0">                <tr BGCOLOR="#CCCCFF">                <td>Date</td>                <td>Author</td>                <td>Details</td>                </tr>                <tr>                <td>08/11/2009</td>                <td>Zhichao Zhang</td>                <td>Initial Version</td>                </tr>                <br>                </table>
     * @return the key file
     */
    public String getKEY_FILE() {
        return keyFile;
    }

    /**
     * setKEY_FILE Description: This method set the key file used by pwmatrix
     *
     * @param keyFile <table border=1 CELLPADDING="3" CELLSPACING="0">                <tr BGCOLOR="#CCCCFF">                <td>Date</td>                <td>Author</td>                <td>Details</td>                </tr>                <tr>                <td>08/11/2009</td>                <td>Zhichao Zhang</td>                <td>Initial Version</td>                </tr>                <br>                </table>
     */
    public void setKEY_FILE() {
        keyFile = pwFilePath + PW_KEYS_NAME;
    }

    /**
     * getSOURCE_FILE_PATH Description: This method get the source file used by
     * pwmatrix
     *
     * @param sourceFilePath <table border=1 CELLPADDING="3" CELLSPACING="0">                       <tr BGCOLOR="#CCCCFF">                       <td>Date</td>                       <td>Author</td>                       <td>Details</td>                       </tr>                       <tr>                       <td>08/11/2009</td>                       <td>Zhichao Zhang</td>                       <td>Initial Version</td>                       </tr>                       <br>                       </table>
     * @return the source file path
     */
    public String getSOURCE_FILE_PATH() {
        return sourceFilePath;
    }

    /**
     * setSOURCE_FILE_PATH Description: This method get the source file used by
     * pwmatrix
     *
     * @param sourceFilePath <table border=1 CELLPADDING="3" CELLSPACING="0">                       <tr BGCOLOR="#CCCCFF">                       <td>Date</td>                       <td>Author</td>                       <td>Details</td>                       </tr>                       <tr>                       <td>08/11/2009</td>                       <td>Zhichao Zhang</td>                       <td>Initial Version</td>                       </tr>                       <br>                       </table>
     */
    public void setSOURCE_FILE_PATH() {
        sourceFilePath = pwFilePath + PW_SOURCE_NAME;
    }

    /**
     * getSOURCE_FILE_RELATIVE_PATH Description: This method get the source file
     * used by pwmatrix
     *
     * @param sourceFileRelativePath <table border=1 CELLPADDING="3" CELLSPACING="0">                               <tr BGCOLOR="#CCCCFF">                               <td>Date</td>                               <td>Author</td>                               <td>Details</td>                               </tr>                               <tr>                               <td>08/11/2009</td>                               <td>Zhichao Zhang</td>                               <td>Initial Version</td>                               </tr>                               <br>                               </table>
     * @return the source file relative path
     */
    public String getSOURCE_FILE_RELATIVE_PATH() {
        return sourceFileRelativePath;
    }

    /**
     * setSOURCE_FILE_RELATIVE_PATH Description: This method get the source file
     * used by pwmatrix
     *
     * @param userId the user id
     */
    public void setSOURCE_FILE_RELATIVE_PATH(String userId) {
        sourceFileRelativePath = new StringBuffer().append(".//")
                .append(userId).append("//").append(PW_SOURCE_NAME).toString();
    }

    /**
     * getENCRYPT_FILE_PATH Description: This method get the entrypted file used
     * by pwmatrix
     *
     * @param encryptFilePath <table border=1 CELLPADDING="3" CELLSPACING="0">                        <tr BGCOLOR="#CCCCFF">                        <td>Date</td>                        <td>Author</td>                        <td>Details</td>                        </tr>                        <tr>                        <td>08/11/2009</td>                        <td>Zhichao Zhang</td>                        <td>Initial Version</td>                        </tr>                        <br>                        </table>
     * @return the encrypt file path
     */
    public String getENCRYPT_FILE_PATH() {
        return encryptFilePath;
    }

    /**
     * setENCRYPT_FILE_PATH Description: This method get the entrypted file used
     * by pwmatrix
     *
     * @param encryptFilePath <table border=1 CELLPADDING="3" CELLSPACING="0">                        <tr BGCOLOR="#CCCCFF">                        <td>Date</td>                        <td>Author</td>                        <td>Details</td>                        </tr>                        <tr>                        <td>08/11/2009</td>                        <td>Zhichao Zhang</td>                        <td>Initial Version</td>                        </tr>                        <br>                        </table>
     */
    public void setENCRYPT_FILE_PATH() {
        encryptFilePath = pwFilePath + PW_ENCRYPT_NAME;
    }

    /**
     * getENCRYPT_FILE_RELATIVE_PATH Description: This method get the entrypted
     * file used by pwmatrix
     *
     * @param encryptFileRelativePath <table border=1 CELLPADDING="3" CELLSPACING="0">                                <tr BGCOLOR="#CCCCFF">                                <td>Date</td>                                <td>Author</td>                                <td>Details</td>                                </tr>                                <tr>                                <td>08/11/2009</td>                                <td>Zhichao Zhang</td>                                <td>Initial Version</td>                                </tr>                                <br>                                </table>
     * @return the encrypt file relative path
     */
    public String getENCRYPT_FILE_RELATIVE_PATH() {
        return encryptFileRelativePath;
    }

    /**
     * setENCRYPT_FILE_RELATIVE_PATH Description: This method get the entrypted
     * file used by pwmatrix
     *
     * @param userId the user id
     */
    public void setENCRYPT_FILE_RELATIVE_PATH(String userId) {
        encryptFileRelativePath = new StringBuffer().append(".//")
                .append(userId).append("//").append(PW_ENCRYPT_NAME).toString();
    }
}
