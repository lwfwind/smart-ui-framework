package com.qa.framework.library.log4j;

import org.apache.log4j.helpers.FormattingInfo;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Created by kcgw001 on 2016/2/22.
 */
public class ExPatternConverter extends PatternConverter {
    /**
     * Instantiates a new Ex pattern converter.
     *
     * @param fi the fi
     */
    public ExPatternConverter(FormattingInfo fi) {
        super(fi);
    }

    /**
     * 当需要显示线程ID的时候，返回当前调用线程的ID
     */
    @Override
    protected String convert(LoggingEvent event) {
        return String.valueOf(Thread.currentThread().getId());
    }
}
