package com.qa.framework.library.base;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Static functions for reading and writing text files as a single string, and
 * treating a file as an ArrayList.
 *
 * @author a106403
 */
public class TextFileHelper extends ArrayList<String> {

    private final static Logger logger = Logger
            .getLogger(TextFileHelper.class);

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Read a file, split by any regular expression:
     *
     * @param fileName the file name
     * @param splitter the splitter
     */
    public TextFileHelper(String fileName, String splitter) {
        super(Arrays.asList(read(fileName).split(splitter)));
        // Regular expression split() often leaves an empty
        // String at the first position:
        if (get(0).equals("")) {
            remove(0);
        }

    }

    /**
     * Normally read by lines:
     *
     * @param fileName the file name
     */
    public TextFileHelper(String fileName) {
        this(fileName, "\n");
    }

    /**
     * Read a file as a single string:
     *
     * @param fileName the file name
     * @return the string
     */
    public static String read(String fileName) {
        StringBuilder sBuilder = new StringBuilder();
        try {
            BufferedReader readerIn = new BufferedReader(new FileReader(
                    new File(fileName).getAbsoluteFile()));
            try {
                String str;
                while ((str = readerIn.readLine()) != null) {
                    sBuilder.append(str);
                    sBuilder.append("\n");
                }
            } finally {
                readerIn.close();
            }
        } catch (IOException e) {
            logger.error(e.toString(), e);
        }
        return sBuilder.toString();
    }

    /**
     * Write a single file in one method call:
     *
     * @param fileName the file name
     * @param text     the text
     */
    public static void write(String fileName, String text) {
        try {
            PrintWriter out = new PrintWriter(
                    new File(fileName).getAbsoluteFile());
            try {
                out.print(text);
            } finally {
                out.close();
            }
        } catch (IOException e) {
            logger.error(e.toString(), e);
        }
    }

    /**
     * Write.
     *
     * @param fileName the file name
     */
    public void write(String fileName) {
        try {
            final PrintWriter out = new PrintWriter(
                    new File(fileName).getAbsoluteFile());
            try {
                for (String item : this) {
                    out.println(item);
                }
            } finally {
                out.close();
            }
        } catch (IOException e) {
            logger.error(e.toString(), e);
        }
    }
}