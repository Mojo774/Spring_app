package com.example.demo.util;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirectInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            String args = request.getQueryString() != null ? request.getQueryString() : "";

            String url;
            if (args.equals("")){
                url = request.getRequestURI().toString();
            } else {
                url = request.getRequestURI().toString() + "?" + args;
            }
            response.setHeader("Turbolinks-Location", url);
        }
    }
}
