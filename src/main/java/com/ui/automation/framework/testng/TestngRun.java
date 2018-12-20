package com.ui.automation.framework.testng;

import com.library.common.IOHelper;
import com.library.common.StringHelper;
import com.library.common.XmlHelper;
import org.dom4j.Element;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.testng.TestNG;
import org.testng.xml.Parser;
import org.testng.xml.XmlSuite;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * testng run for spring boot
 */
public class TestngRun {


    /**
     * run testng suite xml
     *
     * @param xmlSuiteFile  the testng suite xml file
     */
    public static void execute(String xmlSuiteFile) {
        try {
            List<XmlSuite> xmlSuiteList = (List <XmlSuite>)(new Parser(xmlSuiteFile).parse());
            TestNG testNG = new TestNG();
            testNG.setXmlSuites(xmlSuiteList);
            testNG.run();
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

}
