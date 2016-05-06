package com.qa.framework.library.autoit;

import com.sun.jna.WString;
import org.apache.log4j.Logger;

/**
 * The type Auto it dll.
 */
public class AutoItDLL implements IAutoItX {

    private final static Logger logger = Logger
            .getLogger(AutoItDLL.class);

    /**
     * The Autoitx.
     */
    AutoItXDLL autoitx;

    /**
     * Instantiates a new Auto it dll.
     */
    public AutoItDLL() {
        System.setProperty("jna.encoding", "UTF-16");
        autoitx = AutoItXDLL.INSTANCE;
    }

    /**
     * Sends a mouse click command to a given control.
     *
     * @param title     The title of the window to access.
     * @param text      The text of the window to access.
     * @param controlID The control to interact with.
     * @param button    The button to click, "left", "right" or "middle". Default is
     *                  the left button.
     * @param clicks    The number of times to click the mouse. Default is center.
     * @param x         The x position to click within the control. Default is center.
     * @param y         The y position to click within the control. Default is center.
     * @return True if success, false otherwise.
     */
    public boolean controlClick(String title, String text, String controlID,
                                String button, int clicks, int x, int y) {
        WString Wtitle = new WString(title);
        WString Wtext = new WString(text);
        WString WcontrolID = new WString(controlID);
        WString Wbutton = new WString(button);
        int result = autoitx.AU3_ControlClick(Wtitle, Wtext, WcontrolID,
                Wbutton, clicks, x, y);
        return oneToTrue(result);
    }

    /**
     * Sends a mouse click command to a given control.
     *
     * @param title     The title of the window to access.
     * @param text      The text of the window to access.
     * @param controlID The control to interact with.
     * @param button    The button to click, "left", "right" or "middle". Default is
     *                  the left button.
     * @param clicks    The number of times to click the mouse. Default is center.
     * @return True if success, false otherwise.
     */
    public boolean controlClick(String title, String text, String controlID,
                                String button, int clicks) {
        WString Wtitle = new WString(title);
        WString Wtext = new WString(text);
        WString WcontrolID = new WString(controlID);
        WString Wbutton = new WString(button);
        int result = autoitx.AU3_ControlClick(Wtitle, Wtext, WcontrolID,
                Wbutton, clicks, AutoItXDLL.AU3_INTDEFAULT,
                AutoItXDLL.AU3_INTDEFAULT);
        return oneToTrue(result);
    }

    /**
     * Sends a mouse click command to a given control.
     *
     * @param title     The title of the window to access.
     * @param text      The text of the window to access.
     * @param controlID The control to interact with.
     * @param button    The button to click, "left", "right" or "middle". Default is
     *                  the left button.
     * @return True if success, false otherwise.
     */
    public boolean controlClick(String title, String text, String controlID,
                                String button) {
        WString Wtitle = new WString(title);
        WString Wtext = new WString(text);
        WString WcontrolID = new WString(controlID);
        WString Wbutton = new WString(button);
        int result = autoitx.AU3_ControlClick(Wtitle, Wtext, WcontrolID,
                Wbutton, 1, AutoItXDLL.AU3_INTDEFAULT,
                AutoItXDLL.AU3_INTDEFAULT);
        return oneToTrue(result);
    }

    /**
     * Sends a mouse click command to a given control.
     *
     * @param title     The title of the window to access.
     * @param text      The text of the window to access.
     * @param controlID The control to interact with.
     * @return True if success, false otherwise.
     */
    public boolean controlClick(String title, String text, String controlID) {
        WString Wtitle = new WString(title);
        WString Wtext = new WString(text);
        WString WcontrolID = new WString(controlID);
        WString Wbutton = new WString("left");
        int result = autoitx.AU3_ControlClick(Wtitle, Wtext, WcontrolID,
                Wbutton, 1, AutoItXDLL.AU3_INTDEFAULT,
                AutoItXDLL.AU3_INTDEFAULT);
        return oneToTrue(result);
    }

    /**
     * Activates (gives focus to) a window.
     *
     * @param title The title of the window to activate.
     * @param text  The text of the window to activate.
     */
    public void winActivate(String title, String text) {
        WString Wtitle = new WString(title);
        WString Wtext = new WString(text);
        autoitx.AU3_WinActivate(Wtitle, Wtext);
    }

    /**
     * Activates (gives focus to) a window.
     *
     * @param title The title of the window to activate.
     */
    public void winActivate(String title) {
        WString Wtitle = new WString(title);
        WString Wtext = new WString("");
        autoitx.AU3_WinActivate(Wtitle, Wtext);
    }

    /**
     * Pauses execution of the script until the requested window is active.
     *
     * @param title   The title of the window to check.
     * @param text    The text of the window to check.
     * @param timeout The timeout in seconds.
     * @return True if success, false otherwise.
     */
    public boolean winWaitActive(String title, String text, int timeout) {
        WString Wtitle = new WString(title);
        WString Wtext = new WString(text);
        int result = autoitx.AU3_WinWaitActive(Wtitle, Wtext, timeout);
        return oneToTrue(result);
    }

    /**
     * Pauses execution of the script until the requested window is active.
     *
     * @param title The title of the window to check.
     * @param text  The text of the window to check.
     * @return True if success, false otherwise.
     */
    public boolean winWaitActive(String title, String text) {
        WString Wtitle = new WString(title);
        WString Wtext = new WString(text);
        int result = autoitx.AU3_WinWaitActive(Wtitle, Wtext, 0);
        return oneToTrue(result);
    }

    /**
     * Pauses execution of the script until the requested window is active.
     *
     * @param title The title of the window to check.
     * @return True if success, false otherwise.
     */
    public boolean winWaitActive(String title) {
        WString Wtitle = new WString(title);
        WString Wtext = new WString("");
        int result = autoitx.AU3_WinWaitActive(Wtitle, Wtext, 1);
        return oneToTrue(result);
    }

    /**
     * Checks to see if a specified window exists.
     *
     * @param title The title of the window to activate.
     * @param text  The text of the window to activate.
     * @return True if window exists, false otherwise.
     */
    public boolean winExists(String title, String text) {
        WString Wtitle = new WString(title);
        WString Wtext = new WString(text);
        int result = autoitx.AU3_WinExists(Wtitle, Wtext);
        return oneToTrue(result);
    }

    /**
     * Checks to see if a specified window exists.
     *
     * @param title The title of the window to activate.
     * @return True if window exists, false otherwise.
     */
    public boolean winExists(String title) {
        WString Wtitle = new WString(title);
        WString Wtext = new WString("");
        int result = autoitx.AU3_WinExists(Wtitle, Wtext);
        return oneToTrue(result);
    }

    /**
     * Wait a specified window exists
     *
     * @param title   The title of the window to activate.
     * @return True if window exists, false otherwise.
     * @throws InterruptedException
     */
    public boolean waitWinExists(String title, long timeout)
            throws InterruptedException {
        long end = System.currentTimeMillis() + timeout * 1000;
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() < end) {
            if (winExists(title)) {
                long usedtime = System.currentTimeMillis() - start;
                logger.info("the " + title + " Window is found after "
                        + usedtime / 1000 + " s");
                return true;
            }
            waitHalfASecond();
        }
        return false;
    }

    /**
     * Sets input focus to a given control on a window.
     *
     * @param title   The title of the window to access.
     * @param text    The text of the window to access.
     * @param control The control to interact with.
     * @return True if success, false otherwise.
     */
    public boolean controlFocus(String title, String text, String control) {
        WString Wtitle = new WString(title);
        WString Wtext = new WString(text);
        WString Wcontrol = new WString(control);
        int result = autoitx.AU3_ControlFocus(Wtitle, Wtext, Wcontrol);
        return oneToTrue(result);
    }

    /**
     * Sends simulated keystrokes to the active window.
     *
     * @param keys The sequence of keys to send.
     */
    public void send(String keys) {
        WString Wkeys = new WString(keys);
        autoitx.AU3_Send(Wkeys, 1);
    }

    /**
     * Sends simulated keystrokes to the active window.
     *
     * @param keys  The sequence of keys to send.
     * @param isRaw Changes how "keys" is processed: true - Text contains special               characters like + and ! to indicate SHIFT and ALT key presses. false, keys are sent raw.
     */
    public void send(String keys, boolean isRaw) {
        WString Wkeys = new WString(keys);
        autoitx.AU3_Send(Wkeys, isRaw ? 1 : 0);
    }

    /**
     * Closes a window.
     *
     * @param title The title of the window to activate.
     * @param text  The text of the window to activate.
     */
    public void winClose(String title, String text) {
        WString Wtitle = new WString(title);
        WString Wtext = new WString(text);
        autoitx.AU3_WinClose(Wtitle, Wtext);
    }

    /**
     * Closes a window.
     *
     * @param title The title of the window to activate.
     */
    public void winClose(String title) {
        WString Wtitle = new WString(title);
        WString Wtext = new WString("");
        autoitx.AU3_WinClose(Wtitle, Wtext);
    }

    /**
     * Converts the value 1 to true, anything else to false.
     *
     * @param i The value to convert to true/false
     * @return 1 = true, anything else = false.
     */
    protected boolean oneToTrue(int i) {
        return (i == 1);
    }

    /**
     * Wait for half A second
     * @throws InterruptedException
     */
    private void waitHalfASecond() throws InterruptedException {
        Thread.sleep(500);

    }

}
