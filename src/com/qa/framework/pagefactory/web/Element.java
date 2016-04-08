package com.qa.framework.pagefactory.web;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;

/**
 * wraps a web element interface with extra functionality. Anything added here will be added to all descendants.
 */
@ImplementedBy(ElementImpl.class)
public interface Element extends WebElement, WrapsElement, Locatable {
    void mouseOver();

    void scrollIntoView(boolean basedByTop);

    void highLight();

    String getHideAttribute(String attribute);

    void removeReadOnly();

    void addReadOnly();

    void doubleClick();
}
