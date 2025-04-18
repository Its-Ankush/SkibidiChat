package com.realtimechatapp.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ChatPage extends HttpServlet {

    private final static ObjectMapper objMap = new ObjectMapper();


    //    This will only respond. It will not serve. Serving will be handled by JS window href
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StaticFileServer.serveStaticFile(resp, "chat.html");
    }
}
