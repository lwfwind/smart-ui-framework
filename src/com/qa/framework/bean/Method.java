package com.qa.framework.bean;

/**
 * Created by kcgw001 on 2016/2/24.
 */
public class Method {
    private int id;
    private int hashCode;
    private String name;
    private String status;

    public Method(int id, int hashCode, String name, String status) {
        this.id = id;
        this.hashCode = hashCode;
        this.name = name;
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String value) {
        this.status = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(int value) {
        this.hashCode = value;
    }

    public int getID() {
        return this.id;
    }

    public void setID(int value) {
        this.id = value;
    }
}
