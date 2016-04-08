package com.qa.framework.library.admin;


import java.util.List;

/**
 * The type Base admin bean.
 */
public class BaseAdminBean {

    private String name;
    private List<AdminAccount> adminList;

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
     * Gets admin list.
     *
     * @return the admin list
     */
    public List<AdminAccount> getAdminList() {
        return adminList;
    }

    /**
     * Sets admin list.
     *
     * @param adminList the admin list
     */
    public void setAdminList(List<AdminAccount> adminList) {
        this.adminList = adminList;
    }
}
