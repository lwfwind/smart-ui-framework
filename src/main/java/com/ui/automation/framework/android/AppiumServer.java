package com.ui.automation.framework.android;

import com.library.common.OSHelper;
import com.library.common.ProcessHelper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The type Appium server.
 */
public class AppiumServer {
    private final static ReentrantLock lock = new ReentrantLock(true); //uses "fair" thread ordering policy
    private static Logger logger = Logger.getLogger(AppiumServer.class);
    private static Process process;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args)
            throws Exception {
        logger.info(available(4723));
        logger.info(start("127.0.0.1", 4723));
    }

    /**
     * Checks to see if a specific port is available.
     *
     * @param port the port to check for availability
     * @return the boolean
     */
    public static boolean available(int port) {
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException ignored) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }

    /**
     * Restart boolean.
     *
     * @param address the address
     * @param port    the port
     * @return the boolean
     */
    public static boolean start(String address, int port) {
        lock.lock();
        try {
            if (OSHelper.isWindows()) {
                for (int i = 0; i < 3; i++) {
                    try {
                        ProcessHelper.closePidsByName("node.exe");
                        String cmd = "cmd.exe /C start appium -a " + address + " -p " + port;
                        logger.info("cmd==" + cmd);
                        process = Runtime.getRuntime().exec(cmd);
                        ProcessHelper.getStreamResult(process);
                        if (!available(port)) {
                            return true;
                        }
                    } catch (IOException | InterruptedException | ExecutionException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Stop.
     */
    public static void stop() {
        lock.lock();
        try {
            if (process != null) {
                process.destroy();
            }
            process = null;
        } finally {
            lock.unlock();
        }
    }
}
