package com.qa.framework.library.jndi;

import org.apache.log4j.Logger;

import javax.naming.directory.DirContext;

/**
 * The type Jndi service.
 */
public abstract class JNDIService {
    /**
     * The constant logger.
     */
    protected final static Logger logger = Logger
            .getLogger(JNDIService.class);
    /**
     * The Dir context.
     */
    protected DirContext dirContext = null;

    /**
     * Instantiates a new Jndi service.
     *
     * @param context the context
     */
    protected JNDIService(DirContext context) {
        this.dirContext = context;
    }

    /**
     * closes this context, releases this context's resources immediately
     */
    public void closeDirContext() {
        try {
            if (dirContext != null) {
                dirContext.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
