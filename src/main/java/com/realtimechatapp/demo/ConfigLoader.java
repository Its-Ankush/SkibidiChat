package com.realtimechatapp.demo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Base64;

public class ConfigLoader {

    private static ConfigLoader configLoader;
    private Properties appProps;


    public static ConfigLoader getConfigLoader() throws IOException {
        if(configLoader==null){
            configLoader=new ConfigLoader();
        }
        return configLoader;
    }

    private ConfigLoader() throws IOException {
        InputStream is = ConfigLoader.class.getClassLoader().getResourceAsStream("application.properties");
        appProps = new Properties();
        try {
            appProps.load(is);
        } catch (IOException | NullPointerException e) {
            throw new IOException("application.properties file is not found");
        }

    }
    public int getJettyPort(){

        return Integer.parseInt(appProps.getProperty("jetty.port","8090"));
    }

    public int getJWTCookieMaxAge(){
        return Integer.parseInt(appProps.getProperty("jwt.cookie.maxage","86400"));
    }
    public String getMongoUrl(){
        return appProps.getProperty("mongo.url","mongodb://localhost:27017/?serverSelectionTimeoutMS=2000");
    }

    public int getRedisPort(){
        return Integer.parseInt(appProps.getProperty("redis.port","6379"));
    }

    public int getNumHistoryMessages(){
        return Integer.parseInt(appProps.getProperty("history.num.messages","100"));
    }

    public String secret(){
        String b64encoded_jwt  = null;
        try {
            b64encoded_jwt = System.getenv("CADDY_JWTAUTH_SIGN_KEY");
        } catch (NullPointerException e) {
            throw new NullPointerException("User environment variable by the name CADDY_JWTAUTH_SIGN_KEY is not set");
        }
        byte[] decoded = Base64.getDecoder().decode(b64encoded_jwt);
        String decodedString = new String(decoded);
        return  decodedString;
    }

}
