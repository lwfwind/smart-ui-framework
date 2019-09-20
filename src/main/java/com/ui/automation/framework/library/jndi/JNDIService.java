package com.ui.automation.framework.library.jndi;

import lombok.extern.slf4j.Slf4j;

import javax.naming.directory.DirContext;

/**
 * The type Jndi service.
 */
@Slf4j
public abstract class JNDIService {

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
            log.error(e.getMessage(), e);
        }
    }
}
