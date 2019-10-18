package com.eshanren.handle;

import com.eshanren.utils.StringUtil;
import com.jfinal.handler.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * FakeStaticHandler.
 */
public class FakeStaticHandler extends Handler {

    private String viewPostfix;

    public FakeStaticHandler() {
        viewPostfix = ".html";
    }

    public FakeStaticHandler(String viewPostfix) {
        if (StringUtil.isEmpty(viewPostfix)) {
            throw new IllegalArgumentException("viewPostfix can not be blank.");
        }
        this.viewPostfix = viewPostfix;
    }

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        int index = target.lastIndexOf(viewPostfix);
        if (index != -1) {
            target = target.substring(0, index);
        }
        try {
            next.handle(target, request, response, isHandled);
        } catch (Exception e) {
            next.handle("/", request, response, isHandled);
        }
    }
}