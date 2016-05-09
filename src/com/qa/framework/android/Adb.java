package com.qa.framework.android;

import com.qa.framework.config.ProjectEnvironment;
import com.qa.framework.library.base.IOHelper;
import com.qa.framework.library.base.OSHelper;
import com.qa.framework.library.base.ProcessHelper;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * The type Adb.
 */
public class Adb {
    /**
     * The constant logcatPath.
     */
    public static String logcatPath = System.getProperty("java.io.tmpdir") + (OSHelper.isUnix() ? File.separator : "") + "logcat.txt";
    /**
     * The constant allLogcatPath.
     */
    public static String allLogcatPath = System.getProperty("java.io.tmpdir") + (OSHelper.isUnix() ? File.separator : "") + "AllLogcat.txt";
    private static Logger logger = Logger.getLogger(Adb.class);
    private String emulatorProcessName;
    private String avdName;
    private String sdkTarget;
    private String cmdShell;

    /**
     * Instantiates a new Adb.
     *
     * @param avdName   the avd name
     * @param sdkTarget the sdk target
     */
    public Adb(String avdName, String sdkTarget) {
        this.avdName = avdName;
        this.sdkTarget = sdkTarget;
        if (OSHelper.isWindows()) {
            this.cmdShell = "cmd.exe /C ";
            this.emulatorProcessName = "emulator-arm.exe";
        } else if (OSHelper.isUnix()) {
            this.cmdShell = " ";
            this.emulatorProcessName = "emulator-arm";
        } else {
            this.cmdShell = " ";
            this.emulatorProcessName = "emulator64-arm";
        }
    }

    /**
     * Instantiates a new Adb.
     *
     * @param avdName the avd name
     */
    public Adb(String avdName) {
        this(avdName, null);
    }


    /**
     * Instantiates a new Adb.
     */
    public Adb() {
        this(null, null);
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args)
            throws Exception {
        Adb emulatorHelper = new Adb("test", "android-22");
        emulatorHelper.closeEmulator();
        emulatorHelper.deleteEmulator();
        emulatorHelper.createEmulator();
        emulatorHelper.startEmulator();
        emulatorHelper.closeEmulator();

        Adb emulatorHelper2 = new Adb("test");
        emulatorHelper2.restartEmulator();

        Adb emulatorHelper3 = new Adb();
        logger.info(emulatorHelper3.getDeviceName());
        emulatorHelper3.adbPush(ProjectEnvironment.resourcePath() + "upload.jpg", "/data/local/tmp");
        emulatorHelper3.adbPull("/data/local/tmp/upload.jpg", ProjectEnvironment.resourcePath() + "upload.jpg");
    }

    /**
     * Restart adb.
     *
     * @throws IOException          the io exception
     * @throws InterruptedException the interrupted exception
     * @throws ExecutionException   the execution exception
     */
    public void restartAdb()
            throws IOException, InterruptedException, ExecutionException {
        for (int i = 0; i < 3; i++) {
            logger.info("Beginning Restart adb");
            String cmd = this.cmdShell + "adb start-server";
            logger.info("cmd==" + cmd);

            Process process = Runtime.getRuntime().exec(cmd);
            String restart = ProcessHelper.getStreamResult(process, 20, false);
            if (!restart.contains("TimeoutException")) {
                return;
            }
        }
    }

    /**
     * Close adb.
     */
    public void closeAdb() {
        try {
            logger.info("Beginning Close Adb.");
            if (OSHelper.isWindows()) {
                ProcessHelper.closePidsByName("adb.exe");
            } else {
                ProcessHelper.closePidsByName("adb");
            }
            logger.info("close Adb scucessfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create emulator.
     *
     * @throws IOException          the io exception
     * @throws InterruptedException the interrupted exception
     * @throws ExecutionException   the execution exception
     */
    public void createEmulator()
            throws IOException, InterruptedException, ExecutionException {
        logger.info("Beginning Create emulator.");
        String cmd = this.cmdShell + "android create avd --name " + this.avdName + " --target " + this.sdkTarget + " --force --sdcard 100M";
        if (ProcessHelper.errorResult.contains("This platform has more than one ABI. Please specify one using --abi")) {
            logger.info("enter abi configuration");
            cmd = cmd + " --abi armeabi-v7a";
            logger.info("cmd = " + cmd);
        }
        logger.info("cmd==" + cmd);

        Process process = Runtime.getRuntime().exec(cmd);
        Thread.sleep(2000L);

        PrintWriter stdin = new PrintWriter(process.getOutputStream());
        stdin.println("no");
        stdin.close();
        try {
            ProcessHelper.getStreamResult(process);
        } catch (IOException e) {
            logger.info(e.getClass().getName());
            logger.info(ProcessHelper.errorResult);
            if (ProcessHelper.errorResult.contains("This platform has more than one ABI. Please specify one using --abi")) {
                logger.info(e.getClass().getName());
                createEmulator();
            } else {
                throw e;
            }
        }
        logger.info("Create emulator sucessfully.");
    }

    /**
     * Close emulator.
     */
    public void closeEmulator() {
        try {
            logger.info("Beginning Close emulator.");
            ProcessHelper.closePidsByName(this.emulatorProcessName);
            logger.info("close all emulators scucessfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete emulator.
     *
     * @throws IOException          the io exception
     * @throws InterruptedException the interrupted exception
     * @throws ExecutionException   the execution exception
     */
    public void deleteEmulator()
            throws IOException, InterruptedException, ExecutionException {
        logger.info("Beginning Delete emulator.");
        String cmd = this.cmdShell + "android delete avd -n " + this.avdName;
        logger.info("cmd==" + cmd);

        Process process = Runtime.getRuntime().exec(cmd);

        ProcessHelper.getStreamResult(process, 20, false);
        logger.info("Delete emulator scucessfully.");
    }

    /**
     * Restart emulator.
     *
     * @param hostsFilePath the hosts file path
     * @throws Exception the exception
     */
    public void restartEmulator(String hostsFilePath)
            throws Exception {
        closeEmulator();
        restartAdb();
        startEmulator();
        saveAllLogcat(allLogcatPath);
        if (!hostsFilePath.equals("")) {
            remount();
            modifyHosts(hostsFilePath);
        }
        inputKeyEvent(82);
    }

    /**
     * Restart emulator.
     *
     * @throws Exception the exception
     */
    public void restartEmulator()
            throws Exception {
        closeEmulator();
        restartAdb();
        startEmulator();
        saveAllLogcat(allLogcatPath);
    }

    /**
     * Start emulator string.
     *
     * @return the string
     * @throws Exception the exception
     */
    public String startEmulator()
            throws Exception {
        boolean start = false;
        String exception = "";
        String emulatorName = "";
        for (int i = 0; i < 3; i++) {
            try {
                logger.info("Beginning Start emulator.");
                Thread.sleep(2000L);
                String cmd = this.cmdShell + "emulator -avd " + this.avdName + " -partition-size 256";
                logger.info("cmd==" + cmd);
                Process process = Runtime.getRuntime().exec(cmd);
                ProcessHelper.getStreamResult(process);
                Thread.sleep(60000L);
                emulatorName = getDeviceName();
                start = waitForFullyBooted(600000, emulatorName);
                Thread.sleep(5000L);
            } catch (Exception e) {
                start = false;
                exception = e.toString();
            }
            if (!start) {
                closeEmulator();
                closeAdb();
                Thread.sleep(5000L);
            } else {
                logger.info("Start emulator scucessfully.");
                return emulatorName;
            }
        }
        throw new IOException("start emulator failed after try 3 times <br>" + exception);
    }

    /**
     * Remount.
     *
     * @throws IOException the io exception
     */
    public void remount()
            throws IOException {
        logger.info("Beginning adb remount.");
        for (int i = 0; i < 3; i++) {
            try {
                String cmd = this.cmdShell + " adb -s " + getDeviceName() + " remount";
                logger.info("cmd==" + cmd);
                Process process = Runtime.getRuntime().exec(cmd);
                String okResult = ProcessHelper.getStreamResult(process);
                if (okResult.contains("succeeded")) {
                    logger.info("adb remount successfully");
                    return;
                }
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }
        throw new IOException("adb remount failed");
    }


    private ArrayList<String> getEmulatorPid(String processName, String taskList) {
        ArrayList<String> pids = new ArrayList<String>();
        String[] processes = taskList.split("\r\n");
        for (String process1 : processes) {
            if (process1.contains(processName)) {
                logger.info(processName + "==" + process1);
                String[] process = process1.split(processName);
                if (process[1] != null) {
                    String pid = process[1].trim().split(" ")[0];
                    logger.info("pid==" + pid);
                    pids.add(pid);
                }
            }
        }
        return pids;
    }

    private String listDevices()
            throws Exception {
        for (int i = 0; i < 3; i++) {
            logger.info("Beginning list Devices.");
            String cmd = this.cmdShell + "adb devices";
            logger.info("cmd==" + cmd);

            Process process = Runtime.getRuntime().exec(cmd);
            String okResult = ProcessHelper.getStreamResult(process, 20L, true, true);
            if (!okResult.contains("TimeoutException")) {
                logger.info("list Devices scucessfully.");
                return okResult;
            }
        }
        throw new Exception("Timeout");
    }

    private boolean getEmulatorStatus(String emulatorName)
            throws Exception {
        logger.info("Beginning getprop init.svc.bootanim");
        String cmd = this.cmdShell + "adb -s " + emulatorName + " shell getprop init.svc.bootanim";
        logger.info("cmd==" + cmd);
        Process process = null;
        process = Runtime.getRuntime().exec(cmd);
        String okResult = ProcessHelper.getStreamResult(process, 120L, true, true);
        return okResult.contains("stopped");
    }

    /**
     * Wait for fully booted boolean.
     *
     * @param timeout      the timeout
     * @param emulatorName the emulator name
     * @return the boolean
     * @throws Exception the exception
     */
    public boolean waitForFullyBooted(int timeout, String emulatorName)
            throws Exception {
        long startTime = System.currentTimeMillis();
        logger.info("startTime=" + startTime);
        long endTime = startTime + timeout;
        logger.info("endTime=" + endTime);
        while (System.currentTimeMillis() < endTime) {
            boolean status = getEmulatorStatus(emulatorName);
            if (status) {
                return true;
            }
            Thread.sleep(5000);
        }
        return false;
    }

    /**
     * Gets device name.
     *
     * @return the device name
     * @throws Exception the exception
     */
    public String getDeviceName()
            throws Exception {
        String result = listDevices();
        logger.info("listDevices: " + result);
        String[] splitDevices = result.split("\r\n");
        for (int j = 0; j < splitDevices.length; j++) {
            if (splitDevices[j].contains("device") && !splitDevices[j].contains("devices")) {
                logger.info("splitDevice[" + j + "]: " + splitDevices[j]);
                return splitDevices[j].split("\\s")[0];
            }
        }
        throw new Exception("通过adb device 未找到设备名称");
    }

    /**
     * Modify hosts.
     *
     * @param hostsFilePath the hosts file path
     * @throws IOException the io exception
     */
    public void modifyHosts(String hostsFilePath)
            throws IOException {
        logger.info("Beginning modify hosts file.");
        for (int i = 0; i < 3; i++) {
            try {
                String cmd = this.cmdShell + " adb -s " + getDeviceName() + " push " + hostsFilePath + " /system/etc/";
                logger.info("cmd==" + cmd);
                Process process = Runtime.getRuntime().exec(cmd);
                ProcessHelper.getStreamResult(process, 10L, false, true);
                if (ProcessHelper.errorResult.contains("KB/s")) {
                    logger.info("modify hosts file successfully");
                    return;
                }
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }
        throw new IOException("fail to modify hosts file ");
    }

    /**
     * Install apk.
     *
     * @param path the path
     * @throws Exception the exception
     */
    public void installApk(String path)
            throws Exception {
        for (int i = 0; i < 3; i++) {
            try {
                logger.info("Beginning Install APK.");
                String cmd = this.cmdShell + "adb -s " + getDeviceName() + " wait-for-device install -r " + path;
                logger.info("cmd==" + cmd);

                Process process = Runtime.getRuntime().exec(cmd);

                String okResult = ProcessHelper.getStreamResult(process, 180, false);
                System.out.println("okResult============" + okResult);
                if (okResult.contains("Success")) {
                    logger.info("adb install 返回值包含Success字符.");
                } else {
                    throw new IOException("installApkException:" + ProcessHelper.errorResult + okResult);
                }
                logger.info("Install APK Successfully.");
            } catch (IOException e) {
                if (!e.getMessage().contains("TimeoutException")) {
                    throw e;
                }
            }
        }
    }

    /**
     * Un install apk.
     *
     * @param packageName the package name
     * @throws Exception the exception
     */
    public void unInstallApk(String packageName)
            throws Exception {
        logger.info("Beginning Uninstall APK.");
        String cmd = this.cmdShell + "adb -s " + getDeviceName() + " uninstall " + packageName;
        logger.info("cmd==" + cmd);

        Process process = Runtime.getRuntime().exec(cmd);
        process.waitFor();

        ProcessHelper.getStreamResult(process, 20, false);
        logger.info("Uninstall APK Successfully.");
    }

    /**
     * Input key event.
     *
     * @param keyEnvent the key envent
     * @throws Exception the exception
     */
    public void inputKeyEvent(int keyEnvent)
            throws Exception {
        logger.info("Beginning inputKeyEvent.");
        String cmd = this.cmdShell + "adb -s " + getDeviceName() + " shell input keyevent  " + keyEnvent;
        logger.info("cmd==" + cmd);

        Process process = Runtime.getRuntime().exec(cmd);
        process.waitFor();
        ProcessHelper.getStreamResult(process, 60);
        logger.info("End inputKeyEvent.");
    }

    /**
     * Save all logcat.
     *
     * @param savePath the save path
     */
    public void saveAllLogcat(String savePath) {
        try {
            logger.info("Beginning saveAllLogcat to sdcard.");
            IOHelper.creatFile(savePath);
            String cmd = this.cmdShell + "adb -s " + getDeviceName() + " logcat -v time > " + savePath;

            logger.info("cmd==" + cmd);
            Process process;
            if (OSHelper.isWindows()) {
                process = Runtime.getRuntime().exec(cmd);
            } else {
                process = Runtime.getRuntime().exec(new String[]{"sh", "-c", cmd});
            }
            Thread.sleep(2000L);
            ProcessHelper.getStreamResult(process, 5);
        } catch (Exception e) {
            logger.info("saveAllLogcat timeout" + e);
        }
        logger.info("End saveAllLogcat.");
    }

    /**
     * Save needed logcat.
     *
     * @param filter the filter
     */
    public void saveNeededLogcat(String filter) {
        if (filter.trim().equals("")) {
            return;
        }
        logger.info("Beginning saveNeededLogcat.");
        String cmd = "";
        IOHelper.creatFile(logcatPath);
        try {
            Process process;
            if (OSHelper.isWindows()) {
                filter = " \"" + filter.replace(",", " ") + "\" ";
                cmd = this.cmdShell + "findstr /r " + filter + allLogcatPath + " > " + logcatPath;
                logger.info("cmd==" + cmd);
                process = Runtime.getRuntime().exec(cmd);
            } else {
                filter = " '" + filter.replace(",", "|") + "' ";
                cmd = this.cmdShell + "grep -E " + filter + allLogcatPath + " > " + logcatPath;
                logger.info("cmd==" + cmd);
                process = Runtime.getRuntime().exec(new String[]{"sh", "-c", cmd});
            }
            Thread.sleep(2000L);
            ProcessHelper.getStreamResult(process, 60L, true, true);
        } catch (Exception e) {
            logger.info("saveNeededLogcat failed");
        }
        logger.info("End saveNeededLogcat.");
    }

    /**
     * Clear logcat.
     *
     * @throws Exception the exception
     */
    public void clearLogcat()
            throws Exception {
        logger.info("Beginning clearLogcat.");
        String cmd = this.cmdShell + "adb -s " + getDeviceName() + " logcat -c";
        logger.info("cmd==" + cmd);

        Process process = Runtime.getRuntime().exec(cmd);

        ProcessHelper.getStreamResult(process);
    }

    /**
     * Adb pull.
     *
     * @param fromPath the from path
     * @param toPath   the to path
     * @param timeOut  the time out
     * @throws Exception the exception
     */
    public void adbPull(String fromPath, String toPath, int timeOut)
            throws Exception {
        logger.info("Beginning adb Pull File.");
        for (int i = 0; i < 3; i++) {
            String cmd = this.cmdShell + "adb -s " + getDeviceName() + " pull " + fromPath + " " + toPath;
            logger.info("cmd==" + cmd);

            Process process = Runtime.getRuntime().exec(cmd);

            ProcessHelper.getStreamResult(process, timeOut, false);
            if (ProcessHelper.errorResult.contains("KB/s")) {
                logger.info("adb pull 返回值包含KB/s字符,pull成功，返回文件在" + toPath);
                logger.info("End adbPullFile.");
                return;
            }
        }
        throw new IOException("adb pull失败" + ProcessHelper.errorResult + ProcessHelper.okResult);
    }

    /**
     * Adb pull.
     *
     * @param fromPath the from path
     * @param toPath   the to path
     * @throws Exception the exception
     */
    public void adbPull(String fromPath, String toPath)
            throws Exception {
        adbPull(fromPath, toPath, 60);
    }


    /**
     * Adb push.
     *
     * @param fromPath the from path
     * @param toPath   the to path
     * @param timeOut  the time out
     * @throws IOException the io exception
     */
    public void adbPush(String fromPath, String toPath, int timeOut)
            throws IOException {
        logger.info("Beginning push file.");
        for (int i = 0; i < 3; i++) {
            try {
                String cmd = this.cmdShell + " adb -s " + getDeviceName() + " push " + fromPath + " " + toPath;
                logger.info("cmd==" + cmd);
                Process process = Runtime.getRuntime().exec(cmd);
                ProcessHelper.getStreamResult(process, timeOut, false, true);
                if (ProcessHelper.errorResult.contains("KB/s")) {
                    logger.info("push file successfully");
                    return;
                }
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }
        throw new IOException("fail to push file ");
    }

    /**
     * Adb push.
     *
     * @param fromPath the from path
     * @param toPath   the to path
     * @throws IOException the io exception
     */
    public void adbPush(String fromPath, String toPath) throws IOException {
        adbPush(fromPath, toPath, 60);
    }


    /**
     * Adb remove file.
     *
     * @param filePath the file path
     * @throws Exception the exception
     */
    public void adbRemoveFile(String filePath)
            throws Exception {
        logger.info("Beginning adbRemoveFile.");
        String cmd = this.cmdShell + "adb -s " + getDeviceName() + " shell rm " + filePath;
        logger.info("cmd==" + cmd);

        Process process = Runtime.getRuntime().exec(cmd);
        try {
            ProcessHelper.getStreamResult(process, 60, false);
        } catch (Exception e) {
            if (e.getMessage().contains("rm failed for" + filePath)) {
                logger.info(filePath + " is not exist");
            } else {
                throw e;
            }
        }
        logger.info("End adbRemoveFile.");
    }

    /**
     * Adb remove dir.
     *
     * @param dirPath the dir path
     * @throws Exception the exception
     */
    public void adbRemoveDir(String dirPath)
            throws Exception {
        logger.info("Beginning adbRemoveDir.");
        String cmd = this.cmdShell + "adb -s " + getDeviceName() + " shell rm -r " + dirPath;
        logger.info("cmd==" + cmd);

        Process process = Runtime.getRuntime().exec(cmd);
        try {
            ProcessHelper.getStreamResult(process, 60, false);
        } catch (Exception e) {
            if (e.getMessage().contains("rm failed for" + dirPath)) {
                logger.info(dirPath + " is not exist");
            } else {
                throw e;
            }
        }
        logger.info("End adbRemoveDir.");
    }

    /**
     * Pre monkey script string.
     *
     * @param packageName the package name
     * @return the string
     * @throws Exception the exception
     */
    public String preMonkeyScript(String packageName) throws Exception {
        logger.info("*** start preMonkeyScript ***");
        String result = "";
        String cmd = this.cmdShell + " adb -s " + getDeviceName() + " shell monkey --throttle 100 --pct-motion 100 -p " + packageName + " 200";
        logger.info("*** cmd ***" + cmd);
        try {
            Process process;
            if (OSHelper.isWindows()) {
                process = Runtime.getRuntime().exec(cmd);
            } else {
                process = Runtime.getRuntime().exec(new String[]{"sh", "-c", cmd});
            }
            result = ProcessHelper.getStreamResult(process, 20L, true, true);
            System.out.println(result);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        logger.info("*** end preMonkeyScript ***");
        return result;
    }

    /**
     * Execute monkey script string.
     *
     * @param scriptPath the script path
     * @return the string
     * @throws Exception the exception
     */
    public String executeMonkeyScript(String scriptPath) throws Exception {
        logger.info("*** start executeMonkeyScript ***");
        String result = "";
        String cmd = this.cmdShell + " adb -s " + getDeviceName() + " shell monkey -v -f " + scriptPath + " 1";
        logger.info("*** cmd ***" + cmd);
        try {
            Process process;
            if (OSHelper.isWindows()) {
                process = Runtime.getRuntime().exec(cmd);
            } else {
                process = Runtime.getRuntime().exec(new String[]{"sh", "-c", cmd});
            }
            result = ProcessHelper.getStreamResult(process, 20L, true, true);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        logger.info("*** result ***" + result);
        logger.info("*** end executeMonkeyScript ***");
        return result;
    }

    /**
     * Execute monkey test string.
     *
     * @param packageName the package name
     * @param interval    the interval
     * @param counts      the counts
     * @param touch       the touch
     * @param syskeys     the syskeys
     * @param majornav    the majornav
     * @return the string
     * @throws Exception the exception
     */
    public String executeMonkeyTest(String packageName, String interval, String counts, String touch, String syskeys, String majornav) throws Exception {
        logger.info("*** start executeMonkeyTest ***");
        String result = "";
        String tmpSyskeys = syskeys;
        String tmpMajornav = majornav;
        if (!"".equals(touch)) {
            touch = " --pct-touch " + touch;
        }
        if (!"".equals(syskeys)) {
            syskeys = " --pct-syskeys " + syskeys;
        }
        if (!"".equals(majornav)) {
            majornav = " --pct-majornav " + majornav;
        }
        int random = (int) (Math.random() * 1000.0D);
        String cmd = this.cmdShell + "adb -s " + getDeviceName() + " shell monkey -v -p " + packageName + " --ignore-crashes --ignore-timeouts --throttle " + interval + " -s " + random + touch + syskeys + majornav + " " + counts;
        for (int i = 0; (("".equals(result)) || (result.contains("Error: Unknown option: --pct-syskeys"))) && (i < 3); i++) {
            if (result.contains("Error: Unknown option: --pct-syskeys")) {
                majornav = " --pct-majornav " + String.valueOf(Long.parseLong(tmpSyskeys) + Long.parseLong(tmpMajornav));
                cmd = this.cmdShell + "adb -s " + getDeviceName() + " shell monkey -v -p " + packageName + " --ignore-crashes --ignore-timeouts --throttle " + interval + " -s " + random + touch + majornav + " " + counts;
            }
            logger.info(cmd);

            long waitTime = Long.parseLong(interval) * Long.parseLong(counts) * 1000L;
            try {
                Process process;
                if (OSHelper.isWindows()) {
                    process = Runtime.getRuntime().exec(cmd);
                } else {
                    process = Runtime.getRuntime().exec(new String[]{"sh", "-c", cmd});
                }
                result = ProcessHelper.getStreamResult(process, waitTime, true, true);
                File monkeyFile = new File("monkey.log");
                BufferedWriter output = new BufferedWriter(new FileWriter(monkeyFile));
                output.write(result);
                output.close();
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }
        logger.info("*** end executeMonkeyTest ***");
        return result;
    }
}

