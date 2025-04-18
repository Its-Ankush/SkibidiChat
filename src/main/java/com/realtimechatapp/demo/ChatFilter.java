package com.realtimechatapp.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ChatFilter extends BaseFilter {

    private final static ObjectMapper objMap = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(ChatFilter.class);


    public void punish(HttpServletResponse resp) throws IOException {
        resp.setStatus(401);
        objMap.writeValue(resp.getWriter(), new GenericJSONResponsePOJO("Well well well, look what the user thinks"));

    }

    public void unAuthRequest(ServletRequest var1, ServletResponse var2, FilterChain var3) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) var1;
        HttpServletResponse resp = (HttpServletResponse) var2;
        LOG.info("User is not authorized to view chat page. Redirecting to login page");
        resp.sendRedirect("login");

//        punish(resp);


    }

    @Override
    public void authRequest(ServletRequest var1, ServletResponse var2, FilterChain var3) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) var1;
        HttpServletResponse resp = (HttpServletResponse) var2;

        var3.doFilter(var1, var2);

    }


}
