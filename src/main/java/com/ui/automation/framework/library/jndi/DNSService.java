package com.ui.automation.framework.library.jndi;

import lombok.extern.slf4j.Slf4j;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;

/**
 * The type Dns service.
 */
@Slf4j
public class DNSService extends JNDIService {

    /**
     * Instantiates a new Dns service.
     *
     * @param context the context
     */
    protected DNSService(DirContext context) {
        super(context);
    }

    /**
     * get IP Address according to the URL
     *
     * @param Url Uniform Resource Locator
     * @return ip address
     */
    public String getIPAddress(String Url) {
        try {
            Attributes atts = dirContext.getAttributes(Url,
                    new String[]{"A"});
            for (NamingEnumeration<?> ae = atts.getAll(); ae.hasMore(); ) {
                Attribute attr = (Attribute) ae.next();
                for (NamingEnumeration<?> e = attr.getAll(); e.hasMore(); ) {
                    Object ipObject = e.next();
                    if (ipObject != null) {
                        return ipObject.toString();
                    }
                }
            }
        } catch (NamingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
