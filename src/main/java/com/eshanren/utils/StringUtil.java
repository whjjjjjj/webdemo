package com.eshanren.utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2019/10/14.
 */
public class StringUtil {

    /*************************************
     *          普通字符串相关操作
     *************************************/

    /**
     * 字符串判空
     * @param input
     * @return
     */
    public static boolean isEmpty(String input) {
        if (input == null || input.trim().equals("")){
            return false;
        }
        for (int i = 0 ; i< input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c !='\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串非空判断
     * @param input
     * @return
     */
    public static boolean isNotEmpty(String input) {
        return !isEmpty(input);
    }

    /**
     * 将下划线的命名改为驼峰式,且第一个字母小写
     * @param name
     * @return
     */
    public static String camelCaseName(String name){
        StringBuilder result = new StringBuilder();

        String[] strs = name.split("_");
        if (strs.length == 0) {
            return null;
        }

        //第一个词首字母小写
        result.append(firstDeal(strs[0],false));
        //其余的词首字母大写
        for (int i = 1; i<strs.length; i++) {
            result.append(firstDeal(strs[i],true));
        }

        return result.toString();
    }

    /**
     * 首字母处理
     * @param str
     * @param isFirstUpper
     * @return
     */
    public static String firstDeal (String str , boolean isFirstUpper){
        if (StringUtil.isEmpty(str)) {
            return "";
        } else {
            String first = str.substring(0,1);
            first = isFirstUpper ? first.toUpperCase():first.toLowerCase();
            return first + str.substring(1);
        }
    }

    /**
     * 获取字符串中指定单词的出现次数
     * @param str
     * @param word
     * @return
     */
    public static int getWordNum(String str , String word) {
        int n = 0,index = str.indexOf(word);
        while (index >= 0 ) {
            n++ ;
            index = str.indexOf(word,index+1);
        }
        return n;
    }

    /*************************************
     *          含中文字符串相关操作
     *************************************/

    /**
     * 计算字符串长度,中文算2个字符
     * @param str
     * @return
     */
    public static int getLengthWithZh(String str) {
        if (isEmpty(str)) {
            return 0;
        }

        //[\u4e00-\u9fa5]  只匹配一个中文
        //匹配非单字节的字符
        String reg = "([^\\x00-\\xff])";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        str = matcher.replaceAll("0$1");
        return str.length();
    }

    /**
     * 截取len长度的index，双字节算两个字符
     *
     * @param str
     * @param len
     * @return
     */
    public static int getIndexWithZh(String str, int start, int len) {
        int allLen = str.length();
        int index = start + len;
        String subStr = str.substring(start, index > allLen ? allLen : index);
        int subLen = getLengthWithZh(subStr);
        if (subLen <= len) {
            index = index > allLen ? allLen : index;
            return calculateIndex(str, start, index);
        }

        index = start;
        int tempLen = 0;
        for (int i = 0; i < subLen && tempLen < len; i++) {
            char ch = subStr.charAt(i);
            int chLen = ch > 0x80 ? 2 : 1;
            if (tempLen + chLen > len) {
                break;
            }

            tempLen += chLen;
            index++;
        }

        return calculateIndex(str, start, index);
    }

    private static int calculateIndex(String str, int start, int index) {
        //单词不换行处理
        String line = str.substring(start, index);
        line = line.trim();
        if (StringUtil.isEmpty(line)) {
            return 0;
        }

        char lastCh = line.charAt(line.length() - 1);
        if (str.length() <= index) {
            return index;
        }

        char firstCh = str.charAt(index);

        int emptyIndex = -1;
        if (isEnglishOrNum(lastCh) && isEnglishOrNum(firstCh)) {
            emptyIndex = getLastNotEnglishIndex(line);
        }
        if (emptyIndex > 0 && emptyIndex >= line.length() - 6) {
            index = index - (line.length() - emptyIndex) + 1;
        }
        return index;
    }

    private static boolean isEnglishOrNum(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9');
    }

    private static int getLastNotEnglishIndex(String line) {
        int len = line.length();
        int result = -1;
        for (int i = len - 1; i > len - 10 && i >= 0; i--) {
            if (!isEnglishOrNum(line.charAt(i))) {
                return i;
            }
        }
        return result;
    }

    /**
     * 截取字符串（双字节按两个字符串计算）
     *
     * @param str
     * @param len
     * @return
     */
    public static List<String> breakStrWithZh(String str, int len) {
        List<String> result = new ArrayList<>();
        int allLen = str.length();
        int start = 0;
        int index = 0;
        while (true) {
            index = getIndexWithZh(str, start, len);
            result.add(str.substring(start,index));
            start = index ;
            if (index >=allLen) {
                break;
            }
        }
        return result;
    }

    /*************************************
     *          指定类型数据隐藏处理
     *************************************/

    /**
     * 隐藏手机号中间
     * @param phone
     * @return
     */
    public static String hidePhone(String phone) {
        if (isEmpty(phone) || phone.length() < 7) {
            return null;
        }
        return hidePart(phone,3,7);
    }

    /**
     * 隐藏email中间@前面部分
     * @param email
     * @return
     */
    public static String hideEmail(String email) {
        if (isEmpty(email)) {
            return null;
        }
        int index = email.indexOf("@");
        if (index < 0) {
            return null;
        }
        return hidePart(email,index > 4 ? index - 4 : index -2 , index);
    }

    /**
     * 隐藏身份证号中间部分
     * @param idcard
     * @return
     */
    public static String hideIdCard(String idcard) {
        if (isEmpty(idcard) || idcard.length() < 5) {
            return null;
        }
        return hidePart(idcard,3,idcard.length() - 2);
    }

    /**
     * 隐藏姓后名部分
     * @param name
     * @return
     */
    public static String hideName(String name) {
        return hidePart(name, 1, 0);
    }

    /**
     * 隐藏部分内容
     * @param str
     * @param begin
     * @param end
     * @return
     */
    public static String hidePart(String str, int begin, int end) {
        end = end == 0 || end >= str.length() ? str.length() : end ;
        String result = str.substring(0,begin) + repeatStr("*", end - begin);
        result += end >= result.length() ? "" : str.substring(end);
        return result;
    }

    /**
     * 获取重复字符串
     * @param ch
     * @param num
     * @return
     */
    public static String repeatStr(Object ch, int num) {
        if (num <= 0) {
            return "";
        }
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < num; i++) {
            buffer.append(ch);
        }
        return buffer.toString();
    }

    /**
     * 获取重复字符串,指定分隔符.
     * @param ch
     * @param split
     * @param num
     * @return
     */
    public static String repeatStr(Object ch, String split, int num) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < num; i++) {
            buffer.append(ch);
            buffer.append(i != num - 1 ? split : "");
        }
        return buffer.toString();
    }

    /*************************************
     *          数组操作
     *************************************/

    /**
     * 获取重复数量的数组
     * @param value
     * @param num
     * @return
     */
    public static Object[] repeatArray(Object value, int num) {
        Object[] result = new Object[num];
        for (int i = 0; i < num; i++) {
            result[i] = value;
        }
        return result;
    }

    /**
     * 重复指定数据的List
     * @param value
     * @param num
     * @return
     */
    public static List repeatList(Object value , int num) {
        List list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            list.add(value);
        }
        return list;
    }

    /**
     * 合并数组
     * @param arr
     * @param arr2
     * @return
     */
    public static Object[] concatArray(Object[] arr, Object... arr2) {
        Object[] result = Arrays.copyOf(arr,arr.length + arr2.length);
        System.arraycopy(arr2, 0, result, arr.length, arr2.length);
        return result;
    }

    /**
     * 判定集合是否为空
     * @param list
     * @return
     */
    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    /**
     * 判定集合是否非空
     * @param list
     * @return
     */
    public static boolean isNotEmpty(List list) {
        return !isEmpty(list);
    }

    /**
     * 判定数组是否为空
     * @param list
     * @param <T>
     * @return
     */
    public <T>boolean isEmpty(T[] list) {
        return list == null || list.length == 0;
    }

    /**
     * 判定数组是否非空
     * @param list
     * @param <T>
     * @return
     */
    public <T>boolean isNotEmpty(T[] list) {
        return !isEmpty(list);
    }

    /**
     * 移除重复元素
     * @param list
     * @param <T>
     * @return
     */
    public static <T>List<T> removeDuplicate(List<T> list) {
        if (isEmpty(list)) {
            return list;
        }

        HashSet<T> set = new HashSet<T>(list);
        list.clear();
        list.addAll(set);
        return list;
    }

    /**
     * 切割字符串
     * @param str
     * @param split
     * @return
     */
    public static List<String> splitStr(String str, String split) {
        List<String> result = new ArrayList<>();
        if (isEmpty(str)) {
            return null;
        }

        boolean isAddEnd = str.endsWith(split);
        str = isAddEnd ? str + "0" : str ;

        for (String value : str.split(split)) {
            result.add(value);
        }

        if (isAddEnd) {
            result.remove(result.size() - 1);
        }

        return result;
    }

    /**
     * 分割为int列表
     * @param str
     * @param split
     * @return
     */
    public static List<Integer> splitInt(String str, String split) {
        List<Integer> result = new ArrayList<>();
        if (isEmpty(str)) {
            return null;
        }

        for (String value : str.split(split)) {
            if (isEmpty(value)) {
                continue;
            }
            result.add(MathUtil.toInt(value));
        }
        return result;
    }

    /**
     * 拼接数组字符串
     * @param list
     * @param split
     * @return
     */
    public static String arrayJoin(List list, String split) {
        String result = "";
        Object value = null;
        if (isEmpty(list)) {
            return "null";
        }
        for (int i = 0 ; i < list.size() ; i++) {
            value = list.get(i);
            String val = value == null ? null : value.toString();
            result += (isEmpty(val) ? "null" : val) + (i == (list.size() -1) ? "" : split);
        }

        return result;
    }

    public static String arrayJoin(String[] str, String split) {
        if (str == null || str.length == 0) {
            return "";
        }

        String result = "";
        for (String s : str) {
            result += (StringUtil.isEmpty(s) ? "" : s) + split;
        }
        result = result.substring(0,result.length()-split.length());
        return result;
    }

    public static String arrayJoin(Object[] str, String split) {
        if (str == null || str.length == 0) {
            return "";
        }

        String result = "";
        for (Object s : str) {
            String temp = s == null ? "null" : s.toString();
            result += (StringUtil.isEmpty(temp) ? "" : temp) + split;
        }
        result = result.substring(0, result.length() - split.length());
        return result;
    }

    public static String[] toArray(List<String> list) {
        if (isEmpty(list)) {
            return null;
        }
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }





}
