package com.qa.framework.common;

import com.library.common.StringHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The type Alert.
 */
public class Alert {

    private final static Logger logger = Logger.getLogger(Alert.class);
    private static int stepInterval = 100;
    /**
     * The Driver.
     */
    public WebDriver driver;

    /**
     * Instantiates a new Alert.
     *
     * @param driver the driver
     */
    public Alert(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Pause
     *
     * @param time in millisecond
     */
    public void pause(int time) {
        if (time <= 0) {
            return;
        }
        try {
            Thread.sleep(time);
            //logger.info("Pause " + time + " ms");
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Select last opened window.
     */
    public void selectLastOpenedWindow() {
        Set<String> windowHandles = driver.getWindowHandles();
        int index = windowHandles.size() - 1;
        int count = 0;
        for (String handler : windowHandles) {
            if (count != index) {
                count++;
            } else {
                driver.switchTo().window(handler);
            }
        }
    }

    /**
     * Accept alert.
     */
    public void acceptAlert() {
        driver.switchTo().alert().accept();
    }

    /**
     * Accept alert and msg string.
     *
     * @return the string
     */
    public String acceptAlertAndMsg() {
        long current = System.currentTimeMillis();
        long endCur = current + 2000;
        String msg = null;
        while ((msg == null || msg.trim().equalsIgnoreCase("")) && current <= endCur) {
            try {
                org.openqa.selenium.Alert alert = driver.switchTo().alert();
                msg = alert.getText();
                alert.accept();
            } catch (NoAlertPresentException ignored) {
            }
            pause(500);
            current = System.currentTimeMillis();
        }
        return msg;
    }

    /**
     * Accept alert list.
     *
     * @param timeout the timeout
     * @return the list
     */
    public List<String> acceptAlert(long timeout) {
        List<String> alertMsg = new ArrayList<String>();
        long currentTime = System.currentTimeMillis();
        long maxTime = currentTime + timeout;
        while (currentTime < maxTime) {
            String msg = acceptAlertAndMsg();
            if (StringHelper.isNotEmpty(msg)) {
                alertMsg.add(msg);
                logger.info(">>>>>>>>>>>>>>>>>>>>  alert:" + msg + " <<<<<<<<<<<<<<<<<<<<<<<<<<");
            }
            currentTime = System.currentTimeMillis();
        }
        return alertMsg;
    }


}
