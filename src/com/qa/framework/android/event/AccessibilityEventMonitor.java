package com.qa.framework.android.event;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.MultiLineReceiver;
import com.qa.framework.android.DebugBridge;
import com.qa.framework.library.base.StringHelper;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AccessibilityEventMonitor {
    private static Logger logger = Logger.getLogger(AccessibilityEventMonitor.class);
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static List<String> toastEventLogs = new ArrayList<String>();
    private static boolean isCancelMonitored = false;

    public static void start() {
        Thread thread = new AccessibilityEventThread();
        thread.setDaemon(true);
        executorService.execute(thread);
    }

    public static void stop() {
        isCancelMonitored = true;
        executorService.shutdown();
        executorService.shutdownNow();
    }

    public static String getLastToast() {
        if (toastEventLogs.size() > 0) {
            return toastEventLogs.get(toastEventLogs.size() - 1);
        }
        return "No Toast Message";
    }

    public static void main(String[] args) throws InterruptedException {
        DebugBridge.init();
        logger.info("executorService started");
        start();
        Thread.sleep(10000);
        logger.info(getLastToast());
        stop();
        logger.info("executorService stoped");
        DebugBridge.terminate();
        logger.info("DebugBridge terminated");
    }

    static class AccessibilityEventThread extends Thread {
        @Override
        public void run() {
            try {
                IDevice device = DebugBridge.getDevice();
                if (device != null) {
                    MultiLineReceiver receiver = new MultiLineReceiver() {
                        @Override
                        public void processNewLines(String[] lines) {
                            for (String line : lines) {
                                if (line.contains("ClassName: android.widget.Toast")) {
                                    toastEventLogs.add(StringHelper.getBetweenString(line, "Text: [", "]; ContentDescription"));
                                }
                            }
                        }

                        @Override
                        public boolean isCancelled() {
                            return isCancelMonitored;
                        }
                    };
                    device.executeShellCommand(
                            "/system/bin/uiautomator events",
                            receiver,
                            0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
