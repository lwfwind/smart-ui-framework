package com.qa.framework.data;

import org.apache.log4j.Logger;

/**
 * Created by kcgw001 on 2016/3/31.
 */
public interface SuiteData {
    public Logger logger = Logger.getLogger(SuiteData.class);
    /**
     * Sets .
     */
    public void setup();

    /**
     * Teardown.
     */
    public void teardown();
}
