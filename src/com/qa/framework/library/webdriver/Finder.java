package com.qa.framework.library.webdriver;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * The type Finder.
 */
public class Finder {

    private final static Logger logger = Logger.getLogger(Finder.class);

    /***
     * Gets element by Class Name
     *
     * @param driver    the driver
     * @param className the class name
     * @return element by class name
     */
    public static WebElement getElementByClassName(WebDriver driver,
                                                   String className) {
        WebElement element = null;
        try {
            element = driver.findElement(By.className(className));
        } catch (Exception e) {
            //logger.info(e.toString());
        }
        return element;
    }

    /***
     * Gets elements by Class Name
     *
     * @param driver    the driver
     * @param className the class name
     * @return elements by class name
     */
    public static List<WebElement> getElementsByClassName(WebDriver driver,
                                                          String className) {
        List<WebElement> elements = null;
        try {
            elements = driver.findElements(By.className(className));
        } catch (Exception e) {
            //logger.info(e.toString());
        }
        return elements;
    }

    /**
     * Gets elements by class name.
     *
     * @param parentElement the parent element
     * @param className     the class name
     * @return the elements by class name
     */
    public static List<WebElement> getElementsByClassName(
            WebElement parentElement, String className) {
        List<WebElement> elements = null;
        try {
            elements = parentElement.findElements(By.className(className));
        } catch (Exception e) {
            //logger.info(e.toString());
        }
        return elements;
    }

    /***
     * Gets element parent element by Class Name
     *
     * @param parentElement the parent element
     * @param className     the class name
     * @return element by class name
     */
    public static WebElement getElementByClassName(WebElement parentElement,
                                                   String className) {
        WebElement element = null;
        try {
            element = parentElement.findElement(By.className(className));
        } catch (Exception e) {
            //logger.info(e.toString());
        }
        return element;
    }

    /***
     * Gets element by CSS
     *
     * @param driver the driver
     * @param CSS    the css
     * @return element by css
     */
    public static WebElement getElementByCSS(WebDriver driver, String CSS) {
        WebElement element = null;
        try {
            element = driver.findElement(By.cssSelector(CSS));
        } catch (Exception e) {
            //logger.info(e.toString());
        }
        return element;
    }

    /***
     * Gets elements by CSS
     *
     * @param driver the driver
     * @param CSS    the css
     * @return elements by css
     */
    public static List<WebElement> getElementsByCSS(WebDriver driver, String CSS) {
        List<WebElement> elements = null;
        try {
            elements = driver.findElements(By.cssSelector(CSS));
        } catch (Exception e) {
            //logger.info(e.toString());
        }
        return elements;
    }

    /**
     * Gets elements by css.
     *
     * @param parentElement the parent element
     * @param CSS           the css
     * @return the elements by css
     */
    public static List<WebElement> getElementsByCSS(WebElement parentElement,
                                                    String CSS) {
        List<WebElement> elements = null;
        try {
            elements = parentElement.findElements(By.cssSelector(CSS));
        } catch (Exception e) {
            //logger.info(e.toString());
        }
        return elements;
    }

    /**
     * Gets element by css.
     *
     * @param parentElement the parent element
     * @param CSS           the css
     * @return the element by css
     */
    public static WebElement getElementByCSS(WebElement parentElement,
                                             String CSS) {
        WebElement element = null;
        try {
            element = parentElement.findElement(By.cssSelector(CSS));
        } catch (Exception e) {
            //logger.info(e.toString());
        }
        return element;
    }

    /**
     * Gets element by x path.
     *
     * @param driver the driver
     * @param xPath  the x path
     * @return the element by x path
     */
    public static WebElement getElementByXPath(WebDriver driver, String xPath) {
        WebElement element = null;
        try {
            element = driver.findElement(By.xpath(xPath));
        } catch (Exception e) {
            //logger.info(e.toString());
        }
        return element;
    }

    /**
     * Gets elements by x path.
     *
     * @param driver the driver
     * @param xPath  the x path
     * @return the elements by x path
     */
    public static List<WebElement> getElementsByXPath(WebDriver driver,
                                                      String xPath) {
        List<WebElement> elements = null;
        try {
            elements = driver.findElements(By.xpath(xPath));
        } catch (Exception e) {
            //logger.info(e.toString());
        }
        return elements;

    }

    /**
     * Gets elements by x path.
     *
     * @param parentElement the parent element
     * @param xPath         the x path
     * @return the elements by x path
     */
    public static List<WebElement> getElementsByXPath(WebElement parentElement,
                                                      String xPath) {
        List<WebElement> elements = null;
        try {
            elements = parentElement.findElements(By.xpath(xPath));
        } catch (Exception e) {
            //logger.info(e.toString());
        }
        return elements;

    }

    /***
     * Gets element from parent element by xpath
     *
     * @param parentElement the parent element
     * @param xPath         the x path
     * @return element by x path
     */
    public static WebElement getElementByXPath(WebElement parentElement,
                                               String xPath) {
        WebElement element = null;
        try {
            return parentElement.findElement(By.xpath(xPath));
        } catch (Exception e) {
            //logger.info(e.toString());
        }
        return element;
    }

    /**
     * Gets element by link text.
     *
     * @param parentElement the parent element
     * @param LinkText      the link text
     * @return the element by link text
     */
    public static WebElement getElementByLinkText(WebElement parentElement,
                                                  String LinkText) {
        WebElement element = null;
        try {
            element = parentElement.findElement(By.partialLinkText(LinkText));
        } catch (Exception e) {
            //logger.info(e.toString());
        }
        return element;
    }

    /**
     * Gets element by link text.
     *
     * @param driver   the driver
     * @param LinkText the link text
     * @return the element by link text
     */
    public static WebElement getElementByLinkText(WebDriver driver,
                                                  String LinkText) {
        WebElement element = null;
        try {
            element = driver.findElement(By.partialLinkText(LinkText));
        } catch (Exception e) {
            //logger.info(e.toString());
        }
        return element;
    }

    /**
     * Gets elements by link text.
     *
     * @param driver   the driver
     * @param LinkText the link text
     * @return the elements by link text
     */
    public static List<WebElement> getElementsByLinkText(WebDriver driver,
                                                         String LinkText) {
        List<WebElement> elements = null;
        try {
            elements = driver.findElements(By.partialLinkText(LinkText));
        } catch (Exception e) {
            //logger.info(e.toString());
        }
        return elements;
    }

    /**
     * Gets elements by link text.
     *
     * @param parentElement the parent element
     * @param LinkText      the link text
     * @return the elements by link text
     */
    public static List<WebElement> getElementsByLinkText(
            WebElement parentElement, String LinkText) {
        List<WebElement> elements = null;
        try {
            elements = parentElement.findElements(By.partialLinkText(LinkText));
        } catch (Exception e) {
            //logger.info(e.toString());
        }
        return elements;
    }

    /***
     * Gets an element by name or id
     *
     * @param driver   the driver
     * @param nameOrID the name or id
     * @return element by name or id
     */
    public static WebElement getElementByNameOrID(WebDriver driver,
                                                  String nameOrID) {

        WebElement element = null;
        // try getting the element by name
        try {
            element = driver.findElement(By.name(nameOrID));
        } catch (NoSuchElementException e) {
            //logger.info(e.toString());
        }
        if (element == null) {
            try {
                element = driver.findElement(By.id(nameOrID));
            } catch (NoSuchElementException e) {
                //logger.info(e.toString());
            }
        }
        return element;
    }

    /***
     * Gets an element from parent element by name or id
     *
     * @param parentElement the parent element
     * @param nameOrID      the name or id
     * @return element by name or id
     */
    public static WebElement getElementByNameOrID(WebElement parentElement,
                                                  String nameOrID) {

        WebElement element = null;
        // try getting the element by name
        try {
            element = parentElement.findElement(By.name(nameOrID));
        } catch (NoSuchElementException e) {
            //logger.info(e.toString());
        }
        if (element == null) {
            try {
                element = parentElement.findElement(By.id(nameOrID));
            } catch (NoSuchElementException e) {
                //logger.info(e.toString());
            }
        }
        return element;
    }

    /**
     * Gets element by id.
     *
     * @param parentElement the parent element
     * @param ID            the id
     * @return the element by id
     */
    public static WebElement getElementByID(WebElement parentElement, String ID) {

        WebElement element = null;
        // try getting the element by ID
        try {
            element = parentElement.findElement(By.id(ID));
        } catch (NoSuchElementException e) {
            //logger.info(e.toString());
        }
        return element;
    }

    /**
     * Gets element by id.
     *
     * @param driver the driver
     * @param ID     the id
     * @return the element by id
     */
    public static WebElement getElementByID(WebDriver driver, String ID) {

        WebElement element = null;
        // try getting the element by ID
        try {
            element = driver.findElement(By.id(ID));
        } catch (NoSuchElementException e) {
            //logger.info(e.toString());
        }
        return element;
    }

    /**
     * Gets elements by id.
     *
     * @param driver the driver
     * @param ID     the id
     * @return the elements by id
     */
    public static List<WebElement> getElementsByID(WebDriver driver, String ID) {

        List<WebElement> elements = null;
        // try getting the element by ID
        try {
            elements = driver.findElements(By.id(ID));
        } catch (NoSuchElementException e) {
            //logger.info(e.toString());
        }
        return elements;
    }

    /**
     * Gets elements by id.
     *
     * @param parentElement the parent element
     * @param ID            the id
     * @return the elements by id
     */
    public static List<WebElement> getElementsByID(WebElement parentElement,
                                                   String ID) {

        List<WebElement> elements = null;
        // try getting the element by ID
        try {
            elements = parentElement.findElements(By.id(ID));
        } catch (NoSuchElementException e) {
            //logger.info(e.toString());
        }
        return elements;
    }

    /**
     * Gets element by name.
     *
     * @param driver the driver
     * @param Name   the name
     * @return the element by name
     */
    public static WebElement getElementByName(WebDriver driver, String Name) {

        WebElement element = null;
        // try getting the element by name
        try {
            element = driver.findElement(By.name(Name));
        } catch (NoSuchElementException e) {
            //logger.info(e.toString());
        }
        return element;
    }

    /**
     * Gets elements by name.
     *
     * @param driver the driver
     * @param Name   the name
     * @return the elements by name
     */
    public static List<WebElement> getElementsByName(WebDriver driver,
                                                     String Name) {

        List<WebElement> elements = null;
        // try getting the element by name
        try {
            elements = driver.findElements(By.name(Name));
        } catch (NoSuchElementException e) {
            //logger.info(e.toString());
        }
        return elements;
    }

    /**
     * Gets elements by name.
     *
     * @param parentElement the parent element
     * @param Name          the name
     * @return the elements by name
     */
    public static List<WebElement> getElementsByName(WebElement parentElement,
                                                     String Name) {

        List<WebElement> elements = null;
        // try getting the element by name
        try {
            elements = parentElement.findElements(By.name(Name));
        } catch (NoSuchElementException e) {
            //logger.info(e.toString());
        }
        return elements;
    }

    /**
     * Gets element by name.
     *
     * @param parentElement the parent element
     * @param Name          the name
     * @return the element by name
     */
    public static WebElement getElementByName(WebElement parentElement,
                                              String Name) {

        WebElement element = null;
        // try getting the element by name
        try {
            element = parentElement.findElement(By.name(Name));
        } catch (NoSuchElementException e) {
            //logger.info(e.toString());
        }
        return element;
    }

    /***
     * Gets an element by name or id or Xpath
     *
     * @param driver          the driver
     * @param nameOrIDOrXpath the name or id or xpath
     * @return element by name or id or xpath
     */
    public static WebElement getElementByNameOrIDOrXpath(WebDriver driver,
                                                         String nameOrIDOrXpath) {

        WebElement element = null;
        // try getting the element by name
        try {
            element = getElementByNameOrID(driver, nameOrIDOrXpath);
        } catch (NoSuchElementException e) {
            //logger.info(e.toString());
        }
        if (element == null) {
            try {
                element = getElementByNameOrID(driver, nameOrIDOrXpath);
            } catch (NoSuchElementException e) {
                //logger.info(e.toString());
            }
        }
        if (element == null) {
            try {
                element = getElementByXPath(driver, nameOrIDOrXpath);
            } catch (NoSuchElementException e) {
                //logger.info(e.toString());
            }
        }
        return element;
    }

    /***
     * Gets an element from parent element by name or id or Xpath
     *
     * @param parentElement   the parent element
     * @param nameOrIDOrXpath the name or id or xpath
     * @return element by name or id or xpath
     */
    public static WebElement getElementByNameOrIDOrXpath(
            WebElement parentElement, String nameOrIDOrXpath) {

        WebElement element = null;
        // try getting the element by name
        try {
            element = getElementByNameOrID(parentElement, nameOrIDOrXpath);
        } catch (NoSuchElementException e) {
            //logger.info(e.toString());
        }
        if (element == null) {
            try {
                element = getElementByNameOrID(parentElement, nameOrIDOrXpath);
            } catch (NoSuchElementException e) {
                //logger.info(e.toString());
            }
        }
        if (element == null) {
            try {
                element = getElementByXPath(parentElement, nameOrIDOrXpath);
            } catch (NoSuchElementException e) {
                //logger.info(e.toString());
            }
        }
        return element;
    }

    /***
     * Gets select option by xpath
     *
     * @param driver      the driver
     * @param nameOrID    the name or id
     * @param optionXPath the option x path
     * @return select option by x path
     */
    public static WebElement getSelectOptionByXPath(WebDriver driver,
                                                    String nameOrID, String optionXPath) {
        WebElement selectElement = getElementByNameOrID(driver, nameOrID);
        return selectElement.findElement(By.xpath(optionXPath));
    }

    /**
     * Gets select option by option value
     *
     * @param driver      the driver
     * @param nameOrID    the name or id
     * @param optionValue the option value
     * @return select option by value
     */
    public static WebElement getSelectOptionByValue(WebDriver driver,
                                                    String nameOrID, String optionValue) {
        String optionXPath = "//option[@value='" + optionValue + "']";
        return getSelectOptionByXPath(driver, nameOrID, optionXPath);
    }

    /**
     * Gets select option by value.
     *
     * @param driver        the driver
     * @param selectElement the select element
     * @param optionValue   the option value
     * @return the select option by value
     */
    public static WebElement getSelectOptionByValue(WebDriver driver,
                                                    WebElement selectElement, String optionValue) {
        return selectElement.findElement(By.xpath("//option[@value='"
                + optionValue + "']"));
    }

    /**
     * Gets image by alt text.
     *
     * @param driver  the driver
     * @param altText the alt text
     * @return the image by alt text
     */
    public static WebElement getImageByAltText(WebDriver driver, String altText) {
        String xpath = "//img[@alt='" + altText + "']";
        return driver.findElement(By.xpath(xpath));
    }

    /**
     * Gets link by name or id.
     *
     * @param driver   the driver
     * @param nameOrID the name or id
     * @return the link by name or id
     */
    public static WebElement getLinkByNameOrID(WebDriver driver, String nameOrID) {
        String xpath = "//a[@id='" + nameOrID + "']";
        WebElement link = null;
        try {
            link = getElementByXPath(driver, xpath);
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
        if (null != link) {
            return link;
        }
        xpath = "//a[@name='" + nameOrID + "']";
        try {
            link = getElementByXPath(driver, xpath);
        } catch (Exception e) {
            //logger.info(e.toString());
        }
        return link;
    }

    /**
     * Gets element by all.
     *
     * @param driver                                    the driver
     * @param nameOrIDOrXpathOrClassNameOrCssOrLinkText the name or id or xpath or class name or css or link text
     * @return the element by all
     */
    public static WebElement getElementByAll(WebDriver driver,
                                             String nameOrIDOrXpathOrClassNameOrCssOrLinkText) {

        WebElement element = null;
        // try getting the element by name
        try {
            element = getElementByNameOrIDOrXpath(driver,
                    nameOrIDOrXpathOrClassNameOrCssOrLinkText);
        } catch (NoSuchElementException e) {
            //logger.info(e.toString());
        }
        if (element == null) {
            try {
                element = getElementByClassName(driver,
                        nameOrIDOrXpathOrClassNameOrCssOrLinkText);
            } catch (NoSuchElementException e) {
                //logger.info(e.toString());
            }
        }
        if (element == null) {
            try {
                element = getElementByCSS(driver,
                        nameOrIDOrXpathOrClassNameOrCssOrLinkText);
            } catch (NoSuchElementException e) {
                //logger.info(e.toString());
            }
        }
        if (element == null) {
            try {
                element = getElementByLinkText(driver,
                        nameOrIDOrXpathOrClassNameOrCssOrLinkText);
            } catch (NoSuchElementException e) {
                //logger.info(e.toString());
            }
        }
        return element;
    }

    /**
     * Gets element by all.
     *
     * @param parentElement                             the parent element
     * @param nameOrIDOrXpathOrClassNameOrCssOrLinkText the name or id or xpath or class name or css or link text
     * @return the element by all
     */
    public static WebElement getElementByAll(WebElement parentElement,
                                             String nameOrIDOrXpathOrClassNameOrCssOrLinkText) {

        WebElement element = null;
        // try getting the element by name
        try {
            element = getElementByNameOrIDOrXpath(parentElement,
                    nameOrIDOrXpathOrClassNameOrCssOrLinkText);
        } catch (NoSuchElementException e) {
            //logger.info(e.toString());
        }
        if (element == null) {
            try {
                element = getElementByClassName(parentElement,
                        nameOrIDOrXpathOrClassNameOrCssOrLinkText);
            } catch (NoSuchElementException e) {
                //logger.info(e.toString());
            }
        }
        if (element == null) {
            try {
                element = getElementByCSS(parentElement,
                        nameOrIDOrXpathOrClassNameOrCssOrLinkText);
            } catch (NoSuchElementException e) {
                //logger.info(e.toString());
            }
        }
        if (element == null) {
            try {
                element = getElementByLinkText(parentElement,
                        nameOrIDOrXpathOrClassNameOrCssOrLinkText);
            } catch (NoSuchElementException e) {
                //logger.info(e.toString());
            }
        }
        return element;
    }

}
