package com.realtimechatapp.demo;

public class LoginDataUsernamePassword {

    String username;
    String password;

    public LoginDataUsernamePassword(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginDataUsernamePassword() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
