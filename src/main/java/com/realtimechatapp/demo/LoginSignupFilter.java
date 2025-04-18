package com.realtimechatapp.demo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


public class LoginSignupFilter extends BaseFilter {
    @Override
    public void unAuthRequest(ServletRequest var1, ServletResponse var2, FilterChain var3) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) var1;
        HttpServletResponse resp = (HttpServletResponse) var2;
        /*go to class and serve static page*/
        var3.doFilter(var1, var2);
    }

    @Override
    public void authRequest(ServletRequest var1, ServletResponse var2, FilterChain var3) throws IOException {
        HttpServletRequest req = (HttpServletRequest) var1;
        HttpServletResponse resp = (HttpServletResponse) var2;

        String path = req.getServletPath();

        resp.sendRedirect("chat");

    }


}
