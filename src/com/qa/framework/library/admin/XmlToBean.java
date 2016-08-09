package com.qa.framework.library.admin;

import com.qa.framework.config.ProjectEnvironment;
import com.qa.framework.config.PropConfig;
import com.qa.framework.library.base.XMLHelper;
import com.qa.framework.library.database.BaseConnBean;
import org.apache.log4j.Logger;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The type Xml to bean.
 */
public class XmlToBean {
    private final static Logger logger = Logger
            .getLogger(XmlToBean.class);
    private static BaseAdminBean baseAdminBean = null;

    static {
        read(ProjectEnvironment.adminConfigFile());
    }

    /**
     * Read.
     *
     * @param path the path
     */
    public static void read(String path) {

        List<BaseConnBean> pools = new ArrayList<BaseConnBean>();
        XMLHelper XmlUtil = new XMLHelper();
        XmlUtil.readXMLFile(path);
        List<Element> servers = XmlUtil.findElementsByXPath("Servers/Server");
        String serverName = PropConfig.getWebPath().split("/")[2].trim();
        Element server = null;
        for (Element server1 : servers) {
            server = server1;
            String xmlName = XmlUtil.getChildText(server, "name");
            if (xmlName.equalsIgnoreCase(serverName)) {
                baseAdminBean = new BaseAdminBean();
                baseAdminBean.setName(xmlName);
                break;
            }
        }
        if (baseAdminBean == null) {
            logger.error("对不起,请检查AdminConfig.xml的配置");
            //http://stackoverflow.com/questions/2070293/why-doesnt-java-allow-to-throw-a-checked-exception-from-static-initialization-b
            //throw new ExceptionInInitializerError("对不起,请检查AdminConfig.xml的配置");
        }
        Element adminRoot = XmlUtil.getChildElement(server, "admins");
        List<Element> admins = XmlUtil.getChildElements(adminRoot, "admin");
        List<AdminAccount> adminAccounts = new ArrayList<AdminAccount>();
        for (Element admin : admins) {
            String username = XmlUtil.getChildText(admin, "username");
            String password = XmlUtil.getChildText(admin, "password");
            AdminAccount adminAccount = new AdminAccount(username, password);
            adminAccounts.add(adminAccount);
        }
        baseAdminBean.setAdminList(adminAccounts);
    }

    /**
     * Gets base admin bean.
     *
     * @return the base admin bean
     */
    public static BaseAdminBean getBaseAdminBean() {
        return baseAdminBean;
    }

}
