package com.eshanren.handle;

import com.jfinal.handler.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2019/10/14.
 */
public class SafeHandler extends Handler {
    public SafeHandler() {
    }

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {

        SafeHttpRequest httpRequest = new SafeHttpRequest(request);
        if (target.contains(";jsessionid")) {
            target = target.substring(0, target.indexOf(";"));
        }

        try {
            next.handle(target, httpRequest, response, isHandled);
        } catch (Exception e) {
            next.handle("/admin/homeland", httpRequest, response, isHandled);
        }
    }
}
