package com.realtimechatapp.demo;

public class SignupDataUsernamePassword {

    String username;
    String password;


    public String getUsername() {
        return username;
    }

    public SignupDataUsernamePassword() {
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


    public SignupDataUsernamePassword(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
