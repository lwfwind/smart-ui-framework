package com.qa.framework.library.database;

import com.qa.framework.config.ProjectEnvironment;
import com.qa.framework.library.base.XMLHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The type Xml to bean.
 */
public class XmlToBean {

    /**
     * Read list.
     *
     * @return the list
     */
    public static List<BaseConnBean> read() {
        return read(ProjectEnvironment.dbConfigFile());
    }

    /**
     * Read list.
     *
     * @param path the path
     * @return the list
     */
    public static List<BaseConnBean> read(String path) {

        List<BaseConnBean> pools = new ArrayList<BaseConnBean>();
        XMLHelper XmlUtil = new XMLHelper();
        XmlUtil.readXMLFile(path);
        List<Element> list = XmlUtil.findElementsByXPath("pools/pool");
        Element pool = null;
        Iterator<Element> allPool = list.iterator();
        while (allPool.hasNext()) {
            pool = (Element) allPool.next();
            BaseConnBean bcBean = new BaseConnBean();
            bcBean.setName(XmlUtil.getChildText(pool, "name"));
            bcBean.setUsername(XmlUtil.getChildText(pool, "username"));
            bcBean.setPassword(XmlUtil.getChildText(pool, "password"));
            bcBean.setJdbcurl(XmlUtil.getChildText(pool, "jdbcurl"));
            try {
                bcBean.setMax(Integer.parseInt(XmlUtil
                        .getChildText(pool, "max")));
            } catch (NumberFormatException e) {
                bcBean.setMax(0);
            }
            try {
                bcBean.setWait(Long.parseLong(XmlUtil
                        .getChildText(pool, "wait")));
            } catch (NumberFormatException e) {
                bcBean.setWait(-1L);
            }
            bcBean.setDriver(XmlUtil.getChildText(pool, "driver"));
            pools.add(bcBean);
        }
        return pools;
    }

}
