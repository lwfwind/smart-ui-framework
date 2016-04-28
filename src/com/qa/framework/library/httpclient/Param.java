package com.qa.framework.library.httpclient;


import com.qa.framework.library.base.StringHelper;

/**
 * 封装一个测试用例需要的一个数据
 * Created by apple on 15/11/18.
 */
public class Param {
    private String name;            //参数名字
    private String type;            //参数类型, 目前无用字段
    private String value;            //参数值

    private boolean show = true;           //是否出现在链接中

    public Param() {
    }

    public Param(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue(boolean useDecode) {
        if (useDecode) {
            return StringHelper.urlDecode(value);
        } else {
            return value;
        }
    }

    public String getValue() {
        return StringHelper.urlDecode(value);
    }

    public void setValue(String value) {
        this.value = StringHelper.urlEncode(value);
    }


    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public void setShow(String show) {
        this.show = StringHelper.changeString2boolean(show);
    }

    @Override
    public String toString() {
        return name + "= " + value;
    }
}
