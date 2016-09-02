package com.kayb.util;

import java.util.Random;

/**
 * @author @kaybinwong
 * @since 2016/8/25
 */
public final class RandomStrs {

    private static final String seed = "abcdefghijklmnopqrstuvwxyz0123456789";

    private static final Random random = new Random();

    /**
     * 生成随机字符串
     * @param length 长度
     * @return 长度为length的字符串
     */
    public static String generate(Integer length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(seed.length());
            sb.append(seed.charAt(number));
        }
        return sb.toString();
    }
}
