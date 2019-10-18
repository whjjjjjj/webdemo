package com.eshanren.utils;

import java.util.Random;

/**
 * Created by Administrator on 2019/10/14.
 */
public class MathUtil {

    /****************************************
     *              随机数
     ****************************************/

    /**
     * 六位随机数
     * @return
     */
    public static String getSixRandom() {
        return String.valueOf(getRandom(100000,999999));
    }

    /**
     * 四位随机数
     * @return
     */
    public static String getFourRandom() {
        return String.valueOf(getRandom(1000,9999));
    }

    /**
     * 获取随机数
     * @param min
     * @param max
     * @return
     */
    public static int getRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    /****************************************
     *              数据转换
     ****************************************/

    /**
     * string 转int ,指定默认值
     * @param str
     * @param defaultValue
     * @return
     */
    public static int toInt(String str, int defaultValue) {
        Integer result = stringToInt(str);
        return result == null ? defaultValue : result;
    }

    /**
     * string转int , 默认值为0
     * @param str
     * @return
     */
    public static int toInt(String str) {
        return toInt(str,0);
    }

    /**
     * string转int 实现
     * @param str
     * @return
     */
    public static Integer stringToInt(String str) {
        if (StringUtil.isNotEmpty(str)) {
            try {
                return Integer.valueOf(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * string转long 指定默认值
     * @param str
     * @param defaultValue
     * @return
     */
    public static long toLong(String str, long defaultValue) {
        Long result = stringToLong(str);
        return result == null ? defaultValue : result;
    }

    /**
     * string转long,默认值为0L
     * @param str
     * @return
     */
    public static long toLong(String str) {
        return toLong(str,0L);
    }

    /**
     * string 转long 实现
     * @param str
     * @return
     */
    public static Long stringToLong(String str) {
        if (StringUtil.isNotEmpty(str)) {
            try {
                return Long.valueOf(str);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * string转float，指定默认值
     *
     * @param str
     * @param defaultValue
     * @return
     */
    public static float toFloat(String str, float defaultValue) {
        Float result = stringToFloat(str);
        return result == null ? defaultValue : result;
    }

    /**
     * string转float，默认值0
     *
     * @param str
     * @return
     */
    public static float toFloat(String str) {
        return toFloat(str, 0F);
    }

    /**
     * string转float
     *
     * @param str
     * @return
     */
    public static Float stringToFloat(String str) {
        if (StringUtil.isNotEmpty(str)) {
            try {
                return Float.valueOf(str);
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * string转double，指定默认值
     *
     * @param str
     * @param defaultValue
     * @return
     */
    public static double toDouble(String str, double defaultValue) {
        Double result = stringToDouble(str);
        return result == null ? defaultValue : result;
    }

    /**
     * string转double，默认值为0
     *
     * @param str
     * @return
     */
    public static double toDouble(String str) {
        return toDouble(str, 0D);
    }

    /**
     * string转double，转换失败返回null
     *
     * @param str
     * @return
     */
    public static Double stringToDouble(String str) {
        if (StringUtil.isNotEmpty(str)) {
            try {
                return Double.valueOf(str);
            } catch (Exception e) {
            }
        }
        return null;
    }

    /****************************************
     *              数据校验
     ****************************************/


    /**
     * 判断数字是否为空或者0
     * @param n
     * @return
     */
    public static boolean isZero(Object n) {
        if (n == null) {
            return true;
        }

        if (n instanceof Integer) {
            return (Integer) n <= 0;
        } else if (n instanceof Long) {
            return (Long) n <= 0;
        } else if (n instanceof Float) {
            return (Float) n <= 0;
        } else if (n instanceof Double) {
            return (Double) n <= 0;
        }
        return true;
    }

    /**
     * 判断数字是否不为空且不为0
     * @param n
     * @return
     */
    public static boolean isNotZero(Object n) {
        return !isZero(n);
    }

    public static int min(int a, int b) {
        return a < b ? a : b ;
    }

    public static int max(int a, int b) {
        return a > b ? a : b;
    }

    public static int min(int... nums) {
        int result = nums[0];
        for (int n : nums) {
            result = min(result, n);
        }
        return result;
    }

    public static int max(int... nums) {
        int result = nums[0];
        for (int n : nums) {
            result = max(result, n);
        }
        return result;
    }

    public static long max(long a, long b) {
        return a > b ? a : b;
    }

    public static long max(long... nums) {
        long result = nums[0];
        for (long n : nums) {
            result = max(result, n);
        }
        return result;
    }

}
