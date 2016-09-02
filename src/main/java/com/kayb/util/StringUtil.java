package com.kayb.util;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String Util base on common lang3
 * @author @kaybinwong
 * @since 2016/8/25
 */
@Slf4j
public class StringUtil {

    public static String stream2String(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i;
        while ((i = in.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

    /**
     * 根据UnicodeBlock方法判断中文标点符号
     *
     * @param c
     * @return
     */
    public static boolean isChinesePunctuation(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
                || ub == Character.UnicodeBlock.VERTICAL_FORMS) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符是否是中文
     *
     * @param c 字符
     * @return 是否是中文
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否是乱码
     *
     * @param strName 字符串
     * @return 是否是乱码
     */
    public static boolean isMessyCode(String strName) {
        Pattern p = Pattern.compile("\\s*|t*|r*|n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
            }
        }
        float result = count / chLength;
        if (result > 0.4) {
            return true;
        } else {
            return false;
        }

    }

    public static String hideName(String username) {
        return hideName(username, "*");
    }

    public static String hideName(String username, String symbol) {
        int len;
        if (isEmpty(username)
                || (len = username.length()) == 1
                || isMessyCode(username)) {
            return username;
        }
        String first = username.substring(0, 1);
        String last = username.substring(len - 1);
        return len == 2 ? first + symbol : first + symbol + last;
    }

    public static String[] getStringList(String... stringList) {
        return stringList;
    }

    public static boolean isEmpty(String str) {
        return (str == null || str.trim().length() == 0);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static Date dateFromIntString(String str) {
        if (str == null || str.length() == 0)
            return null;
        Integer time = Integer.parseInt(str);
        return new Date(time.longValue() * 1000);
    }

    // 2011-07-19T00:00:00+08:00
    public static Date dateFromString(String str) {
        try {
            SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+08:00'");
            Date date = myFormatter.parse(str);
            return date;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }


    }

    public static int intFromString(String str) {
        if (str == null || str.trim().length() == 0)
            return 0;

        if (!str.matches("[0-9]*"))
            return 0;

        Integer i = Integer.parseInt(str);
        return i.intValue();
    }

    public static double doubleFromString(String str) {
        if (str == null || str.length() == 0)
            return -1;

        str = str.replaceAll("\\-", "");
        if (str.length() == 0) {
            return -1;
        }

        Double i = Double.parseDouble(str);
        return i.doubleValue();
    }

    public static List<String> stringToList(String strings) {
        if (strings == null)
            return null;

        String[] list = strings.split(",");
        if (list == null || list.length == 0)
            return null;

        List<String> stringList = new ArrayList<String>();
        Collections.addAll(stringList, list);
//		for (int i=0; i<list.length; i++)
//			stringList.add(list[i]);

        return stringList;
    }

    public static boolean booleanFromString(String isPostString) {
        if (isPostString == null)
            return false;

        if (isPostString.equalsIgnoreCase("no"))
            return false;
        else
            return true;
    }

    public static boolean isValidMail(String mail) {
        if (null == mail) return false;

        int length = mail.length();
        if (length < 10) {
            return false;
        }
        String retMail = "^[a-zA-Z0-9_.\\-]{1,}@[a-zA-Z0-9_.\\-]{1,}\\.[a-zA-Z0-9_\\-.]{1,}$";

        if (Pattern.matches(retMail, mail)) {
            return true;
        } else {
            return false;
        }
    }

    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }


    /**
     * 把sa与sb中字符交替合并为新的字符串，剩下的直接并入结果字符串
     * <p>
     * 如：
     * abc  + def  ==> adbecf
     * abcd + ef   ==> aebfcd
     * ab   + cdef ==> acbdef
     */
    public static String intersetTwoStrings(String sa, String sb) {

        if (sa == null)
            return sb;
        if (sb == null)
            return sa;

        char[] sac = sa.toCharArray();
        char[] sbc = sb.toCharArray();
        char[] rc = new char[sac.length + sbc.length];

        int minLen = sac.length <= sbc.length ? sac.length : sbc.length;
        int i = 0;
        for (; i < minLen; i++) {
            rc[i * 2] = sac[i];
            rc[i * 2 + 1] = sbc[i];
        }
        if (sac.length < sbc.length) {
            for (int j = 2 * i; j < rc.length; j++) {
                rc[j] = sbc[i + j - 2 * i];
            }
        } else if (sac.length > sbc.length) {
            for (int j = 2 * i; j < rc.length; j++) {
                rc[j] = sac[i + j - 2 * i];
            }
        }

        return new String(rc);
    }

    /**
     * intersetTwoStrings的逆操作，
     * 从sa中sb第一个字符出现的位置开始，
     * 间隔的剔除sb中的字符。如果sa中没有
     * sb，返回null。
     * <p>
     * <p>
     * 如：
     * sa = "adbecf" sb = "def" ==> abc
     * sa = "adbecf" sb = "abc" ==> def
     */
    public static String deIntersetTwoStrings(String sa, String sb) {

        if (sa == null)
            return null;
        if (sb == null)
            return sa;
        if (sa.length() <= sb.length())
            return null;

        int startIndex = sa.indexOf(sb.substring(0, 1));
        if (startIndex == -1)
            return null;

        byte[] sab = sa.getBytes();
        byte[] sbb = sb.getBytes();
        byte[] rb = new byte[sab.length - sbb.length];

        int i = 0;
        for (; i < startIndex; i++) {
            rb[i] = sab[i];
        }

        for (; i < rb.length; i++) {
            if (i - startIndex < sbb.length) {
                rb[i] = sab[startIndex + 1 + 2 * (i - startIndex)];
            } else {
                rb[i] = sab[sbb.length + i];
            }
        }

        return new String(rb);

    }

    public static List<Integer> stringArrayToIntList(String[] strings) {

        if (strings == null || strings.length == 0)
            return Collections.emptyList();

        List<Integer> retList = new ArrayList<Integer>(strings.length);
        for (int i = 0; i < strings.length; i++) {
            retList.add(Integer.parseInt(strings[i]));
        }

        return retList;

    }

    public static String getEmptyStringWhenNull(String string) {
        return (string == null) ? "" : string;
    }

    static String CHINESE_CODES = "[\u4e00-\u9fa5]";
    static String TIBET_CODES = "[\u0f00-\u0fff]";

    public static boolean isContainsChinese(String str) {
        return isContainsCharacterSet(str, CHINESE_CODES);
    }

    public static boolean isContainsTibet(String str) {
        return isContainsCharacterSet(str, TIBET_CODES);
    }

    public static boolean isContainsCharacterSet(String str, String regEx) {
        Pattern pat = Pattern.compile(regEx);

        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        int count = 0;
        while (matcher.find()) {
            flg = true;
            for (int i = 0; i <= matcher.groupCount(); i++) {
                count = count + 1;
            }
        }
//        System.out.println("共有 " + count + "个 ");
        return flg;
    }

    public static byte[] string2Bytes(String value) {
        try {
            return value.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String bytes2String(byte[] value) {
        try {
            return value == null ? null : new String(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

}
