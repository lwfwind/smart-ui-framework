package com.qa.framework.library.base;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

/**
 * Created by kcgw001 on 2016/3/29.
 */
public class ProcessHelper {
    /**
     * The constant errorResult.
     */
    public static String errorResult;
    /**
     * The constant okResult.
     */
    public static String okResult;
    private static Logger logger = Logger.getLogger(ProcessHelper.class);


    /**
     * Port is used boolean.
     *
     * @param port the port
     * @return the boolean
     */
    public static boolean portIsUsed(int port) {
        if (OSHelper.isWindows()) {
            try {
                String cmd = "cmd.exe /C netstat -ano|findstr " + port;
                logger.info("cmd==" + cmd);
                Process process = Runtime.getRuntime().exec(cmd);
                ProcessHelper.getStreamResult(process);
                if (ProcessHelper.okResult.contains("LISTENING")) {
                    return true;
                }
            } catch (IOException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Close pids by name.
     *
     * @param processName the process name
     * @throws IOException          the io exception
     * @throws InterruptedException the interrupted exception
     * @throws ExecutionException   the execution exception
     */
    public static void closePidsByName(String processName)
            throws IOException, InterruptedException, ExecutionException {
        String cmd;
        if (OSHelper.isWindows()) {
            cmd = "cmd.exe /C taskkill /F /im " + processName;
        } else {
            cmd = "killall " + processName;
        }
        logger.info("cmd==" + cmd);
        Process process = Runtime.getRuntime().exec(cmd);
        getStreamResult(process, 20, false);
    }

    /**
     * Gets stream result.
     *
     * @param process the process
     * @return the stream result
     * @throws InterruptedException the interrupted exception
     * @throws ExecutionException   the execution exception
     * @throws IOException          the io exception
     */
    public static String getStreamResult(Process process)
            throws InterruptedException, ExecutionException, IOException {
        return getStreamResult(process, 10L, true, true);
    }

    /**
     * Gets stream result.
     *
     * @param process the process
     * @param timeOut the time out
     * @return the stream result
     * @throws InterruptedException the interrupted exception
     * @throws ExecutionException   the execution exception
     * @throws IOException          the io exception
     */
    public static String getStreamResult(Process process, int timeOut)
            throws InterruptedException, ExecutionException, IOException {
        return getStreamResult(process, timeOut, true, true);
    }

    /**
     * Gets stream result.
     *
     * @param process    the process
     * @param timeOut    the time out
     * @param throwError the throw error
     * @return the stream result
     * @throws InterruptedException the interrupted exception
     * @throws ExecutionException   the execution exception
     * @throws IOException          the io exception
     */
    public static String getStreamResult(Process process, int timeOut, boolean throwError)
            throws InterruptedException, ExecutionException, IOException {
        return getStreamResult(process, timeOut, throwError, true);
    }

    /**
     * Gets stream result.
     *
     * @param process           the process
     * @param timeOut           the time out
     * @param throwError        the throw error
     * @param runGetErrorStream the run get error stream
     * @return the stream result
     * @throws InterruptedException the interrupted exception
     * @throws ExecutionException   the execution exception
     * @throws IOException          the io exception
     */
    public static String getStreamResult(Process process, long timeOut, boolean throwError, boolean runGetErrorStream)
            throws InterruptedException, ExecutionException, IOException {
        errorResult = "";
        okResult = "";
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        String currentMethod = ste[1].getMethodName();
        String invokeMethod = ste[2].getMethodName().equals(currentMethod) ? ste[3].getMethodName() : ste[2].getMethodName();
        logger.info("[" + invokeMethod + "]" + "Begining getStreamResult");
        String timeOutEexception = "";
        ExecutorService exec = Executors.newCachedThreadPool();
        InputStream okStream = process.getInputStream();
        Future<String> futureOkString = exec.submit(new StreamReaderThread(okStream));
        try {
            okResult = futureOkString.get(timeOut, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            okResult = "TimeoutException";
            timeOutEexception = e.toString();
            logger.warn("[" + invokeMethod + "]" + "getInputStreamException==" + e.toString());
        }
        if ((!okResult.contains("android.util.AndroidException")) || ((!"".equals(okResult)) && (!"resign".equals(invokeMethod)))) {
            logger.info("[" + invokeMethod + "]" + "getInputStream==" + okResult);
        }
        InputStream errorStream = null;
        if (runGetErrorStream) {
            errorStream = process.getErrorStream();
            Future<String> futureErrorString = exec.submit(new StreamReaderThread(errorStream));
            try {
                errorResult = (futureErrorString.get(timeOut, TimeUnit.SECONDS));
            } catch (TimeoutException e) {
                timeOutEexception = e.toString();
                logger.warn("[" + invokeMethod + "]" + "getErrorStreamException==" + e.toString());
            }
            if (!"".equals(errorResult)) {
                logger.info("[" + invokeMethod + "]" + "getErrorStream==" + errorResult);
            }
            if ((throwError) && errorResult != null && (!errorResult.equals(""))) {
                closeStream(process);
                throw new IOException("error " + errorResult + "<br>" + timeOutEexception);
            }
        }
        closeStream(process);
        logger.info("[" + invokeMethod + "]" + "End getStreamResult");
        return okResult;
    }

    private static void closeStream(Process process) {
        try {
            if (process != null) {
                process.getOutputStream().close();
            }
        } catch (Exception e) {
            logger.warn("close stream exception :" + e);
        }
    }

    /**
     * The type Stream reader thread.
     */
    static class StreamReaderThread
            implements Callable<String> {
        private InputStream ins;

        /**
         * Instantiates a new Stream reader thread.
         *
         * @param ins the ins
         */
        public StreamReaderThread(InputStream ins) {
            this.ins = ins;
        }

        public String call() {
            String threadResult = "";
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(this.ins, StandardCharsets.UTF_8));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    threadResult = threadResult + line + "\r\n";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return threadResult;
        }
    }
}
