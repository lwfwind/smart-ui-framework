package com.qa.framework.library.base;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * General convenience methods for working with date time
 */
public class DateTimeHelper {

    private final static Logger logger = Logger
            .getLogger(DateTimeHelper.class);

    /**
     * Convert string to corresponding date format
     *
     * @param date        string date to be converted
     * @param sDateFormat specific date format
     * @return Date date
     */
    public static Date toDate(String date, String sDateFormat) {
        SimpleDateFormat format1 = new SimpleDateFormat(sDateFormat);
        format1.setLenient(false);
        try {
            return format1.parse(date);
        } catch (ParseException e) {
            logger.error(e.toString());
        }
        return null;
    }

    /**
     * Convert string to corresponding date format
     *
     * @param date string date to be converted
     * @return Date date
     */
    public static Date toDate(String date) {
        int i = 0;
        ArrayList<String> ALL_FORMAT = new ArrayList<String>();
        ALL_FORMAT.add("yyyy-MM-dd");
        ALL_FORMAT.add("yyyy MM dd");
        ALL_FORMAT.add("yyyy.MM.dd");
        ALL_FORMAT.add("yyyy/MM/dd");
        ALL_FORMAT.add("MM/dd/yyyy");
        ALL_FORMAT.add("dd/MM/yyyy");
        ALL_FORMAT.add("dd-MMM-yyyy");
        ALL_FORMAT.add("dd MMM yyyy");
        ALL_FORMAT.add("yyyyMMdd");
        ALL_FORMAT.add("MMM.dd.yyyy");
        ALL_FORMAT.add("dd-MMM-yyyy HH:mm:ss");
        ALL_FORMAT.add("dd MMM yyyy HH:mm:ss");
        ALL_FORMAT.add("yyyy-MM-dd HH:mm:ss");
        ALL_FORMAT.add("yyyy-MM-dd HH:mm");
        ALL_FORMAT.add("yyyy-MM-dd HH:mm:ss.SSS");
        ALL_FORMAT.add("EEE MMM dd HH:mm:ss zzz yyyy");
        int len = ALL_FORMAT.size();
        while (i < len) {
            for (String sDateformat : ALL_FORMAT) {
                if (toDate(date, sDateformat) != null) {
                    return toDate(date, sDateformat);
                }
                i = i + 1;
            }

        }
        return null;
    }

    /**
     * Checks if the string can be converted into Date or not
     *
     * @param str the first string can be converted into Date
     * @return true if the string can be converted into Date
     */
    public static boolean isDate(String str) {
        if (toDate(str) != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if two strings can be converted into Date are on the same day
     * ignoring time.
     *
     * @param str1 the first string can be converted into Date
     * @param str2 the second string can be converted into Date
     * @return true if they represent the same day
     */
    public static boolean isSameDay(String str1, String str2) {
        Date date1 = toDate(str1);
        Date date2 = toDate(str2);
        return DateUtils.isSameDay(date1, date2);
    }

    /**
     * Convert string to corresponding date format
     *
     * @param date        string date to be converted
     * @param sDateFormat specific date format
     * @return Date date
     */
    public static Date toDateTime(String date, String sDateFormat) {
        SimpleDateFormat format1 = new SimpleDateFormat(sDateFormat);
        format1.setLenient(false);
        try {
            return format1.parse(date);
        } catch (ParseException e) {
            logger.error(e.toString());
        }
        return null;
    }

    /**
     * Convert string to corresponding date format
     *
     * @param date string date to be converted
     * @return Date date
     */
    public static Date toDateTime(String date) {
        int i = 0;
        ArrayList<String> ALL_FORMAT = new ArrayList<String>();
        ALL_FORMAT.add("dd-MMM-yyyy HH:mm:ss");
        ALL_FORMAT.add("dd MMM yyyy HH:mm:ss");
        ALL_FORMAT.add("yyyy-MM-dd HH:mm:ss");
        ALL_FORMAT.add("yyyy-MM-dd HH:mm");
        ALL_FORMAT.add("yyyy-MM-dd HH:mm:ss.SSS");
        ALL_FORMAT.add("EEE MMM dd HH:mm:ss zzz yyyy");
        int len = ALL_FORMAT.size();
        while (i < len) {
            for (String sDateformat : ALL_FORMAT) {
                if (toDateTime(date, sDateformat) != null) {
                    return toDateTime(date, sDateformat);
                }
                i = i + 1;
            }

        }
        return null;
    }

    /**
     * Checks if two strings can be converted into Date are the same instant in
     * time.
     *
     * @param str1 the first string can be converted into Date
     * @param str2 the second string can be converted into Date
     * @return true if they represent the same time
     */
    public static boolean isSameInstant(String str1, String str2) {
        Date date1 = toDateTime(str1);
        Date date2 = toDateTime(str2);
        return DateUtils.isSameInstant(date1, date2);
    }

    /**
     * Convert Date Now to string
     *
     * @return String now string
     */
    public static String getNowString() {
        Date dt = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss");
        return format1.format(dt);
    }

    /**
     * Get Date Now
     *
     * @return Date date
     */
    public static Date now() {
        Date dt = new Date();
        return dt;
    }

    /**
     * Wait specified second
     *
     * @param Second Double
     */
    public static void wait(Double Second) {
        try {
            Thread.sleep((long) (Second * 1000));
        } catch (InterruptedException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Wait specified second
     *
     * @param intSecond int
     */
    public static void wait(int intSecond) {
        try {
            Thread.sleep((intSecond * 1000));
        } catch (InterruptedException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Returns the number of intervals between two dates.
     *
     * @param interval  The interval you want to use to calculate the differences                  between StartDate and EndDate Can take the following values: d                  - Day h - Hour n - Minute s - Second
     * @param StartDate the start date
     * @param EndDate   the end date
     * @return long int
     */
    public static int dateDiff(String interval, Date StartDate, Date EndDate) {
        long l1 = StartDate.getTime();
        long l2 = EndDate.getTime();
        long difference = l2 - l1;
        if (interval.equalsIgnoreCase("h")) {
            return (int) Math.floor((difference / 1000 / 60 / 60));
        } else if (interval.equalsIgnoreCase("n")) {
            return (int) Math.floor((difference / 1000 / 60));
        } else if (interval.equalsIgnoreCase("s")) {
            return (int) Math.floor(difference / 1000);
        } else if (interval.equalsIgnoreCase("d")) {
            return (int) Math.floor(difference / 1000 / 60 / 60 / 24);
        }
        return 0;
    }

}
