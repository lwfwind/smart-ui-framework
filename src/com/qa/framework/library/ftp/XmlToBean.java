package com.qa.framework.library.ftp;

import com.library.common.XmlHelper;
import com.qa.framework.config.ProjectEnvironment;
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
    public static List<FTPConnBean> read() {
        return read(ProjectEnvironment.ftpConfigFile());
    }

    /**
     * Read list.
     *
     * @param path the path
     * @return the list
     */
    public static List<FTPConnBean> read(String path) {

        List<FTPConnBean> servers = new ArrayList<FTPConnBean>();
        XmlHelper XmlUtil = new XmlHelper();
        XmlUtil.readXMLFile(path);
        List<Element> list = XmlUtil.findElementsByXPath("Servers/Server");
        Element server = null;
        Iterator<Element> Server = list.iterator();
        while (Server.hasNext()) {
            server = (Element) Server.next();
            FTPConnBean ftpBean = new FTPConnBean();
            ftpBean.setName(XmlUtil.getChildText(server, "name"));
            ftpBean.setUserName(XmlUtil.getChildText(server, "username"));
            ftpBean.setPassword(XmlUtil.getChildText(server, "password"));
            ftpBean.setHost(XmlUtil.getChildText(server, "host"));
            servers.add(ftpBean);
        }
        return servers;
    }

}
