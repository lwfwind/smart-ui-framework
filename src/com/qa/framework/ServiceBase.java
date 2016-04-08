package com.qa.framework;

import org.apache.log4j.Logger;

import static com.qa.framework.library.reflect.AutoInjectHelper.initFields;

/**
 * Created by kcgw001 on 2016/1/12.
 */
public abstract class ServiceBase {
    protected final Logger logger = Logger.getLogger(this.getClass());

    public ServiceBase() {
        initFields(this);
    }
}
