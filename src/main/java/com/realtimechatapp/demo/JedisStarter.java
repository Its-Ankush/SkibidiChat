package com.realtimechatapp.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.resps.StreamEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class JedisStarter {
    private final static ObjectMapper objMap = new ObjectMapper();

    public static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws InterruptedException {

//        xadd globalchat * username rohit  message "Hello this is next next message haha"
        JedisPooled jedis = new JedisPooled("localhost", 6379);
        Map<String, String> hm = new HashMap<>();
        hm.put("name", "sasquatch");
        hm.put("message", "Hello, send from jedis");


        List<StreamEntry> history = jedis.xrevrange("globalchat", (StreamEntryID) null, null, 5);

        for (int i = history.size() - 1; i >= 0; i--) {
            String username = history.get(i).getFields().get("username");
            String message = history.get(i).getFields().get("message");


            System.out.println(" username === " + username + " and his message === " + message);
        }


        Thread shutdownThreadHook = new Thread(() -> {
            executorService.shutdown();
            jedis.close();
            System.out.println("About to shut the hell down");
        });
//        shutdownThreadHook.start();
        Runtime.getRuntime().addShutdownHook(shutdownThreadHook);
    }
}
