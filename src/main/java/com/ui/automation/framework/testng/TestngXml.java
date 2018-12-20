package com.ui.automation.framework.testng;

import com.library.common.XmlHelper;
import com.ui.automation.framework.TestCaseBase;
import org.dom4j.Element;
import org.reflections.Reflections;
import org.springframework.core.io.ClassPathResource;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The type Testng xml generator.
 */
public class TestngXml {

    private static List<Map<String, Object>> methodListMap = new ArrayList<Map<String, Object>>();

    /**
     * System.setProperty("browser","chrome");
     * System.setProperty("hubURL","http://192.168.20.196:4444/wd/hub");
     * autoGenerate("1");
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        if (args.length > 1) {
            autoGenerate(args[0], Integer.parseInt(args[1]));
        } else {
            autoGenerate(Integer.parseInt(args[0]));
        }
    }

    /**
     * Auto generate.
     *
     * @param path      the path
     * @param threadCnt the thread cnt
     */
    public static String autoGenerate(String path, int threadCnt) {
        methodListMap.clear();

        Reflections reflections = new Reflections("");
        Set<Class<? extends TestCaseBase>> testCaseClassSet = reflections.getSubTypesOf(TestCaseBase.class);
        for (Class<? extends TestCaseBase> testCaseClass : testCaseClassSet) {
            Method[] methods = testCaseClass.getDeclaredMethods();
            for (Method method : methods) {
                Annotation[] annotations = method.getDeclaredAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType().equals(Test.class)) {
                        Map<String, Object> methodMap = new HashMap<>();
                        methodMap.put("className", testCaseClass.getSimpleName());
                        methodMap.put("packageName", testCaseClass.getPackage().getName());
                        methodMap.put("methodName", method.getName());
                        methodListMap.add(methodMap);
                    }
                }
            }
        }

        XmlHelper xml = new XmlHelper();
        xml.createDocument();
        Element root = xml.createDocumentRoot("suite");
        xml.addAttribute(root, "name", "xml_" + threadCnt);
        xml.addAttribute(root, "thread-count", String.valueOf(threadCnt));
        xml.addAttribute(root, "parallel", "tests");
        xml.addAttribute(root, "verbose", "1");
        Element listeners = xml.addChildElement(root, "listeners");
        Element listener = xml.addChildElement(listeners, "listener");
        xml.addAttribute(listener, "class-name", "com.ui.automation.framework.testng.listener.RetryListener");
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
        String t = new SimpleDateFormat("MM-dd-HH-mm-ss").format(new Date());
        String xmlSuiteFile = path + File.separator + "testng_suite_" + threadCnt + "_" + t + ".xml";
        xml.saveTo(xmlSuiteFile);
        return xmlSuiteFile;
    }

    /**
     * Auto generate.
     *
     * @param threadCnt the thread cnt
     */
    public static String autoGenerate(int threadCnt) {
        ClassPathResource res = new ClassPathResource(".");
        String basePath = null;
        try {
            //get target dir
            basePath = res.getFile().getParent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (basePath == null) {
            basePath = System.getProperty("user.dir");
        }
        return autoGenerate(basePath, threadCnt);
    }

}
