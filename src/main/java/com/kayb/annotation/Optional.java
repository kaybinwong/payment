package com.kayb.annotation;

/**
 * @author @kaybinwong
 * @since 2016/8/25
 */
public @interface Optional {
    /**
     * 是否任何情况下都可选
     * @return optional or not
     */
    boolean any() default true;
}
