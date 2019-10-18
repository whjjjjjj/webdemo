package com.eshanren.handle;

import com.eshanren.utils.StringUtil;
import com.jfinal.log.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 安全HttpRequest
 * Created by WWF
 */
public class SafeHttpRequest extends HttpServletRequestWrapper {

    /**
     * 日志记录
     **/
    public static Log logger = Log.getLog(Log.class);


    /**
     * 注入关键词
     **/
    protected static final String[] SQL_INJECTION_KEYWORD = ("exec|execute|insert|drop|"
            + "master|truncate|declare|sitename|net user|xp_cmdshell|like'|exec|execute|"
            + "create|drop|grant|group_concat|column_name|information_schema.columns|table_schema|union|where"
            + "|select|delete|update|master|truncate|response|print|web.xml|passwd|root|tomcat|.ini"
    ).split("\\|");

    /**
     * 脚本过滤
     **/
    protected static final Pattern SCRIPT_PATTERN = Pattern.compile("<script.*>.*<\\/script\\s*>");

    HttpServletRequest request;

    public SafeHttpRequest(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (name.equals("image") || name.equals("idCardBack") || name.contains("image") || name.equals("headImg")) {
            return values;
        }
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanUnSafeValue(values[i]);
        }
        return encodedValues;
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (name.equals("image") || name.equals("idCardBack") || name.contains("image") || name.equals("headImg")) {
            return value;
        }
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        return cleanUnSafeValue(value);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        return cleanXSS(value);
    }

    private String cleanXSS(String value) {
        if (StringUtil.isNotEmpty(value)) {
//            value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
            value = value.replaceAll("'", "& #39;");
            value = value.replaceAll("eval\\((.*)\\)", "");
            value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
            value = value.replaceAll("script", "");
        }
        return value;
    }

    /**
     * 清除不安全的字符串
     *
     * @param value
     * @return
     */
    private String cleanUnSafeValue(String value) {
        //先验证脚本
        Matcher m = SCRIPT_PATTERN.matcher(value.toLowerCase());
        if (m.find() || value.toLowerCase(Locale.CHINESE).indexOf("script") != -1) {
//            logger.error("ip=" + HttpUtil.getIpAddr(request) + ";" + request.getRequestURI() + ";拦截原因:js注入");
            value = "";
        }
//        if (StringUtil.isNotEmpty(value)) {
//            //验证sql
//            for (int j = 0; j < SQL_INJECTION_KEYWORD.length; j++) {
//                if (value.toLowerCase().contains(SQL_INJECTION_KEYWORD[j])) {
//                    logger.error("ip=" + HttpUtil.getIpAddr(request) + ";" + request.getRequestURI() + ";拦截原因:sql注入;拦截关键词=" + SQL_INJECTION_KEYWORD[j]);
//                    value = "";
//                    break;
//                }
//            }
//        }
        value = cleanXSS(value);
        return value;
    }


}
