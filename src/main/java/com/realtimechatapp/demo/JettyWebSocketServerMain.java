package com.realtimechatapp.demo;

import jakarta.websocket.server.ServerEndpointConfig;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.websocket.jakarta.server.config.JakartaWebSocketServletContainerInitializer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;
public class JettyWebSocketServerMain {
    private static final Logger LOG = LoggerFactory.getLogger(JettyWebSocketServerMain.class);

    public static void main(String[] args) throws Exception {
        Server server = new Server();

//        create a connector and bind to port 8090
        ServerConnector connector = new ServerConnector(server);
        int jettyPort = ConfigLoader.getConfigLoader().getJettyPort();
        connector.setPort(jettyPort);
        server.addConnector(connector);

// allow sessions for jetty - very important for websocket

        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.setContextPath("/");

// use jakarta websocket as per the example in GitHub. Will be on /ws endpoint

        JakartaWebSocketServletContainerInitializer.configure(servletContextHandler, (context, container) ->
        {


// Add echo endpoint to server container

            ServerEndpointConfig echoConfig = ServerEndpointConfig.Builder

                    .create(WebsocketEndpointAnnotations.class, "/ws")

                    .build();

            container.addEndpoint(echoConfig);

        });

//        Creating a static class for handling all http servlet annotations because i just couldn get the annotations to work out :(
        HttpAnnotations.setClassAndPath(servletContextHandler);
        server.setHandler(servletContextHandler);

//        start looping across blocking xread
        WebsocketEndpointAnnotations.init();
//        Create MongoClient as well
        server.start();


        LOG.info("WebSocket Server started on ws://localhost:{}/ws", jettyPort);
        LOG.info("Http Server started on http://localhost:{}",jettyPort);


        Thread shutdownThreadHook = new Thread(() -> {
            try {
                WebsocketEndpointAnnotations.destroy();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                server.stop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            LOG.info("Clearing the executors and shutting down the server");
        });
        Runtime.getRuntime().addShutdownHook(shutdownThreadHook);

        server.join();

//        Lol i thought i need to create 2 connectors but looks like websocket is just an upgrade header at the end of the day. Also caddy will take care of the rest :)
    }
}
