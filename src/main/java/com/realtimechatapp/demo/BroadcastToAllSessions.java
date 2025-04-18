package com.realtimechatapp.demo;

import jakarta.websocket.Session;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BroadcastToAllSessions {

    private static List<Session> listOfSessions = new CopyOnWriteArrayList<>();

    public void addSession(Session session) {
        listOfSessions.add(session);

    }

    public void broadcast(String message) {
        for (Session eachSession : listOfSessions) {
            if (eachSession.isOpen()) {
                eachSession.getAsyncRemote().sendText(message);
            }
        }

    }


}
