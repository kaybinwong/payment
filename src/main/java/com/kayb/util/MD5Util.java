package com.kayb.util;

import java.security.MessageDigest;

/**
 * Md5 Util
 * @author @kaybinwong
 * @since 2016/8/25
 */
public class MD5Util {

    //十六进制下数字到字符的映射数组
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 加密inputString
     * @param inputString
     * @return
     */
    public static String generate(String inputString){
        return md5(inputString);
    }

    /**
     * 验证输入的密码是否正确
     * @param password    加密后的密码
     * @param inputString    输入的字符串
     * @return    验证结果，TRUE:正确 FALSE:错误
     */
    public static boolean validate(String password, String inputString){
        if(password.equals(md5(inputString))){
            return true;
        } else{
            return false;
        }
    }

    /**
     * 对字符串进行MD5加密
     * @param originString
     * @return
     */
    private static String md5(String originString){
        if (originString != null){
            try{
                //创建具有指定算法名称的信息摘要
                MessageDigest md = MessageDigest.getInstance("MD5");
                //使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                byte[] results = md.digest(originString.getBytes());
                //将得到的字节数组变成字符串返回
                String resultString = byteArrayToHexString(results);
                return resultString.toUpperCase();
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 转换字节数组为十六进制字符串
     * @param b
     * @return
     */
    private static String byteArrayToHexString(byte[] b){
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++){
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * 将一个字节转化成十六进制形式的字符串
     * @param b
     * @return
     */
    private static String byteToHexString(byte b){
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

}
