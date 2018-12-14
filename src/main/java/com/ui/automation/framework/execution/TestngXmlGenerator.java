package com.ui.automation.framework.execution;

import com.library.common.IOHelper;
import com.library.common.StringHelper;
import com.library.common.XmlHelper;
import org.dom4j.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Testng xml generator.
 */
public class TestngXmlGenerator {

    private static List<Map<String, Object>> methodListMap = new ArrayList<Map<String, Object>>();

    /**
     * System.setProperty("browser","chrome");
     * System.setProperty("hubURL","http://192.168.20.196:4444/wd/hub");
     * autoGenerate("1");
     * @param args the input arguments
     */
    public static void main(String[] args) {
        if (args.length > 1) {
            autoGenerate(args[0], args[1]);
        } else {
            autoGenerate(args[0]);
        }
    }

    /**
     * Auto generate.
     *
     * @param modulePath the module path
     * @param threadCnt    the thread cnt
     */
    public static void autoGenerate(String modulePath, String threadCnt) {
        methodListMap.clear();
        List<String> files = IOHelper.listFilesInDirectoryRecursive(modulePath, "*.java");
        String className = null;
        String packageName = null;
        for (String file : files) {
            boolean isMatchedFile = false;
            List<String> lines = IOHelper.readLinesToList(file);
            List<String> noBlankLines = new ArrayList<>();
            for (String line : lines) {
                if (!(line.trim().equals("") || line.trim().startsWith("//"))) {
                    noBlankLines.add(line);
                }
            }
            String contents = IOHelper.readFileToString(file);
            for (String line : noBlankLines) {
                if (contents != null && contents.lastIndexOf("@Test") > 0 && line.lastIndexOf("class") > 0 && line.lastIndexOf("extends") > 0) {
                    className = StringHelper.getBetweenString(line, "class", "extends").trim();
                    isMatchedFile = true;
                    break;
                }
            }
            if (isMatchedFile) {
                int index = 0;
                for (String line : noBlankLines) {
                    if (StringHelper.startsWithIgnoreCase(line.trim(), "package ")) {
                        packageName = StringHelper.getBetweenString(line, "package ", ";").trim();
                    }
                    if (StringHelper.startsWithIgnoreCase(line.trim(), "@Test") && !line.trim().contains("enabled = false")) {
                        Map<String, Object> methodMap = new HashMap<>();
                        methodMap.put("className", className);
                        methodMap.put("packageName", packageName);
                        String methodLine = noBlankLines.get(index + 1);
                        String methodName = StringHelper.getBetweenString(methodLine, "void ", "(").trim();
                        methodMap.put("methodName", methodName);
                        methodListMap.add(methodMap);
                    }
                    index++;
                }
            }
        }

        XmlHelper xml = new XmlHelper();
        xml.createDocument();
        Element root = xml.createDocumentRoot("suite");
        xml.addAttribute(root, "name", "xml_" + threadCnt);
        xml.addAttribute(root, "thread-count", threadCnt);
        xml.addAttribute(root, "parallel", "tests");
        xml.addAttribute(root, "verbose", "1");
        Element listeners = xml.addChildElement(root, "listeners");
        Element listener = xml.addChildElement(listeners, "listener");
        xml.addAttribute(listener, "class-name", "com.ui.automation.framework.testnglistener.RetryListener");
        Element listener2 = xml.addChildElement(listeners, "listener");
        xml.addAttribute(listener2, "class-name", "com.ui.automation.framework.testnglistener.SuiteListener");
        for (Map<String, Object> methodMap : methodListMap) {
            Element test = xml.addChildElement(root, "test");
            xml.addAttribute(test, "name", methodMap.get("className").toString() + "_" + methodMap.get("methodName").toString());
            xml.addAttribute(test, "timeout", "600000");
            if (System.getProperty("browser") != null) {
                Element parameter = xml.addChildElement(test, "parameter");
                xml.addAttribute(parameter, "name", "browser");
                xml.addAttribute(parameter, "value", System.getProperty("browser"));
            }
            if (System.getProperty("hubURL") != null) {
                Element parameter = xml.addChildElement(test, "parameter");
                xml.addAttribute(parameter, "name", "hubURL");
                xml.addAttribute(parameter, "value", System.getProperty("hubURL"));
            }
            Element classes = xml.addChildElement(test, "classes");
            Element cls = xml.addChildElement(classes, "class");
            xml.addAttribute(cls, "name", methodMap.get("packageName").toString() + "." + methodMap.get("className").toString());
            Element methods = xml.addChildElement(cls, "methods");
            Element include = xml.addChildElement(methods, "include");
            xml.addAttribute(include, "name", methodMap.get("methodName").toString());
        }
        String outputPath = modulePath+File.separator+"target";
        //IOHelper.deleteDirectory(outputPath);
        IOHelper.createNestDirectory(outputPath);
        if (outputPath.endsWith("/")) {
            xml.saveTo(outputPath + "testng_" + threadCnt + ".xml");
        } else {
            xml.saveTo(outputPath + File.separator + "testng_" + threadCnt + ".xml");
        }
    }

    /**
     * Auto generate.
     *
     * @param threadCnt  the thread cnt
     */
    public static void autoGenerate(String threadCnt) {
        String basePath = System.getProperty("user.dir");
        autoGenerate(basePath, threadCnt);
    }

}
