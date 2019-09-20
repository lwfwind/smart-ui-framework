package com.ui.automation.framework.library.simulate;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitWebElement;

import java.awt.*;
import java.awt.event.InputEvent;

/**
 * The type Mouse.
 */
@Slf4j
public class Mouse {

    /**
     * Click.
     *
     * @param element the element
     */
    public static void click(WebElement element) {
        org.openqa.selenium.Point point = element.getLocation();
        org.openqa.selenium.Dimension size = element.getSize();
        clickOn(point.getX() + size.getWidth() / 2,
                point.getY() + size.getHeight() / 2);
    }

    /**
     * Click.
     *
     * @param element the element
     */
    public static void click(HtmlUnitWebElement element) {
        org.openqa.selenium.Point point = element.getLocation();
        org.openqa.selenium.Dimension size = element.getSize();
        clickOn(point.getX() + size.getWidth() / 2,
                point.getY() + size.getHeight() / 2);
    }

    /**
     * Click on.
     *
     * @param x the x
     * @param y the y
     */
    public static void clickOn(int x, int y) {
        try {
            Robot robot = new Robot();
            robot.mouseMove(x, y);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
        } catch (AWTException e) {
            log.error(e.toString());
        }
    }

}
