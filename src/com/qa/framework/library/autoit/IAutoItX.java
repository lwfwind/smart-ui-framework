package com.qa.framework.library.autoit;

/**
 * The interface Auto it x.
 *
 * @author a106403
 */
public interface IAutoItX {

    /**
     * Sends a mouse click command to a given control.
     *
     * @param title     The title of the window to access.
     * @param text      The text of the window to access.
     * @param controlID The control to interact with.
     * @param button    The button to click, "left", "right" or "middle". Default is                  the left button.
     * @param clicks    The number of times to click the mouse. Default is center.
     * @param x         The x position to click within the control. Default is center.
     * @param y         The y position to click within the control. Default is center.
     * @return True if success, false otherwise.
     */
    public boolean controlClick(String title, String text, String controlID,
                                String button, int clicks, int x, int y);

    /**
     * Sends a mouse click command to a given control.
     *
     * @param title     The title of the window to access.
     * @param text      The text of the window to access.
     * @param controlID The control to interact with.
     * @param button    The button to click, "left", "right" or "middle". Default is                  the left button.
     * @param clicks    The number of times to click the mouse. Default is center.
     * @return True if success, false otherwise.
     */
    public boolean controlClick(String title, String text, String controlID,
                                String button, int clicks);

    /**
     * Sends a mouse click command to a given control.
     *
     * @param title     The title of the window to access.
     * @param text      The text of the window to access.
     * @param controlID The control to interact with.
     * @param button    The button to click, "left", "right" or "middle". Default is                  the left button.
     * @return True if success, false otherwise.
     */
    public boolean controlClick(String title, String text, String controlID,
                                String button);

    /**
     * Sends a mouse click command to a given control.
     *
     * @param title     The title of the window to access.
     * @param text      The text of the window to access.
     * @param controlID The control to interact with.
     * @return True if success, false otherwise.
     */
    public boolean controlClick(String title, String text, String controlID);

    /**
     * Activates (gives focus to) a window.
     *
     * @param title The title of the window to activate.
     * @param text  The text of the window to activate.
     */
    public void winActivate(String title, String text);

    /**
     * Activates (gives focus to) a window.
     *
     * @param title The title of the window to activate.
     */
    public void winActivate(String title);

    /**
     * Pauses execution of the script until the requested window is active.
     *
     * @param title   The title of the window to check.
     * @param text    The text of the window to check.
     * @param timeout The timeout in seconds.
     * @return True if success, false otherwise.
     */
    public boolean winWaitActive(String title, String text, int timeout);

    /**
     * Pauses execution of the script until the requested window is active.
     *
     * @param title The title of the window to check.
     * @param text  The text of the window to check.
     * @return True if success, false otherwise.
     */
    public boolean winWaitActive(String title, String text);

    /**
     * Pauses execution of the script until the requested window is active.
     *
     * @param title The title of the window to check.
     * @return True if success, false otherwise.
     */
    public boolean winWaitActive(String title);

    /**
     * Checks to see if a specified window exists.
     *
     * @param title The title of the window to activate.
     * @param text  The text of the window to activate.
     * @return True if window exists, false otherwise.
     */
    public boolean winExists(String title, String text);

    /**
     * Checks to see if a specified window exists.
     *
     * @param title The title of the window to activate.
     * @return True if window exists, false otherwise.
     */
    public boolean winExists(String title);

    /**
     * Wait a specified window exists
     *
     * @param title   The title of the window to activate.
     * @param timeout the timeout
     * @return True if window exists, false otherwise.
     * @throws InterruptedException the interrupted exception
     */
    public boolean waitWinExists(String title, long timeout)
            throws InterruptedException;

    /**
     * Sets input focus to a given control on a window.
     *
     * @param title   The title of the window to access.
     * @param text    The text of the window to access.
     * @param control The control to interact with.
     * @return True if success, false otherwise.
     */
    public boolean controlFocus(String title, String text, String control);

    /**
     * Sends simulated keystrokes to the active window.
     *
     * @param keys The sequence of keys to send.
     */
    public void send(String keys);

    /**
     * Closes a window.
     *
     * @param title The title of the window to activate.
     * @param text  The text of the window to activate.
     */
    public void winClose(String title, String text);

    /**
     * Closes a window.
     *
     * @param title The title of the window to activate.
     */
    public void winClose(String title);

}
