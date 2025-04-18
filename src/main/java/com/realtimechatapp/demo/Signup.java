package com.realtimechatapp.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class Signup extends OverrideCommonProperties {

    private final MongoClient mongoClient;
    private SignupDataUsernamePassword signupDataUsernamePassword;
    private final static ObjectMapper objMap = new ObjectMapper();


    public Signup() throws IOException {
        mongoClient = MongoPool.getMongoPoolInstance().getMongoClient();
    }

    private void parseSignupData(HttpServletRequest req) throws IOException {
        try {
            signupDataUsernamePassword = objMap.readValue(req.getReader(), SignupDataUsernamePassword.class);
        } catch (IOException e) {
//                        throw a message - "Tampering with the body wont work"
            throw new IOException("Tampering with the body wont work");
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StaticFileServer.serveStaticFile(resp, "signup.html");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {


        ResponseHandler responseHandler = new ResponseHandler();
        AuthServiceClass authServiceClass = new AuthServiceClass(new MongoDBValidationSignup(mongoClient));

        try {
            parseSignupData(req);
        } catch (IOException e) {
            responseHandler.sendResponse(resp, 400, e.getMessage());
        }


        String username = signupDataUsernamePassword.getUsername();
        String password = signupDataUsernamePassword.getPassword();

        String finalMessage = authServiceClass.getSignupResult(username, password);

        if (finalMessage == null) {
            responseHandler.sendResponse(resp, 200, "Good to Go");
        } else {
            responseHandler.sendResponse(resp, 401, finalMessage);
        }
        return;

    }


}
