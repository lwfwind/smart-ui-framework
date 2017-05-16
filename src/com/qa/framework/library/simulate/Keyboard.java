package com.qa.framework.library.simulate;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * The type Keyboard.
 */
public class Keyboard extends Robot {
    private final static Logger logger = Logger
            .getLogger(Keyboard.class);

    /**
     * Instantiates a new Keyboard.
     *
     * @throws AWTException the awt exception
     */
    public Keyboard() throws AWTException {
        super();
    }

    private void keyType(int keyCode) {
        keyPress(keyCode);
        delay(10);
        keyRelease(keyCode);
    }

    private void keyType(int keyCode, int keyCodeModifier) {
        keyPress(keyCodeModifier);
        keyPress(keyCode);
        delay(10);
        keyRelease(keyCode);
        keyRelease(keyCodeModifier);
    }

    /**
     * Type.
     *
     * @param text the text
     */
    public void type(String text) {
        String textUpper = text.toUpperCase();
        for (int i = 0; i < text.length(); ++i) {
            typeChar(textUpper.charAt(i));
        }
    }

    /**
     * Mimic system-level keyboard event with String
     *
     * @param text the text
     */
    public void inputKeyboard(String text) {
        String cmd = System.getProperty("user.dir") + "\\res\\SeleniumCommand.exe" + " sendKeys " + text;

        Process p = null;
        try {
            p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
    }

    private void typeChar(char c) {
        boolean shift = true;
        int keyCode = 0;
        switch (c) {
            case '~':
                keyCode = KeyEvent.VK_BACK_QUOTE;
                break;
            case '!':
                keyCode = (int) '1';
                break;
            case '@':
                keyCode = (int) '2';
                break;
            case '#':
                keyCode = (int) '3';
                break;
            case '$':
                keyCode = (int) '4';
                break;
            case '%':
                keyCode = (int) '5';
                break;
            case '^':
                keyCode = (int) '6';
                break;
            case '&':
                keyCode = (int) '7';
                break;
            case '*':
                keyCode = (int) '8';
                break;
            case '(':
                keyCode = (int) '9';
                break;
            case ')':
                keyCode = (int) '0';
                break;
            case ':':
                keyCode = (int) ';';
                break;
            case '_':
                keyCode = (int) '-';
                break;
            case '+':
                keyCode = (int) '=';
                break;
            case '|':
                keyCode = (int) '\\';
                break;
            case '?':
                keyCode = (int) '/';
                break;
            case '{':
                keyCode = (int) '[';
                break;
            case '}':
                keyCode = (int) ']';
                break;
            case '<':
                keyCode = (int) ',';
                break;
            case '>':
                keyCode = (int) '.';
                break;
            case '"':
                keyCode = (int) KeyEvent.VK_QUOTE;
                shift = true;
                break;
            case '\'':
                keyCode = (int) KeyEvent.VK_QUOTE;
                shift = false;
                break;
            case '\n':
                keyCode = (int) KeyEvent.VK_ENTER;
                shift = false;
                break;
            default:
                keyCode = (int) c;
                shift = false;
        }
        if (shift) {
            keyType(keyCode, KeyEvent.VK_SHIFT);
        } else {
            keyType(keyCode);
        }
    }
}
