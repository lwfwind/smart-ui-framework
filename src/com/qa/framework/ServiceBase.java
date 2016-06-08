package com.qa.framework;

import org.apache.log4j.Logger;

import static com.qa.framework.ioc.AutoInjectHelper.initFields;

/**
 * Created by kcgw001 on 2016/1/12.
 */
public abstract class ServiceBase {
    /**
     * The constant logger.
     */
    protected static Logger logger = Logger.getLogger(ServiceBase.class);

    /**
     * Instantiates a new Service base.
     */
    public ServiceBase() {
        initFields(this);
    }
}
