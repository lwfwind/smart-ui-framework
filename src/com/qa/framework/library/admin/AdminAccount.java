package com.qa.framework.library.admin;

/**
 * Created by apple on 16/2/18.
 */
public class AdminAccount {
    private String name;
    private String pwd;

    /**
     * Instantiates a new Admin account.
     *
     * @param name the name
     * @param pwd  the pwd
     */
    public AdminAccount(String name, String pwd) {
        this.name = name;
        this.pwd = pwd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdminAccount that = (AdminAccount) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return !(pwd != null ? !pwd.equals(that.pwd) : that.pwd != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (pwd != null ? pwd.hashCode() : 0);
        return result;
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
     * Gets pwd.
     *
     * @return the pwd
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * Sets pwd.
     *
     * @param pwd the pwd
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
