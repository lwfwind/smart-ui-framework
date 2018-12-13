package com.ui.automation.framework.pagefactory.web;

import com.ui.automation.framework.pagefactory.web.interceptor.ElementHandler;
import com.ui.automation.framework.pagefactory.web.interceptor.ElementListHandler;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementListHandler;

import java.lang.reflect.*;
import java.util.List;

/**
 * WrappedElementDecorator recognizes a few things that DefaultFieldDecorator does not.
 * It is designed to support and return concrete implementations of wrappers for a variety of common html elements.
 */
public class ElementDecorator implements FieldDecorator {
    /**
     * factory to use when generating ElementLocator.
     */
    private ElementLocatorFactory factory;

    /**
     * Constructor for an ElementLocatorFactory. This class is designed to replace DefaultFieldDecorator.
     *
     * @param factory for locating elements.
     */
    public ElementDecorator(ElementLocatorFactory factory) {
        this.factory = factory;
    }

    @Override
    public Object decorate(ClassLoader loader, Field field) {
        if (field.getDeclaringClass() == ElementImpl.class) {
            return null;
        }

        ElementLocator locator = factory.createLocator(field);
        Class<?> fieldType = field.getType();
        if (WebElement.class.equals(fieldType)) {
            fieldType = Element.class;
        }

        if (WebElement.class.isAssignableFrom(fieldType)) {
            if (field.getAnnotation(FindBy.class) != null || field.getAnnotation(FindBys.class) != null ||
                    field.getAnnotation(FindAll.class) != null) {
                return proxyForLocator(loader, fieldType, locator, field);
            }
        } else if (List.class.isAssignableFrom(fieldType)) {
            Class<?> actualClass = getActualClass(field);
            if (actualClass != null && WebElement.class.isAssignableFrom(actualClass)) {
                if (field.getAnnotation(FindBy.class) != null || field.getAnnotation(FindBys.class) != null ||
                        field.getAnnotation(FindAll.class) != null) {
                    return proxyForListLocator(loader, actualClass, locator, field);
                }
            }
            return null;
        } else {
            return null;
        }
        return null;
    }

    private Class<?> getActualClass(Field field) {
        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) {
            return null;
        }
        return (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
    }

    /**
     * Generate a type-parameterized locator proxy for the element in question. We use our customized InvocationHandler
     * here to wrap classes.
     *
     * @param <T>           The interface of the proxy.
     * @param loader        ClassLoader of the wrapping class
     * @param interfaceType Interface wrapping the underlying WebElement
     * @param locator       ElementLocator pointing at a proxy of the object on the page
     * @param field         the field
     * @return a proxy representing the class we need to wrap.
     */
    protected <T> T proxyForLocator(ClassLoader loader, Class<T> interfaceType, ElementLocator locator, Field field) {
        InvocationHandler handler = new ElementHandler(interfaceType, locator, field);

        T proxy;
        proxy = interfaceType.cast(Proxy.newProxyInstance(
                loader, new Class[]{interfaceType, WebElement.class, WrapsElement.class, Locatable.class}, handler));
        return proxy;
    }

    /**
     * generates a proxy for a list of elements to be wrapped.
     *
     * @param <T>           class of the interface.
     * @param loader        classloader for the class we're presently wrapping with proxies
     * @param interfaceType type of the element to be wrapped
     * @param locator       locator for items on the page being wrapped
     * @param field         the field
     * @return proxy with the same type as we started with.
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> proxyForListLocator(ClassLoader loader, Class<T> interfaceType, ElementLocator locator, Field field) {
        InvocationHandler handler;
        if (interfaceType.getAnnotation(ImplementedBy.class) != null) {
            handler = new ElementListHandler(interfaceType, locator, field);
        } else {
            handler = new LocatingElementListHandler(locator);
        }
        List<T> proxy;
        proxy = (List<T>) Proxy.newProxyInstance(
                loader, new Class[]{List.class}, handler);
        return proxy;
    }
}
