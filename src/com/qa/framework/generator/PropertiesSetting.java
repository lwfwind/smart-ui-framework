package com.qa.framework.generator;

import com.qa.framework.library.base.StringHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class PropertiesSetting {

    /**
     * @param args the input arguments
     */
    public static void main(String[] args) {
        autoSetting(args);
    }

    public static void autoSetting(String[] args) {
        String path = null;
        if(args.length > 0){
            path = args[0];
        }
        else
        {
            path = System.getProperty("user.dir");
        }
        final File propsFile = new File(path + File.separator, "config.properties");
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(propsFile));
            for (String arg : args) {
                List<String> argList = StringHelper.getTokensList(arg, "=");
                props.put(argList.get(0), argList.get(1));
            }
            props.store(new FileOutputStream(propsFile), "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
