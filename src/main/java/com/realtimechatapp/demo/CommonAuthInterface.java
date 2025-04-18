package com.realtimechatapp.demo;

public interface CommonAuthInterface {

//    whats common b/w hardcoded creds vs validation by mongoDB ?
//    Right thats the auth method which takes in username and pwd

    boolean signupAuth(String username, String password);

    String loginAuth(String username, String password);
}
