package com.realtimechatapp.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.websocket.*;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.ServerEndpointConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;


// JWT validation is automatically happening at caddy thank god

// Examples from https://github.com/jetty/jetty-examples/blob/12.0.x/embedded/ee10-websocket-jakarta-api/src/main/java/examples/annotated/EchoServerEndpoint.java

@ServerEndpoint(value = "/ws", configurator = WebsocketEndpointAnnotations.HandshakeConfigurator.class)
public class WebsocketEndpointAnnotations {

    private static final Logger LOG = LoggerFactory.getLogger(WebsocketEndpointAnnotations.class);
    private Session session;
    private RemoteEndpoint.Async remote;
    private static ConcurrentHashMap<Session, String> hashMap = new ConcurrentHashMap<>();
    private final BroadcastToAllSessions broadcastToAllSessions;
    private static final RedisListener redisJobs;
    private static final RedisConnection redisConnection;
    private final RedisMessageStore redisMessageStore;


//    im using static block as a constructor

    /*
     * What is shared - redisConnection, redisJobs
     * What should not be shared  - redisMessageStore, broadcastToAllSessions [they should be session specific]
     * */
    static {
        try {
            String url  = ConfigLoader.getConfigLoader().getRedisUrl();
            redisConnection = new RedisConnection(url, ConfigLoader.getConfigLoader().getRedisPort());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        redisJobs = new RedisListener(redisConnection);
    }

    public WebsocketEndpointAnnotations() {
        this.redisMessageStore = new RedisMessageStore(redisConnection);
        this.broadcastToAllSessions = new BroadcastToAllSessions();
    }

    public static void init() throws IOException {
        redisJobs.start();
        MongoPool.getMongoPoolInstance().start();


    }


//    Annotated endpoints - https://jakarta.ee/learn/docs/jakartaee-tutorial/current/web/websocket/websocket.html#_annotated_endpoints

    //    Kind of like a constructor
    /*
     * https://github.com/jetty/jetty.project/blob/ac24196b0d341534793308d585161381d5bca4ac/jetty-websocket/websocket-server/src/main/java/org/eclipse/jetty/websocket/server/HandshakeRFC6455.java#L49
     * */
    @OnOpen
    public void onWebSocketOpen(Session session, EndpointConfig config) throws IOException, InterruptedException {
        this.session = session;
        this.remote = this.session.getAsyncRemote();
        this.session.setMaxIdleTimeout(300000);
        broadcastToAllSessions.addSession(session);
        HandshakeRequest request = (HandshakeRequest) config.getUserProperties().get("jakarta.websocket.endpoint.handshake.request");
        String username = request.getHeaders().get("X-User").get(0);
//        to be used in OnMessage method because the header will not be accessible inside that OnMessage method

        hashMap.put(session, username);
        LOG.info("WebSocket Connected LESS GOOOOOOO: ");
        redisMessageStore.showHistory(session);
    }


    // i send message to my session. So this is always my own session and not others'
    @OnMessage
    public void onMessage(Session session, String message) throws JsonProcessingException {
        String username = hashMap.get(session);
        redisMessageStore.addMessage(username, message);
//        LOG.info("Echoing back text message [{}]", message);
    }


    @OnClose
    public void close(Session session,
                      CloseReason reason) {
//        Let me not remove sessions rather mark it null. As per CopyOnWriteArrayList, its better suited for reads than writes/modifications. Session object would be garbage collected anyways. This will also prevent an O(n) search lol
        hashMap.remove(session);
        this.session = null;
        this.remote = null;
        LOG.info("WebSocket Close: {} - {}", reason.getCloseCode(), reason.getReasonPhrase());
    }

    @OnError
    public void onError(Session session,
                        Throwable error) {
        /*Example taken from
         * https://jakarta.ee/learn/docs/jakartaee-tutorial/current/web/websocket/websocket.html#_the_endpoint:~:text=The%20lifecycle%20methods%20of%20the%20endpoint%20add%20and%20remove%20sessions%20to%20and%20from%20the%20queue
         * */

        LOG.info("Error : {}", error.toString());

    }

    public static class HandshakeConfigurator extends ServerEndpointConfig.Configurator {
        @Override
        public void modifyHandshake(ServerEndpointConfig sec,
                                    HandshakeRequest request,
                                    HandshakeResponse response) {


            sec.getUserProperties().put(
                    "jakarta.websocket.endpoint.handshake.request",
                    request
            );
            super.modifyHandshake(sec, request, response);
        }
    }


    public static void destroy() throws IOException {
        redisJobs.destroy();
        redisConnection.closeJedis();
        RedisMessageStore.destroy();
        MongoPool.getMongoPoolInstance().destroy();


    }

}
