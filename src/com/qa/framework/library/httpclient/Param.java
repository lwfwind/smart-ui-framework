package com.qa.framework.library.httpclient;


import com.library.common.StringHelper;

/**
 * 封装一个测试用例需要的一个数据
 * Created by apple on 15/11/18.
 */
public class Param {
    private String name;            //参数名字
    private String type;            //参数类型, 目前无用字段
    private String value;            //参数值

    private boolean show = true;           //是否出现在链接中

    /**
     * Instantiates a new Param.
     */
    public Param() {
    }

    /**
     * Instantiates a new Param.
     *
     * @param name  the name
     * @param value the value
     */
    public Param(String name, String value) {
        this.name = name;
        this.value = value;
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
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets value.
     *
     * @param useDecode the use decode
     * @return the value
     */
    public String getValue(boolean useDecode) {
        if (useDecode) {
            return StringHelper.urlDecode(value);
        } else {
            return value;
        }
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return StringHelper.urlDecode(value);
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(String value) {
        this.value = StringHelper.urlEncode(value);
    }


    /**
     * Is show boolean.
     *
     * @return the boolean
     */
    public boolean isShow() {
        return show;
    }

    /**
     * Sets show.
     *
     * @param show the show
     */
    public void setShow(String show) {
        this.show = StringHelper.changeString2boolean(show);
    }

    /**
     * Sets show.
     *
     * @param show the show
     */
    public void setShow(boolean show) {
        this.show = show;
    }

    @Override
    public String toString() {
        return name + "= " + value;
    }
}
