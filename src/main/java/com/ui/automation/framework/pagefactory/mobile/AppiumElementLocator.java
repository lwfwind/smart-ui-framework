package com.ui.automation.framework.pagefactory.mobile;

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.common.base.Function;
import io.appium.java_client.pagefactory.TimeOutDuration;
import io.appium.java_client.pagefactory.locator.CacheableLocator;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.ui.automation.framework.pagefactory.mobile.ThrowableUtil.*;


/**
 * The type Appium element locator.
 */
class AppiumElementLocator implements CacheableLocator {

    /**
     * The Should cache.
     */
    final boolean shouldCache;
    /**
     * The By.
     */
    final By by;
    /**
     * The Time out duration.
     */
    final TimeOutDuration timeOutDuration;
    /**
     * The Original web driver.
     */
    final WebDriver originalWebDriver;
    private final SearchContext searchContext;
    private final WaitingFunction waitingFunction;
    private WebElement cachedElement;
    private List<WebElement> cachedElementList;

    /**
     * Creates a new mobile element locator. It instantiates {@link WebElement}
     * using @AndroidFindBy (-s), @iOSFindBy (-s) and @FindBy (-s) annotation
     * sets
     *
     * @param searchContext     The context to use when finding the element
     * @param by                a By locator strategy
     * @param shouldCache       is the flag that signalizes that elements which are found once should be cached
     * @param duration          is a POJO which contains timeout parameters
     * @param originalWebDriver the original web driver
     */
    public AppiumElementLocator(SearchContext searchContext, By by, boolean shouldCache, TimeOutDuration duration, WebDriver originalWebDriver) {
        this.searchContext = searchContext;
        this.shouldCache = shouldCache;
        this.timeOutDuration = duration;
        this.by = by;
        this.originalWebDriver = originalWebDriver;
        waitingFunction = new WaitingFunction(this.searchContext);
    }

    private void changeImplicitlyWaitTimeOut(long newTimeOut,
                                             TimeUnit newTimeUnit) {
        originalWebDriver.manage().timeouts().implicitlyWait(newTimeOut, newTimeUnit);
    }

    // This method waits for not empty element list using all defined by
    private List<WebElement> waitFor() {
        // When we use complex By strategies (like ChainedBy or ByAll)
        // there are some problems (StaleElementReferenceException, implicitly
        // wait time out
        // for each chain By section, etc)
        try {
            changeImplicitlyWaitTimeOut(0, TimeUnit.SECONDS);
            FluentWait<By> wait = new FluentWait<>(by);
            wait.withTimeout(timeOutDuration.getTime(),
                    timeOutDuration.getTimeUnit());
            return wait.until(waitingFunction);
        } catch (TimeoutException e) {
            return new ArrayList<>();
        } finally {
            changeImplicitlyWaitTimeOut(timeOutDuration.getTime(),
                    timeOutDuration.getTimeUnit());
        }
    }

    /**
     * Find the element.
     */
    public WebElement findElement() {
        if (cachedElement != null && shouldCache) {
            return cachedElement;
        }
        List<WebElement> result = waitFor();
        if (result.size() == 0) {
            String message = "Can't locate an element by this strategy: "
                    + by.toString();
            if (waitingFunction.foundStaleElementReferenceException != null) {
                throw new NoSuchElementException(message, waitingFunction.foundStaleElementReferenceException);
            }
            throw new NoSuchElementException(message);
        }
        if (shouldCache) {
            cachedElement = result.get(0);
        }
        return result.get(0);
    }

    /**
     * Find the element list.
     */
    public List<WebElement> findElements() {
        if (cachedElementList != null && shouldCache) {
            return cachedElementList;
        }
        List<WebElement> result = waitFor();
        if (shouldCache) {
            cachedElementList = result;
        }
        return result;
    }

    @Override
    public boolean isLookUpCached() {
        return shouldCache;
    }

    // This function waits for not empty element list using all defined by
    private static class WaitingFunction implements
            Function<By, List<WebElement>> {
        private final SearchContext searchContext;
        /**
         * The Found stale element reference exception.
         */
        Throwable foundStaleElementReferenceException;

        private WaitingFunction(SearchContext searchContext) {
            this.searchContext = searchContext;
        }

        public List<WebElement> apply(By by) {
            List<WebElement> result = new ArrayList<>();
            Throwable shouldBeThrown = null;
            boolean isRootCauseInvalidSelector;
            boolean isRootCauseStaleElementReferenceException = false;
            foundStaleElementReferenceException = null;

            try {
                result.addAll(searchContext.findElements(by));
            } catch (Throwable e) {

                isRootCauseInvalidSelector = isInvalidSelectorRootCause(e);
                if (!isRootCauseInvalidSelector) {
                    isRootCauseStaleElementReferenceException = isStaleElementReferenceException(e);
                }

                if (isRootCauseStaleElementReferenceException) {
                    foundStaleElementReferenceException = extractReadableException(e);
                }

                if (!isRootCauseInvalidSelector & !isRootCauseStaleElementReferenceException) {
                    shouldBeThrown = extractReadableException(e);
                }
            }

            if (shouldBeThrown != null) {
                if (RuntimeException.class.isAssignableFrom(shouldBeThrown.getClass())) {
                    throw (RuntimeException) shouldBeThrown;
                }
                throw new RuntimeException(shouldBeThrown);
            }

            if (result.size() > 0) {
                return result;
            } else {
                return null;
            }
        }
    }
}

