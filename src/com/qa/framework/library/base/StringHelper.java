package com.qa.framework.library.base;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * General convenience methods for working with Strings
 *
 */
public class StringHelper {
    private final static Logger logger = Logger.getLogger(StringHelper.class);

    /**
     * 判断字符串是否为空
     *
     * @param str the str
     * @return the boolean
     */
    public static boolean isEmpty(String str) {
        if (str != null) {
            str = str.trim();
        }
        return StringUtils.isEmpty(str);
    }

    /**
     * 判断字符串是否非空
     *
     * @param str the str
     * @return the boolean
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Check whether the string is Integer format or not
     *
     * @param str the str
     * @return true or false
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * Check whether the string is double format or not
     *
     * @param str the str
     * @return true or false
     */
    public static boolean isDouble(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * Check whether the string is email format or not
     *
     * @param str the str
     * @return true or false
     */
    public static boolean isEmail(String str) {
        Pattern pattern = Pattern
                .compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        return pattern.matcher(str).matches();
    }

    /**
     * Check if the string is a number or not
     *
     * @param str String to be checked
     * @return boolean boolean
     */
    public static boolean isNumber(String str) {

        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            logger.error(e.toString());
            return false;
        }

    }

    /**
     * Url encode string. url转码
     *
     * @param str the str
     * @return the string
     */
    public static String urlEncode(String str) {
        try {
            logger.info("转码前:" + str);
            String value = URLEncoder.encode(str, "UTF-8");
            logger.info("转码后:" + value);
            return value;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Url decode string.
     *
     * @param str the str
     * @return the string
     */
    public static String urlDecode(String str) {
        //logger.info("转码前:" + str);
        try {
            String value = URLDecoder.decode(str, "UTF-8");
            //logger.info("转码后:" + value);
            return value;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Change string 2 boolean boolean.
     *
     * @param string the string
     * @return the boolean
     */
    public static boolean changeString2boolean(String string) {
        return !"false".equalsIgnoreCase(string);
    }

    /**
     * Count the occurrences of the substring in string s.
     *
     * @param str string to search in. Return 0 if this is null.
     * @param sub string to search for. Return 0 if this is null.
     * @return the int
     */
    public static int countOccurrencesOf(String str, String sub) {
        if (str == null || sub == null || str.length() == 0
                || sub.length() == 0) {
            return 0;
        }
        int count = 0, pos = 0, idx = 0;
        while ((idx = str.indexOf(sub, pos)) != -1) {
            ++count;
            pos = idx + sub.length();
        }
        return count;
    }

    /**
     * This String util method removes single or double quotes from a string if
     * its quoted. for input string = "mystr1" output will be = mystr1 for input
     * string = 'mystr2' output will be = mystr2
     *
     * @param str String value to be unquoted.
     * @return value unquoted, null if input is null.
     */
    public static String unquote(String str) {
        String outputstr = null;
        if (str != null
                && ((str.startsWith("\"") && str.endsWith("\"")) || (str
                .startsWith("'") && str.endsWith("'")))) {
            outputstr = str.substring(1, str.length() - 1);
        }
        return outputstr;
    }

    /**
     * Check a String ends with another string ignoring the case.
     *
     * @param str    the str
     * @param suffix the suffix
     * @return true or false
     */
    public static boolean endsWithIgnoreCase(String str, String suffix) {

        if (str == null || suffix == null) {
            return false;
        }
        if (str.endsWith(suffix)) {
            return true;
        }
        if (str.length() < suffix.length()) {
            return false;
        } else {
            return str.toLowerCase().endsWith(suffix.toLowerCase());
        }
    }

    /**
     * Check a String starts with another string ignoring the case.
     *
     * @param str    the str
     * @param prefix the prefix
     * @return true or false
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {

        if (str == null || prefix == null) {
            return false;
        }
        if (str.startsWith(prefix)) {
            return true;
        }
        if (str.length() < prefix.length()) {
            return false;
        } else {
            return str.toLowerCase().startsWith(prefix.toLowerCase());
        }
    }

    //
    //

    /**
     * Convert an array of strings to one string.
     *
     * @param arr       the arr
     * @param separator Put the 'separator' string between each element.
     * @return string string
     */
    public static String arrayToString(String[] arr, String separator) {
        StringBuilder result = new StringBuilder();
        if (arr.length > 0) {
            result.append(arr[0]);
            for (int i = 1; i < arr.length; i++) {
                result.append(separator);
                result.append(arr[i]);
            }
        }
        return result.toString();
    }

    /**
     * Convert an list of strings to one string.
     *
     * @param list      Put an list
     * @param separator Put the 'separator' string between each element.
     * @return String string
     */
    public static String listToString(List<String> list, String separator) {
        StringBuilder result = new StringBuilder();
        for (String s : list) {
            result.append(s);
            result.append(separator);
        }
        return result.toString();
    }

    /**
     * This method is used to split the given string into different tokens at
     * the occurrence of specified delimiter
     *
     * @param str       The string that needs to be broken
     * @param delimeter The delimiter used to break the string
     * @return a string array
     */
    public static String[] getTokensArray(String str, String delimeter) {
        if (str != null) {
            return str.split(delimeter);
        }
        return new String[0];
    }

    /**
     * This method is used to split the given string into different tokens at
     * the occurrence of specified delimiter
     *
     * @param str       The string that needs to be broken
     * @param delimeter The delimiter used to break the string
     * @return a instance of java.util.List with each token as one item in list
     */
    public static List<String> getTokensList(String str, String delimeter) {
        if (str != null) {
            return Arrays.asList(str.split(delimeter));
        }
        return new ArrayList<String>();
    }

    /**
     * Gets between string.
     *
     * @param origiString the origi string
     * @param beforeStr   the before str
     * @param afterStr    the after str
     * @return the tokens list
     */
    public static String getBetweenString(String origiString, String beforeStr, String afterStr) {
        return origiString.substring(origiString.indexOf(beforeStr) + beforeStr.length(), origiString.indexOf(afterStr));
    }

    /**
     * This method can be used to trim all the String values in the string
     * array. For input {" a1 ", "b1 ", " c1"}, the output will be {"a1", "b1",
     * "c1"} Method takes care of null values
     *
     * @param values the values
     * @return A trimmed array
     */
    public static String[] trimArray(final String[] values) {
        for (int i = 0, length = values.length; i < length; i++) {
            if (values[i] != null) {
                values[i] = values[i].trim();
            }
        }
        return values;
    }

    /**
     * This method can be used to trim all the String values in the string list.
     * For input {" a1 ", "b1 ", " c1"}, the output will be {"a1", "b1", "c1"}
     * Method takes care of null values
     *
     * @param values the values
     * @return A trimmed list
     */
    public static List<String> trimList(final List<String> values) {
        List<String> newValues = new ArrayList<String>();
        for (String value : values) {
            String v = (String) value;
            if (v != null) {
                v = v.trim();
            }
            newValues.add(v);
        }
        return newValues;
    }

    /**
     * This method can be used to merge 2 arrays of string values. If the input
     * arrays are like this array1 = {"a", "b" , "c"} array2 = {"c", "d", "e"}
     * Then the output array will have {"a", "b" , "c", "d", "e"} Note This
     * takes care of eliminating duplicates and checks null values.
     *
     * @param array1 the array 1
     * @param array2 the array 2
     * @return A merged String Arrays
     */
    public static String[] mergeStringArrays(String array1[], String array2[]) {
        if (array1 == null || array1.length == 0) {
            return array2;
        }

        if (array2 == null || array2.length == 0) {
            return array1;
        }

        List<String> array1List = Arrays.asList(array1);
        List<String> array2List = Arrays.asList(array2);
        List<String> result = new ArrayList<String>(array2List);
        List<String> tmp = new ArrayList<String>(array2List);
        List<String> result2 = new ArrayList<String>(array1List);
        tmp.retainAll(array1List);
        result.removeAll(tmp);
        result2.addAll(result);
        return ((String[]) result2.toArray(new String[result2.size()]));
    }

    /**
     * Concatenate the given String arrays into one, with overlapping array
     * elements included twice.
     * The order of elements in the original arrays is preserved.
     *
     * @param array1 the first array (can be <code>null</code>)
     * @param array2 the second array (can be <code>null</code>)
     * @return the new array (<code>null</code> if both given arrays were <code>null</code>)
     */
    public static String[] concatenateStringArrays(String array1[],
                                                   String array2[]) {
        if (array1 == null || array1.length == 0) {
            return array2;
        }
        if (array2 == null || array2.length == 0) {
            return array1;
        }
        String[] newArr = new String[array1.length + array2.length];
        System.arraycopy(array1, 0, newArr, 0, array1.length);
        System.arraycopy(array2, 0, newArr, array1.length, array2.length);
        return newArr;
    }

    /**
     * get a integer array filled with random integer without duplicate [min,
     * max)
     *
     * @param min  the minimum value
     * @param max  the maximum value
     * @param size the capacity of the array
     * @return a integer array filled with random integer without dupulicate
     */
    public static int[] getRandomIntWithoutDuplicate(int min, int max, int size) {
        int[] result = new int[size];

        int arraySize = max - min;
        int[] intArray = new int[arraySize];
        // init intArray
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = i + min;
        }
        // get random integer without duplicate
        for (int i = 0; i < size; i++) {
            int c = getRandomInt(min, max - i);
            int index = c - min;
            swap(intArray, index, arraySize - 1 - i);
            result[i] = intArray[arraySize - 1 - i];
        }

        return result;
    }

    /**
     * get a random Integer with the range [min, max)
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return the random Integer value
     */
    public static int getRandomInt(int min, int max) {
        // include min, exclude max
        int result = min + new Double(Math.random() * (max - min)).intValue();
        return result;
    }

    private static void swap(int[] array, int x, int y) {
        int temp = array[x];
        array[x] = array[y];
        array[y] = temp;
    }

    /**
     * Remove special char string.
     *
     * @param str the str
     * @return the string
     * @throws PatternSyntaxException the pattern syntax exception
     */
    public static String removeSpecialChar(String str) throws PatternSyntaxException {
        // 只允许字母和数字 // String regEx = "[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * Capital first letter string.
     *
     * @param str the str
     * @return the string
     */
    public static String capitalFirstLetter(String str) {
        return str.toUpperCase().substring(0, 1) + str.substring(1);
    }
}
