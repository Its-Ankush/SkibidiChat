package com.realtimechatapp.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.params.XReadParams;
import redis.clients.jedis.resps.StreamEntry;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static redis.clients.jedis.StreamEntryID.XGROUP_LAST_ENTRY;

public class RedisListener {
    private final ExecutorService streamExecutor;
    private final ExecutorService broadcastExecutor;
    private final JedisPooled jedis;
    private final StringPojoBroadcast stringPojoBroadcast = new StringPojoBroadcast();
    private final BroadcastToAllSessions broadcastToAllSessions;
    private final Logger LOG = LoggerFactory.getLogger(RedisListener.class);

    public RedisListener(RedisConnection redisConnection) {
        this.jedis = redisConnection.getJedis();
        this.broadcastToAllSessions = new BroadcastToAllSessions();
        this.streamExecutor = Executors.newSingleThreadExecutor();
        this.broadcastExecutor = Executors.newFixedThreadPool(5);
    }

    public void start() {
        keepListeningForNewMessages();
    }

    public void keepListeningForNewMessages() {

        streamExecutor.submit(() -> {
            LOG.info("Stream executor started. Now listening for new messages in the stream");
            StreamEntryID id = XGROUP_LAST_ENTRY;
            while (!Thread.currentThread().isInterrupted()) {
                XReadParams params = XReadParams.xReadParams().block(0).count(1);
                String key = "globalchat";
                Map<String, StreamEntryID> streams = Collections.singletonMap(key, id);
                List<Map.Entry<String, List<StreamEntry>>> abc = jedis.xread(params, streams);
                id = abc.get(0).getValue().get(0).getID();
                String message = abc.get(0).getValue().get(0).getFields().get("message");
                System.out.println("Message picked up " + message);
                String username = abc.get(0).getValue().get(0).getFields().get("username");
                try {
                    String toSend = stringPojoBroadcast.stringMessage(username, message, "false");
                    broadcastExecutor.submit(() -> {
                        broadcastToAllSessions.broadcast(toSend);
                    });

                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

            }
            ;

        });

    }

    public void destroy() {
        streamExecutor.shutdown();
        LOG.info("Shutting down the main executor which loops for new messages. Status of shutdown - {}",streamExecutor.isShutdown());
        broadcastExecutor.shutdown();
        LOG.info("Shutting down the broadcaster executor which loops over all the sessions. Status of shutdown - {}",broadcastExecutor.isShutdown());

    }
}
