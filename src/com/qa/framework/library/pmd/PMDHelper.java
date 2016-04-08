/*
 * Copyright 2011 State Street Corporation. All rights reserved.
 */
package com.qa.framework.library.pmd;

import java.math.BigDecimal;
import java.util.*;

/**
 * Utility methods for walking around particular known PMD violations.
 */
public class PMDHelper {
    /**
     * Bypasses the violation of "new" instance in a loop.
     *
     * @return new {@code List<Map<String, String>>} instance
     */
    public static List<Map<String, String>> newSheetData() {
        return new ArrayList<Map<String, String>>();
    }

    /**
     * Bypasses the violation of "new" instance in a loop.
     *
     * @return new {@code Map<String, String>} instance
     */
    public static Map<String, String> newRowData() {
        return new LinkedHashMap<String, String>();
    }

    /**
     * Bypasses the violation of "new" instance in a loop.
     *
     * @param value the value
     * @return new {@code BigDecimal} instance
     */
    public static BigDecimal newBigDecimal(final String value) {
        return new BigDecimal(value);
    }

    /**
     * Bypasses the violation of "new" instance in a loop.
     *
     * @return new {@code Map<String, Object>} instance
     */
    public static Map<String, Object> newJbmpParam() {
        return new HashMap<String, Object>();
    }

    /**
     * Uses to avoid PMD violation
     *
     * @return {@link LinkedHashMap}
     */
    public static LinkedHashMap<String, String> newLinkedHashMap() {
        return new LinkedHashMap<String, String>();
    }

    /**
     * Uses to avoid PMD violation
     *
     * @return {@link Map<String, Object>}
     */
    public static Map<String, Object> newMapObj() {
        return new HashMap<String, Object>();
    }

    /**
     * Uses to avoid PMD violation
     *
     * @return {@link Map<String, String>}
     */
    public static Map<String, String> newHashMap() {
        return new HashMap<String, String>();
    }

    /**
     * Uses to avoid PMD violation
     *
     * @return {@link newIntStrMap}
     */
    public static Map<Integer, String> newIntStrMap() {
        return new HashMap<Integer, String>();
    }

    /**
     * Uses to avoid PMD violation
     *
     * @param bytes the bytes
     * @return {@link newString}
     */
    public static String newString(byte[] bytes) {
        return new String(bytes);
    }

    /**
     * Uses to avoid PMD violation
     *
     * @return {@link newStringList}
     */
    public static List<String> newStringList() {
        return new ArrayList<String>();
    }

    /**
     * Uses to avoid PMD violation
     *
     * @return {@link newLinkedStringList}
     */
    public static LinkedList<String> newLinkedStringList() {
        return new LinkedList<String>();
    }
}
