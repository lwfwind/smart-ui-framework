package com.qa.framework.library.pwmatrix;

import java.io.*;
import java.util.*;

/**
 * The type Pw update service.
 */
public class PWUpdateService {

    /**
     * readKeysFile Description: This method read the keys from the key file
     *
     * @param fileName It is the key file name
     * @return a string list of the keys <p/> <table border=1 CELLPADDING="3" CELLSPACING="0"> <tr BGCOLOR="#CCCCFF"> <td>Date</td> <td>Author</td> <td>Details</td> </tr> <tr> <td>04/12/2012</td> <td>Weifeng Lu</td> <td>Initial Version</td> </tr> <br> </table>
     */
    public List<String> readKeysFile(String fileName) {
        File file = new File(fileName);
        List<String> result = new ArrayList<String>();
        String line = "";
        try {
            if (file.exists()) {
                LineNumberReader reader = new LineNumberReader(new FileReader(
                        file));
                while ((line = reader.readLine()) != null) {
                    result.add(line);
                }
            } else {
                // throw new
                // CoreException(ExceptionCoreConstants.PW_FAILURE_UPDATE,
                // ExceptionCoreConstants.PW_FAILURE_UPDATE_MSG +
                // ": readKeysFile");
            }
        } catch (Exception e) {
            // appLogger.error("Exception>>>>>>>>> " + e.getMessage(), e);
            // throw new CoreException(ExceptionCoreConstants.PW_FAILURE_UPDATE,
            // ExceptionCoreConstants.PW_FAILURE_UPDATE_MSG + ": readKeysFile");
        }

        return result;
    }

    /**
     * formCredentials Description: This method create the key connectionPoolMap based on the
     * keys list
     *
     * @param keys It is the keys list
     * @return a string connectionPoolMap of the keys <p/> <table border=1 CELLPADDING="3" CELLSPACING="0"> <tr BGCOLOR="#CCCCFF"> <td>Date</td> <td>Author</td> <td>Details</td> </tr> <tr> <td>04/12/2012</td> <td>Weifeng Lu</td> <td>Initial Version</td> </tr> <br> </table>
     */
    public Map<String, String> formCredentials(List<String> keys) {
        Map<String, String> CredMap = new HashMap<String, String>();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            CredMap.put(key, getPwd(key));
        }
        return CredMap;
    }

    /**
     * getPwd Description: This method get the pwd based on the given key by
     * invoking the PWUtility.getPassword(String server, String userId)
     *
     * @param strKey It is the key
     * @return a string of the pwd
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
    private String getPwd(String strKey) {
        if (strKey == null || strKey.equals("")) {
            // throw new CoreException(ExceptionCoreConstants.PW_FAILURE_GET,
            // ExceptionCoreConstants.PW_FAILURE_GET_MSG + ": key is invalid");
            // return strPwd;
        }

        String[] keys = strKey.split(":");

        if (keys == null || keys.length != 2) {
            // throw new CoreException(ExceptionCoreConstants.PW_FAILURE_GET,
            // ExceptionCoreConstants.PW_FAILURE_GET_MSG + ": key is invalid");
            // return strPwd;
        }

        return PWUtility.getPassword(keys[0], keys[1]);
    }

    /**
     * updateCredMap Description: This method create the key connectionPoolMap based on the
     * keys list
     *
     * @param key     It is the key
     * @param value   It is the value
     * @param CredMap it is the credential connectionPoolMap
     * @return a list of the keys <p/> <table border=1 CELLPADDING="3" CELLSPACING="0"> <tr BGCOLOR="#CCCCFF"> <td>Date</td> <td>Author</td> <td>Details</td> </tr> <tr> <td>04/12/2012</td> <td>Weifeng Lu</td> <td>Initial Version</td> </tr> <br> </table>
     */
    public List<String> updateCredMap(String key, String value,
                                      Map<String, String> CredMap) {
        CredMap.put(key, value);
        Iterator<String> it = CredMap.keySet().iterator();
        List<String> keys = new ArrayList<String>();
        while (it.hasNext()) {
            keys.add(it.next());
        }
        return keys;
    }

    /**
     * generateSourceFile Description: This method create the source file from
     * the credmap and the source file
     *
     * @param credMap It is the crendential connectionPoolMap
     * @param source  It is the source file name                <p/>                <table border=1 CELLPADDING="3" CELLSPACING="0">                <tr BGCOLOR="#CCCCFF">                <td>Date</td>                <td>Author</td>                <td>Details</td>                </tr>                <tr>                <td>04/12/2012</td>                <td>Weifeng Lu</td>                <td>Initial Version</td>                </tr>                <br>                </table>
     */
    public void generateSourceFile(Map<String, String> credMap, String source) {
        PrintWriter filePr = null;
        Object[] keysArr = null;
        try {
            File file = new File(source);
            if (!file.exists()) {
                file.createNewFile();
            }

            // if the file exist, continue the following codes
            filePr = new PrintWriter(new FileWriter(file));
            keysArr = credMap.keySet().toArray();
            Arrays.sort(keysArr);
            for (int i = 0; i < keysArr.length; i++) {
                filePr.println(new StringBuffer().append((String) keysArr[i])
                        .append(":").append(credMap.get((String) keysArr[i])));
            }
            /*
             * Iterator<String> it = credMap.keySet().iterator(); while
			 * (it.hasNext()) { String str = it.next(); filePr.println((new
			 * StringBuffer().append(str).append(":")
			 * .append(credMap.get(str)))); }
			 */
            filePr.close();

        } catch (Exception e) {

            // appLogger.error("Exception>>>>>>>>> " + e.getMessage(), e);
            // throw new CoreException(ExceptionCoreConstants.PW_FAILURE_UPDATE,
            // ExceptionCoreConstants.PW_FAILURE_UPDATE_MSG +
            // ": generateSourceFile");
        } finally {
            if (filePr != null) {
                filePr.close();
            }
        }
    }

}
