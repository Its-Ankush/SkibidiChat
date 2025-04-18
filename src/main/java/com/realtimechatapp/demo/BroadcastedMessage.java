package com.realtimechatapp.demo;

public class BroadcastedMessage {

    private String username;
    private String message;
    private String history;

    public BroadcastedMessage(String username, String message, String history) {
        this.username = username;
        this.message = message;
        this.history = history;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
