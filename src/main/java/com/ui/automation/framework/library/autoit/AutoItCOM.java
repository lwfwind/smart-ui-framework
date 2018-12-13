package com.ui.automation.framework.library.autoit;

import com.jacob.com.LibraryLoader;

import java.io.File;

/**
 * The type Auto it com.
 */
public class AutoItCOM extends AutoItXCOM {

    static {
        File file = null;
        String Jacob_path = "src" + File.separator + "main" + File.separator
                + "resources" + File.separator + "lib" + File.separator
                + "jacob";
        if (System.getProperty("os.arch").equals("x86")) {
            file = new File(Jacob_path, "jacob-1.16-M1-x86.dll");
        } else {
            file = new File(Jacob_path, "jacob-1.16-M1-x64.dll");
        }
        System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());
    }

    /**
     * Instantiates a new Auto it com.
     */
    public AutoItCOM() {
    }

}
