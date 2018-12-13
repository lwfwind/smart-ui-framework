package com.ui.automation.framework.library.ftp;

import com.library.common.IOHelper;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.List;

/**
 * The type Ftp helper.
 */
public class FTPHelper {

    private final static Logger logger = Logger
            .getLogger(FTPHelper.class);
    private FTPClient ftp;
    private String server = "";
    private int port = 21;
    private String username;
    private String password;

    /**
     * Login.
     *
     * @param serverName the server name
     */
    public void login(String serverName) {
        List<FTPConnBean> servers = XmlToBean.read();
        for (FTPConnBean ftpConnBean : servers) {
            if (ftpConnBean.getName().equals(serverName)) {
                server = ftpConnBean.getHost();
                username = ftpConnBean.getUserName();
                password = ftpConnBean.getPassword();
            }

        }
    }

    /**
     * Connect ftp server ftp client.
     *
     * @return the ftp client
     */
    public FTPClient connectFtpServer() {
        // Create a FTP Client object
        ftp = new FTPClient();
        try {
            ftp.setDefaultPort(port);
            // Connect to server
            ftp.connect(server, port);
            // user
            ftp.login(username, password);
            // reply code
            int reply = ftp.getReplyCode();
            if ((!FTPReply.isPositiveCompletion(reply))) {
                ftp.disconnect();
                logger.error("user the FTP server failed, Please check whether server["
                        + server
                        + "], username["
                        + username
                        + "], password["
                        + password + "] are correct!");
            }
            return ftp;
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
        return ftp;
    }

    /**
     * Disconnect FTP Server
     *
     * @param ftp the ftp
     */
    public void disconnectFtpServer(FTPClient ftp) {
        try {
            ftp.disconnect();
        } catch (Exception ex) {
            logger.info(ex.toString());
        }
    }

    /**
     * Upload the local file to remote server
     *
     * @param localIn        the local in
     * @param remoteFilePath the remote file path
     */
    public void upload(InputStream localIn, String remoteFilePath) {

        FTPClient ftp = this.connectFtpServer();
        try {
            boolean result = ftp.storeFile(remoteFilePath, localIn);
            if (!result) {
                logger.error("Upload failed!");
            }
        } catch (Exception ex) {
            logger.error(ex.toString());
        } finally {
            this.disconnectFtpServer(ftp);
        }
    }

    /**
     * Upload the local file to remote server
     *
     * @param localIn                     the local in
     * @param remoteFilePath              the remote file path
     * @param afterUploadCloseInputStream the after upload close input stream
     * @throws Exception the exception
     */
    public void upload(InputStream localIn, String remoteFilePath,
                       boolean afterUploadCloseInputStream) throws Exception {
        try {
            // Upload
            this.upload(localIn, remoteFilePath);
        } finally {
            if (afterUploadCloseInputStream && localIn != null) {
                try {
                    localIn.close();
                } catch (Exception ex) {
                    logger.error(ex.toString());
                }
            }
        }
    }

    /**
     * Download file from remote server
     *
     * @param remotePath the remote path
     * @param fileName   the file name
     * @param localPath  the local path
     * @return boolean boolean
     */
    public boolean download(String remotePath, String fileName, String localPath) {
        IOHelper.createNestDirectory(localPath);
        FTPClient ftp = this.connectFtpServer();
        boolean success = false;
        try {
            int reply;
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return success;
            }
            ftp.changeWorkingDirectory(remotePath);
            File localFile = new File(localPath + File.separator + fileName);
            OutputStream out = new FileOutputStream(localFile);
            ftp.retrieveFile(fileName, out);
            out.close();
            ftp.logout();
            success = true;
        } catch (IOException e) {
            logger.error(e.toString());
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    logger.error(ioe.toString());
                }
            }
        }
        return success;
    }

    /**
     * Download with wildcard boolean.
     *
     * @param remotePath         the remote path
     * @param WildcardFileFilter the wildcard file filter
     * @param localPath          the local path
     * @return the boolean
     */
    public boolean downloadWithWildcard(String remotePath,
                                        String WildcardFileFilter, String localPath) {
        IOHelper.createNestDirectory(localPath);
        FTPClient ftp = this.connectFtpServer();
        boolean success = false;
        try {
            int reply;
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return success;
            }
            ftp.changeWorkingDirectory(remotePath);
            FTPFile[] files = ftp.listFiles();
            for (FTPFile file : files) {
                if (FilenameUtils.wildcardMatch(file.getName(),
                        WildcardFileFilter)) {
                    File localFile = new File(localPath + File.separator
                            + file.getName());
                    FileOutputStream fos = new FileOutputStream(localFile);

                    ftp.retrieveFile(file.getName(), fos);
                    fos.close();
                }
            }
            ftp.logout();
            success = true;
        } catch (IOException e) {
            logger.error(e.toString());
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    logger.error(ioe.toString());
                }
            }
        }
        return success;
    }

    /**
     * Delete the specified file
     *
     * @param ftp the ftp
     * @param url the url
     * @throws Exception the exception
     */
    public void deleteFile(FTPClient ftp, String url) throws Exception {
        ftp.deleteFile("/root/" + url);
        int status = ftp.getReplyCode();
        logger.info("ftp delete info:" + ftp.getReplyString());
        if (status == 250) {
            logger.info("Delete file successfully");

        }
    }
}
