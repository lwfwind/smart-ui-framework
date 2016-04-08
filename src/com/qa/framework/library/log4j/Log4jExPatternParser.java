package com.qa.framework.library.log4j;

import org.apache.log4j.helpers.PatternParser;

/**
 * Created by kcgw001 on 2016/2/22.
 */
public class Log4jExPatternParser extends PatternParser {
    /**
     * Instantiates a new Log 4 j ex pattern parser.
     *
     * @param pattern the pattern
     */
    public Log4jExPatternParser(String pattern) {
        super(pattern);
    }

    /**
     * 重写finalizeConverter，对特定的占位符进行处理，T表示线程ID占位符
     */
    @Override
    protected void finalizeConverter(char c) {
        if (c == 'T') {
            this.addConverter(new ExPatternConverter(this.formattingInfo));
        } else {
            super.finalizeConverter(c);
        }
    }
}
