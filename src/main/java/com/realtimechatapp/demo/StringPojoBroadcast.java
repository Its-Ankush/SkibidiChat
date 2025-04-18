package com.realtimechatapp.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StringPojoBroadcast {

    private final static ObjectMapper objMap = new ObjectMapper();

    BroadcastedMessage message;

    public String stringMessage(String username, String message, String history) throws JsonProcessingException {
        this.message = new BroadcastedMessage(username, message, history);
        return objMap.writeValueAsString(this.message);

    }


}
