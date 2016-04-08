package com.qa.framework.library.jndi;

/**
 * This is wrapper class of environment to create initial DirContext of JNDI
 * service
 */
public class Env {

    /**
     * The Factory.
     */
    String factory;

    /**
     * The Url.
     */
    String url;

    /**
     * The Admin uid.
     */
    String adminUID;

    /**
     * The Admin pwd.
     */
    String adminPWD;

    /**
     * The Ssl trust store.
     */
    String sslTrustStore;

    /**
     * The Security protocol.
     */
    String securityProtocol;

    /**
     * The Time out.
     */
    String timeOut;

    /**
     * Instantiates a new Env.
     *
     * @param url service provider url
     */
    public Env(String url) {
        this.url = url;
    }

    /**
     * Instantiates a new Env.
     *
     * @param url      service provider url
     * @param adminUID user ID
     * @param adminPWD user security credential
     */
    public Env(String url, String adminUID, String adminPWD) {
        this.url = url;
        this.adminUID = adminUID;
        this.adminPWD = adminPWD;
    }

    /**
     * Instantiates a new Env.
     *
     * @param url              the url
     * @param adminUID         the admin uid
     * @param adminPWD         the admin pwd
     * @param sslTrustStore    the ssl trust store
     * @param securityProtocol the security protocol
     */
    public Env(String url, String adminUID, String adminPWD,
               String sslTrustStore, String securityProtocol) {
        this.url = url;
        this.adminUID = adminUID;
        this.adminPWD = adminPWD;
        this.sslTrustStore = sslTrustStore;
        this.securityProtocol = securityProtocol;
    }

    /**
     * Instantiates a new Env.
     *
     * @param url              the url
     * @param adminUID         the admin uid
     * @param adminPWD         the admin pwd
     * @param timeOut          the time out
     * @param sslTrustStore    the ssl trust store
     * @param securityProtocol the security protocol
     */
    public Env(String url, String adminUID, String adminPWD, String timeOut,
               String sslTrustStore, String securityProtocol) {
        this.url = url;
        this.adminUID = adminUID;
        this.adminPWD = adminPWD;
        this.timeOut = timeOut;
        this.sslTrustStore = sslTrustStore;
        this.securityProtocol = securityProtocol;
    }
}
