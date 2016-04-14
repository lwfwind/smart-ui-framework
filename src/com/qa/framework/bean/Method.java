package com.qa.framework.bean;

/**
 * Created by kcgw001 on 2016/2/24.
 */
public class Method {
    private int id;
    private int hashCode;
    private String name;
    private String status;

    /**
     * Instantiates a new Method.
     *
     * @param id       the id
     * @param hashCode the hash code
     * @param name     the name
     * @param status   the status
     */
    public Method(int id, int hashCode, String name, String status) {
        this.id = id;
        this.hashCode = hashCode;
        this.name = name;
        this.status = status;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Sets status.
     *
     * @param value the value
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets name.
     *
     * @param value the value
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets hash code.
     *
     * @return the hash code
     */
    public int getHashCode() {
        return this.hashCode;
    }

    /**
     * Sets hash code.
     *
     * @param value the value
     */
    public void setHashCode(int value) {
        this.hashCode = value;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getID() {
        return this.id;
    }

    /**
     * Sets id.
     *
     * @param value the value
     */
    public void setID(int value) {
        this.id = value;
    }
}
