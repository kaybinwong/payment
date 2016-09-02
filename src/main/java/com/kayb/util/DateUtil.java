package com.kayb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date Util
 * @author @kaybinwong
 * @since 2016/8/25
 */
public class DateUtil {


    private static final String DATE_FORMAT = "yyyyMMddHHmmss";
    public static String CHINA_TIMEZONE = "GMT+0800";

    static String rexp = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
    private static final Pattern pattern = Pattern.compile(rexp);
    public static boolean isDate(String date) {
        Matcher mat = pattern.matcher(date);
        return mat.matches();
    }

    // 转换比赛时间
    public static long dateToLong(String matchDate) {
        Date date = DateUtil.dateFromStringByFormat(matchDate, DATE_FORMAT);
        return date.getTime();
    }

    public static String currentDate() {
        Date date = new Date();
        return DateUtil.dateToStringByFormat(date, DATE_FORMAT);
    }

    public static String dateToString(Date date){
        return dateToStringByFormat(date, DATE_FORMAT);
    }

    // format example "dd/MM/yyyy-hh:mm:ss"
    public static String dateToStringByFormat(Date date, String format) {
        if (date == null || format == null)
            return null;

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

    public static String dateToChineseStringByFormat(Date date, String format) {
        if (date == null || format == null)
            return null;

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone(CHINA_TIMEZONE));
        return dateFormat.format(date);
    }

    public static Date dateFromString(String dateString){
        return dateFromStringByFormat(dateString, DATE_FORMAT);
    }

    public static Date dateFromStringByFormat(String dateString, String format) {
        if (dateString == null || dateString.length() == 0 || format == null)
            return null;

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    // timeZoneString format : GMT+0800
    public static Date dateFromStringByFormat(String dateString, String format, String timeZoneString) {
        if (dateString == null || format == null)
            return null;

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone(timeZoneString));
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String dateDescription(Date date) {
        if (date == null)
            return "(null)";
        else
            return date.toString();
    }

    public static long getCurrentTime() {
        Date date = new Date();
        return date.getTime();
    }

    public static int getCurrentSeconds() {
        return (int)(System.currentTimeMillis()/1000);
    }

    public static Date getDateOfToday(){

        TimeZone timeZone = TimeZone.getTimeZone("GMT+0800");
        Calendar now = Calendar.getInstance(timeZone);
        now.setTime(new Date());

        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);

        return now.getTime();
    }


    private static Date getGMT8Date(){

        TimeZone timeZone = TimeZone.getTimeZone("GMT+0800");
        Calendar now = Calendar.getInstance(timeZone);
        now.setTime(new Date());

        return now.getTime();
    }

    public static int calcHour(Date startDate, Date endDate) {
        if (startDate.before(endDate)) {
            long start = startDate.getTime();
            long end = endDate.getTime();
            int hours =  (int) ((end - start) / (3600L * 1000));
            return hours;
        } else {
            return -1;
        }

    }

    public static boolean isMiddleDate(int startHour, int startMinute, int endHour, int endMinute, String timeZoneString){

        TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);

        Calendar startCalendar = Calendar.getInstance(timeZone);
        Calendar endCalendar = Calendar.getInstance(timeZone);
        startCalendar.set(Calendar.HOUR_OF_DAY, startHour);
        startCalendar.set(Calendar.MINUTE, startMinute);
        endCalendar.set(Calendar.HOUR_OF_DAY, endHour);
        endCalendar.set(Calendar.MINUTE, endMinute);

        Date startDate = startCalendar.getTime();
        Date endDate = endCalendar.getTime();
        Date curDate = new Date();

//        Log.info("startDate="+startDate+",endDate="+endDate+",currentDate="+curDate);

        if (curDate.after(startDate) && curDate.before(endDate)) {
            return true;
        }

        return false;
    }

    public static int dateToInt(Date date) {
        if (date != null) {
            long time = date.getTime()/1000;
            return new Long(time).intValue();
        }
        return 0;
    }

    public static Date getDateOfToday(int hour, int minus, int second){

        TimeZone timeZone = TimeZone.getDefault();

        Calendar now = Calendar.getInstance(timeZone);
        now.setTime(new Date());

        now.set(Calendar.HOUR_OF_DAY, hour);
        now.set(Calendar.MINUTE, minus);
        now.set(Calendar.SECOND, second);
        now.set(Calendar.MILLISECOND, 0);
        return now.getTime();
    }

    public  static  boolean isTodayWeekDay(int weekDay){
        TimeZone timeZone = TimeZone.getDefault();
        Calendar now = Calendar.getInstance(timeZone);
        now.setTime(new Date());
        return  (now.get(Calendar.DAY_OF_WEEK) == weekDay);
    }

    public  static int getMonth(Date date){
        TimeZone timeZone = TimeZone.getTimeZone("GMT+0800");
        Calendar now = Calendar.getInstance(timeZone);
        now.setTime(date);
        return now.get(Calendar.MONTH);
    }

    public  static int getHour(Date date){
        TimeZone timeZone = TimeZone.getTimeZone("GMT+0800");
        Calendar now = Calendar.getInstance(timeZone);
        now.setTime(date);
        return now.get(Calendar.HOUR_OF_DAY);
    }

    public  static int getWeekday(Date date){
        TimeZone timeZone = TimeZone.getTimeZone("GMT+0800");
        Calendar now = Calendar.getInstance(timeZone);
        now.setTime(date);
        return now.get(Calendar.DAY_OF_WEEK);
    }


    public static Date getDateBeforeToday(int nDay){

        long currentTime = DateUtil.getCurrentTime();
        Date date = new Date(currentTime - nDay * 24 * 3600 * 1000);
        return date;
    }

}
