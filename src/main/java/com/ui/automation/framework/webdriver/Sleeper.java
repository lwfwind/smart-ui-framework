package com.ui.automation.framework.webdriver;

/**
 * The type Sleeper.
 */
public class Sleeper {

    /**
     * Sleeps the current thread for a default pause length.
     */
    public void sleep() {
        sleep(500);
    }


    /**
     * Sleeps the current thread for a default mini pause length.
     */
    public void sleepMini() {
        sleep(300);
    }


    /**
     * Sleeps the current thread for <code>time</code> milliseconds.
     *
     * @param time the length of the sleep in milliseconds
     */
    public void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignored) {
        }
    }

}
