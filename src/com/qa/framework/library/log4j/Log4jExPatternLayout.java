package com.qa.framework.library.log4j;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.PatternParser;

/**
 * Created by kcgw001 on 2016/2/22.
 */
public class Log4jExPatternLayout extends PatternLayout {
    /**
     * Instantiates a new Log 4 j ex pattern layout.
     *
     * @param pattern the pattern
     */
    public Log4jExPatternLayout(String pattern) {
        super(pattern);
    }

    /**
     * Instantiates a new Log 4 j ex pattern layout.
     */
    public Log4jExPatternLayout() {
        super();
    }

    /**
     * 重写createPatternParser方法，返回PatternParser的子类
     */
    @Override
    protected PatternParser createPatternParser(String pattern) {
        return new Log4jExPatternParser(pattern);
    }
}
