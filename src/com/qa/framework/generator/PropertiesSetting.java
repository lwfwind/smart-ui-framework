package com.qa.framework.generator;

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
        String path = null;
        if (System.getProperty("basedir") != null) {
            path = System.getProperty("basedir");
        } else {
            path = System.getProperty("user.dir");
        }
        final File propsFile = new File(path + File.separator, "config.properties");
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
