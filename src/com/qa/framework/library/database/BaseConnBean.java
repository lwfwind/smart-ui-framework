package com.qa.framework.library.database;

/**
 * The type Base conn bean.
 */
public class BaseConnBean {

    private String name;
    private String username;
    private String password;
    private String jdbcurl;
    private int max;
    private long wait;
    private String driver;

    /**
     * Gets driver.
     *
     * @return the driver
     */
    public String getDriver() {
        return driver;
    }

    /**
     * Sets driver.
     *
     * @param driver the driver
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets jdbcurl.
     *
     * @return the jdbcurl
     */
    public String getJdbcurl() {
        return jdbcurl;
    }

    /**
     * Sets jdbcurl.
     *
     * @param jdbcurl the jdbcurl
     */
    public void setJdbcurl(String jdbcurl) {
        this.jdbcurl = jdbcurl;
    }

    /**
     * Gets max.
     *
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets max.
     *
     * @param max the max
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Gets wait.
     *
     * @return the wait
     */
    public long getWait() {
        return wait;
    }

    /**
     * Sets wait.
     *
     * @param wait the wait
     */
    public void setWait(long wait) {
        this.wait = wait;
    }

}
