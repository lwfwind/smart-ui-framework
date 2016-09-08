package com.qa.framework;

import com.qa.framework.common.Sleeper;
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
     * The Alert.
     */
    public Sleeper sleeper;

    /**
     * Instantiates a new Service base.
     */
    public ServiceBase() {
        initFields(this);
        sleeper = new Sleeper();
    }
}
