package com.realtimechatapp.demo;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


public class LoginFormClass extends OverrideCommonProperties {

    private final MongoClient mongoClient;
    private final static ObjectMapper objMap = new ObjectMapper();
    private LoginDataUsernamePassword login;


    public LoginFormClass() throws IOException {
        mongoClient = MongoPool.getMongoPoolInstance().getMongoClient();
    }

    private void parseLogin(HttpServletRequest req) throws IOException {
        try {
            login = objMap.readValue(req.getReader(), LoginDataUsernamePassword.class);
        } catch (IOException e) {
//                        throw a message - "Tampering with the body wont work"
            throw new IOException("Tampering with the body wont work");
        }
    }

//    this will map to the json payload. username and password and will help when the request is malformed or someone is trying to tamper with payload

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");

        ResponseHandler responseHandler = new ResponseHandler();
        try {
            parseLogin(req);
        } catch (IOException e) {
            responseHandler.sendResponse(resp, 400, e.getMessage());
            return;

        }
        String username = login.getUsername();
        String password = login.getPassword();

//        pass either mongo or hardcode since either of them implements the CommonAuthInterface interface
        AuthServiceClass authServiceClass = new AuthServiceClass(new MongoDBValidationSignup(mongoClient));


        String finalMessage = authServiceClass.getLoginResult(username, password);

        if (finalMessage != null) {
//                username and pwd incorrect
            responseHandler.sendResponse(resp, 401, finalMessage);
        } else {
//                correct username and pwd
            Cookie cookie = new Cookie("jwt", JwtHelper.createJWT(username));
            cookie.setPath("/");
            cookie.setHttpOnly(false);
            cookie.setMaxAge(ConfigLoader.getConfigLoader().getJWTCookieMaxAge());
            resp.addCookie(cookie);
            resp.setStatus(HttpServletResponse.SC_OK);
        }


    }

    //    This will only respond. It will not serve. Serving will be handled by JS window href
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StaticFileServer.serveStaticFile(resp, "login.html");
    }

}
