package com.realtimechatapp.demo;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;

public class HttpAnnotations {

    public static void setClassAndPath(ServletContextHandler servletContextHandler) {

        servletContextHandler.addFilter(LoginSignupFilter.class, "/login", null);
        servletContextHandler.addFilter(LoginSignupFilter.class, "/signup", null);
        servletContextHandler.addFilter(ChatFilter.class, "/chat", null);

        servletContextHandler.addServlet(LoginFormClass.class, "/login");
        servletContextHandler.addServlet(Signup.class, "/signup");
        servletContextHandler.addServlet(ChatPage.class, "/chat");


    }
}
