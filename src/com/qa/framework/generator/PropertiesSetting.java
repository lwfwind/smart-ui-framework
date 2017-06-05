package com.qa.framework.generator;

import com.library.common.IOHelper;
import com.library.common.StringHelper;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * The type Properties setting.
 */
public class PropertiesSetting {
    private final static Logger logger = Logger
            .getLogger(PropertiesSetting.class);

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        autoSetting(args);
    }

    /**
     * Auto setting.
     *
     * @param args the args
     */
    public static void autoSetting(String[] args) {
        List<String> configPathList = IOHelper.listFilesInDirectory(System.getProperty("user.dir"),"config.properties");
        if(configPathList.size() > 0) {
            File propsFile = new File(configPathList.get(0));
            Properties props = new Properties();
            try {
                props.load(new FileInputStream(propsFile));
                for (String arg : args) {
                    if (arg.contains("=")) {
                        List<String> argList = StringHelper.getTokensList(arg, "=");
                        props.put(argList.get(0), argList.get(1));
                    }
                }
                props.store(new FileOutputStream(propsFile), "");
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }


}
