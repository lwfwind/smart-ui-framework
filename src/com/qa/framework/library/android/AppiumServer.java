package com.qa.framework.library.android;

import com.qa.framework.library.base.OSHelper;
import com.qa.framework.library.base.ProcessHelper;
import org.apache.log4j.Logger;

import java.io.IOException;
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
        logger.info(ProcessHelper.portIsUsed(4723));
        logger.info(start("127.0.0.1", 4723));
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
                        if (ProcessHelper.portIsUsed(port)) {
                            return true;
                        }
                    } catch (IOException | InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

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
