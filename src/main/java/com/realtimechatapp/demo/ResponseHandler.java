package com.realtimechatapp.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponseHandler {

    private final static ObjectMapper objMap = new ObjectMapper();

    public void sendResponse(HttpServletResponse resp, int errorCode, String message) throws IOException {
        resp.setStatus(errorCode);
        objMap.writeValue(resp.getWriter(), new GenericJSONResponsePOJO(message));
        return;

    }

}
