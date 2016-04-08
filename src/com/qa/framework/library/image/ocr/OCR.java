package com.qa.framework.library.image.ocr;

import com.qa.framework.config.PropConfig;
import org.jdesktop.swingx.util.OS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Ocr.
 */
public class OCR {
    private final String LANG_OPTION = "-l";
    private final String EOL = System.getProperty("line.separator");
    // private String tessPath = new File("tesseract").getAbsolutePath();
    private String tessPath = PropConfig.getTessPath();

    /**
     * Recognize text string.
     *
     * @param imageFile   the image file
     * @param imageFormat the image format
     * @return the string
     * @throws Exception the exception
     */
    public String recognizeText(File imageFile, String imageFormat)
            throws Exception {
        File tempImage = ImageIOHelper.createImage(imageFile, imageFormat);
        File outputFile = new File(imageFile.getParentFile(), "output");
        StringBuffer strB = new StringBuffer();
        List<String> cmd = new ArrayList<String>();
        if (OS.isWindowsXP()) {
            cmd.add(tessPath + "//tesseract");
            // cmd.set(tessPath + "//Tesseract-OCR");
        } else if (OS.isLinux()) {
            cmd.add("tesseract");
        } else {
            // cmd.set(tessPath + "//Tesseract-OCR");
            cmd.add(tessPath + "//tesseract");
        }
        cmd.add("");
        cmd.add(outputFile.getName());
        cmd.add(LANG_OPTION);
        // cmd.set("chi_sim");
        cmd.add("eng");
        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(imageFile.getParentFile());
        cmd.set(1, tempImage.getName());
        pb.command(cmd);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        // tesseract.exe 1.jpg 1 -l chi_sim
        int w = process.waitFor();
        // delete temp working files
        tempImage.delete();
        if (w == 0) {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new FileInputStream(outputFile.getAbsolutePath() + ".txt"),
                    "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                strB.append(str).append(EOL);
            }
            in.close();
        } else {
            String msg;
            switch (w) {
                case 1:
                    msg = "Errors accessing files. There may be spaces in your image's filename.";
                    break;
                case 29:
                    msg = "Cannot recognize the image or its selected region.";
                    break;
                case 31:
                    msg = "Unsupported image format.";
                    break;
                default:
                    msg = "Errors occurred.";
            }
            tempImage.delete();
            throw new Exception(msg);
        }
        // new File(outputFile.getAbsolutePath() + ".txt").delete();
        return strB.toString();
    }

}
