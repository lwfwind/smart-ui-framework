package com.qa.framework.pagefactory.web;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;

import java.util.Dictionary;
import java.util.Map;

/**
 * wraps a web element interface with extra functionality. Anything added here will be added to all descendants.
 */
@ImplementedBy(ElementImpl.class)
public interface Element extends WebElement, WrapsElement, Locatable {
    /**
     * Mouse over.
     */
    void mouseOver();

    /**
     * Scroll into view.
     *
     * @param basedByTop the based by top
     */
    void scrollIntoView(boolean basedByTop);

    /**
     * High light.
     */
    void highLight();

    /**
     * Gets hide attribute.
     *
     * @param attribute the attribute
     * @return the hide attribute
     */
    String getHideAttribute(String attribute);

    /**
     * Double click.
     */
    Map<String, Object> getAllAttributes();

    /**
     * Remove read only.
     */
    void removeReadOnly();

    /**
     * Add read only.
     */
    void addReadOnly();

    /**
     * Double click.
     */
    void doubleClick();
}
