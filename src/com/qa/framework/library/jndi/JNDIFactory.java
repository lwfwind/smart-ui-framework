package com.qa.framework.library.jndi;

import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Properties;

/**
 * The type Jndi factory.
 */
public class JNDIFactory {
    private final static Logger logger = Logger
            .getLogger(JNDIFactory.class);

    private static final String LDAPFACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

    private static final String DNSFACTORY = "com.sun.jndi.dns.DnsContextFactory";

    /**
     * Instantiates a new Jndi factory.
     */
    public JNDIFactory() {
    }

    /**
     * get JNDI service by JNDI type passed<br>
     *
     * @param type JNDI service type, such as DNS, LDAP
     * @param env  environment used to create the initial DirContext. Null             indicates an empty environment.
     * @return return null if any error
     */
    public static JNDIService getJNDIService(JNDIType type, Env env) {
        Env environment = env;
        switch (type) {
            case LDAP:
                environment.factory = LDAPFACTORY;
                return new LDAPService(getLDAPDirContext(environment));
            case DNS:
                environment.factory = DNSFACTORY;
                return new DNSService(getDNSDirContext(environment));
            case ADP:
                break;
            default:
                break;
        }
        return null;
    }

    /**
     * get LDAP directory context
     *
     * @param env environment used to create the initial DirContext. Null            indicates an empty environment.
     * @return return null if any error
     */
    public static DirContext getLDAPDirContext(Env env) {
        try {
            Properties mEnv = new Properties();

            mEnv.put(Context.PROVIDER_URL, env.url);
            mEnv.put(Context.SECURITY_PRINCIPAL, env.adminUID);
            mEnv.put(Context.SECURITY_CREDENTIALS, env.adminPWD);

            mEnv.put(Context.AUTHORITATIVE, "true");
            mEnv.put(Context.INITIAL_CONTEXT_FACTORY, env.factory);
            mEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
            mEnv.put(Context.REFERRAL, "follow");

            if (!(env.timeOut == null || env.timeOut.length() == 0)) {
                mEnv.put("com.sun.jndi.ldap.connect.timeout", env.timeOut);
            }

            if ("ssl".equals(env.securityProtocol)) {

                mEnv.put(Context.SECURITY_PROTOCOL, env.securityProtocol);
                System.setProperty("javax.net.ssl.trustStore",
                        env.sslTrustStore);
            }
            DirContext ctx = new InitialDirContext(mEnv);
            return ctx;
        } catch (NamingException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * get DNS directory context
     *
     * @param env environment used to create the initial DirContext. Null            indicates an empty environment.
     * @return return null if any error
     */
    public static DirContext getDNSDirContext(Env env) {
        try {
            Properties mEnv = new Properties();
            mEnv.put(Context.PROVIDER_URL, env.url);
            mEnv.put(Context.INITIAL_CONTEXT_FACTORY, env.factory);
            DirContext ctx = new InitialDirContext(mEnv);
            return ctx;
        } catch (NamingException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * JNDI service type enum
     */
    public enum JNDIType {
        /**
         * Dns jndi type.
         */
        DNS,
        /**
         * Ldap jndi type.
         */
        LDAP,
        /**
         * Adp jndi type.
         */
        ADP,
    }

}
