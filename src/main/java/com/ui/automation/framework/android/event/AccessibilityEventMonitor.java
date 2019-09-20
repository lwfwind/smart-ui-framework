package com.ui.automation.framework.android.event;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.MultiLineReceiver;
import com.library.common.StringHelper;
import com.ui.automation.framework.android.DebugBridge;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * The type Accessibility event monitor.
 */
@Slf4j
public class AccessibilityEventMonitor {
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static List<String> toastEventLogs = new ArrayList<String>();
    private static boolean isCancelMonitored = false;

    /**
     * Start.
     */
    public static void start() {
        Thread thread = new AccessibilityEventThread();
        thread.setDaemon(true);
        executorService.execute(thread);
    }

    /**
     * Stop.
     */
    public static void stop() {
        isCancelMonitored = true;
        executorService.shutdown();
        executorService.shutdownNow();
    }

    /**
     * Gets last toast.
     *
     * @return the last toast
     */
    public static String getLastToast() {
        readEventLog();
        if (toastEventLogs.size() > 0) {
            return toastEventLogs.get(toastEventLogs.size() - 1);
        }
        return "No Toast Message";
    }

    /**
     * Read event log.
     */
    public static void readEventLog() {
        try {
            IDevice device = DebugBridge.getDevice();
            if (device != null) {
                MultiLineReceiver receiver = new MultiLineReceiver() {
                    @Override
                    public void processNewLines(String[] lines) {
                        for (String line : lines) {
                            if (line.contains("com.abc360.tool")) {
                                toastEventLogs.add(StringHelper.getBetweenString(line, "Message: ", "[Source"));
                            }
                        }
                    }

                    @Override
                    public boolean isCancelled() {
                        return false;
                    }
                };
                device.executeShellCommand(
                        "cat /sdcard/event.log",
                        receiver,
                        5000);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args) throws InterruptedException {
        DebugBridge.init();
        log.info("executorService started");
        start();
        Thread.sleep(10000);
        log.info(getLastToast());
        stop();
        log.info("executorService stoped");
        DebugBridge.terminate();
        log.info("DebugBridge terminated");
    }

    /**
     * The type Accessibility event thread.
     */
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
                log.error(e.getMessage(), e);
            }
        }
    }
}
