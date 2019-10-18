package com.eshanren.config;

/**
 * Created by Administrator on 2019/10/15.
 */
public interface BasePara {

    /**
     * 提示信息
     */
    String MESSAGE = "message";

    /**
     * 错误信息
     */
    String ERROR = "error";

    /**
     * 参数
     */
    String PARA = "para";

    /**
     * 跳转路由
     */
    String URL = "url";

    /**
     * 请求结果代码
     */
    String CODE = "code";

    /**
     * 请求成功
     */
    Integer CODE_SUCCESS = 1 ;

    /**
     * 请求失败
     */
    Integer CODE_FAIL = 0 ;

    /**
     * 服务器错误
     */
    Integer CODE_ERROR = 500;

    /**
     * 权限校验失败
     */
    Integer CODE_AUTH = 403;

}
