package com.eshanren.controller;

import com.eshanren.config.BasePara;
import com.jfinal.core.Controller;

/**
 * Created by Administrator on 2019/10/14.
 */
public class BaseController extends Controller implements BasePara{

    /**
     * 页码
     */
    protected int pageNumber = 1 ;

    /**
     * 每页数量
     */

    protected int pageSize = 15;

    protected int pageSize() {
        return getInt("pageSize",15);
    }

    protected int pageNumber(){
        return getInt("pageNumber",1);
    }

    /**************************************
     *               返回值操作
     **************************************/

    /**
     * 结果判断
     * @param result
     * @param message
     */
    protected void setResult(boolean result, String message) {
        if (result) {
            successResult(message + "成功");
        } else {
            failResult(message + "失败");
        }
    }

    /**
     * 成功请求
     * @param message
     */
    protected void successResult(String message) {
        jsonResult(CODE_SUCCESS,message,null);
    }

    /**
     * 操作成功返回
     */
    protected void successResult() {
        jsonResult(CODE_SUCCESS,"操作成功",null);
    }

    /**
     * 操作失败返回
     */
    protected void failResult() {
        jsonResult(CODE_FAIL,"操作失败",null);
    }

    /**
     * 失败请求
     * @param error
     */
    protected void failResult(String error) {
        jsonResult(CODE_FAIL,error,null);
    }

    /**
     * json结果返回
     * @param code
     * @param message
     * @param error
     */
    protected void jsonResult(Integer code, String message, String error) {
        setAttr(CODE,code);
        setAttr(MESSAGE,message);
        setAttr(ERROR,error);
        renderJson();
    }
}
