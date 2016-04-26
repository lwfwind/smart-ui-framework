package com.qa.framework.autogenerator;

import com.qa.framework.library.base.IOHelper;
import com.qa.framework.library.base.StringHelper;
import com.qa.framework.library.base.XMLHelper;
import org.apache.log4j.Logger;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kcgw001 on 2016/4/26.
 */
public class TestngXmlGenerator {
    private final static Logger logger = Logger.getLogger(TestngXmlGenerator.class);

    private static List<Map<String, Object>> classListMap = new ArrayList<Map<String, Object>>();
    private static List<Map<String, Object>> methodListMap = new ArrayList<Map<String, Object>>();

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) {
        autoGenerate("D:\\git\\web_ui_automation\\src","D:\\git\\web_ui_automation\\test-xml\\","1","classes");
        autoGenerate("D:\\git\\web_ui_automation\\src","D:\\git\\web_ui_automation\\test-xml\\","11","classes");
        autoGenerate("D:\\git\\web_ui_automation\\src","D:\\git\\web_ui_automation\\test-xml\\","11","methods");
    }

    public static void autoGenerate(String testCasePath,String outputPath,String threadCnt,String parallelType){
        classListMap.clear();
        methodListMap.clear();
        List<String> files = IOHelper.listFilesInDirectoryRecursive(testCasePath,"*.java");
        String className = null;
        String packageName = null;
        for (String file : files) {
            boolean isMatchedFile = false;
            List<String> lines = IOHelper.readLinesToList(file);
            List<String> noBlankLines = new ArrayList<>();
            for (String line : lines) {
                if(!(line.trim().equals("") || line.trim().startsWith("//"))){
                    noBlankLines.add(line);
                }
            }
            String contents = IOHelper.readFileToString(file);
            for (String line : noBlankLines) {
                if(contents != null && contents.lastIndexOf("@Test") > 0 && line.lastIndexOf("class") > 0 && line.lastIndexOf("extends") > 0 && line.lastIndexOf("TestCaseBase") > 0){
                    className = StringHelper.getBetweenString(line,"class","extends").trim();
                    isMatchedFile = true;
                    break;
                }
            }
            if(isMatchedFile){
                Map<String, Object> classMap = new HashMap<>();
                Map<String, Object> methodMap = new HashMap<>();
                int index = 0;
                for (String line : noBlankLines) {
                    if (StringHelper.startsWithIgnoreCase(line.trim(), "package ")) {
                        packageName = StringHelper.getBetweenString(line,"package ",";").trim();
                        classMap.put("className",className);
                        classMap.put("packageName",packageName);
                    }
                    if (StringHelper.startsWithIgnoreCase(line.trim(), "@Test")) {
                        methodMap.put("className",className);
                        methodMap.put("packageName",packageName);
                        String methodLine = noBlankLines.get(index+1);
                        String methodName = StringHelper.getBetweenString(methodLine,"void ","(").trim();
                        methodMap.put("methodName",methodName);
                    }
                    index++;
                }
                classListMap.add(classMap);
                methodListMap.add(methodMap);
            }
        }

        XMLHelper xml = new XMLHelper();
        xml.createDocument();
        Element root = xml.createDocumentRoot("suite");
        xml.addAttribute(root,"name",parallelType+"_"+threadCnt);
        xml.addAttribute(root,"thread-count",threadCnt);
        xml.addAttribute(root,"parallel","tests");
        xml.addAttribute(root,"verbose","1");
        Element listeners = xml.addChildElement(root,"listeners");
        Element listener = xml.addChildElement(listeners,"listener");
        xml.addAttribute(listener,"class-name","com.qa.framework.testnglistener.RetryListener");
        if(parallelType.equalsIgnoreCase("classes")){
            for(Map<String, Object> classMap : classListMap){
                Element test = xml.addChildElement(root,"test");
                xml.addAttribute(test,"name",classMap.get("className").toString());
                xml.addAttribute(test,"timeout","600000");
                Element classes = xml.addChildElement(test,"classes");
                Element cls = xml.addChildElement(classes,"class");
                xml.addAttribute(cls,"name",classMap.get("packageName").toString()+"."+classMap.get("className").toString());
            }
        }
        else
        {
            for(Map<String, Object> classMap : methodListMap){
                Element test = xml.addChildElement(root,"test");
                xml.addAttribute(test,"name",classMap.get("className").toString());
                xml.addAttribute(test,"timeout","600000");
                Element classes = xml.addChildElement(test,"classes");
                Element cls = xml.addChildElement(classes,"class");
                xml.addAttribute(cls,"name",classMap.get("packageName").toString()+"."+classMap.get("className").toString());
                Element methods = xml.addChildElement(cls,"methods");
                Element include = xml.addChildElement(methods,"include");
                xml.addAttribute(include,"name",classMap.get("methodName").toString());
            }
        }
        xml.saveTo(outputPath+parallelType+"_"+threadCnt+".xml");
    }


}