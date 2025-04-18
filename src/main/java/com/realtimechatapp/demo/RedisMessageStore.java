package com.realtimechatapp.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.websocket.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.resps.StreamEntry;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RedisMessageStore {

    private final JedisPooled jedis;
    private final StringPojoBroadcast stringPojoBroadcast;
    private static final ExecutorService historyExecutor = Executors.newFixedThreadPool(3);
    private static final Logger LOG = LoggerFactory.getLogger(RedisMessageStore.class);


    public RedisMessageStore(RedisConnection redisConnection) {
        this.jedis = redisConnection.getJedis();
        this.stringPojoBroadcast = new StringPojoBroadcast();

    }

    public void addMessage(String username, String message) {
        Map<String, String> hm = new HashMap<>();
        hm.put("username", username);
        hm.put("message", message);
        jedis.xadd("globalchat", (StreamEntryID) null, hm);
    }

    public void showHistory(Session session) throws InterruptedException, IOException {
        List<StreamEntry> history = jedis.xrevrange("globalchat", (StreamEntryID) null, null, ConfigLoader.getConfigLoader().getNumHistoryMessages());
        historyExecutor.submit(() -> {
            try {
                for (StreamEntry eachHistoryMessage : history) {
                    String username = eachHistoryMessage.getFields().get("username");
                    String message = eachHistoryMessage.getFields().get("message");
                    String toSend = stringPojoBroadcast.stringMessage(username, message, "true");
                    session.getAsyncRemote().sendText(toSend);
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public static void destroy() {
        historyExecutor.shutdown();
        LOG.info("Closing the history executor which runs asynchronously and shows history {}",historyExecutor.isShutdown());
    }

}
