package com.realtimechatapp.demo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MongoPool {
    MongoClient mongoClient;
//    String uri;
private static final Logger LOG = LoggerFactory.getLogger(MongoPool.class);


    private static MongoPool mongoPool = null;

    public static MongoPool getMongoPoolInstance() throws IOException {
        if (mongoPool == null) {
            mongoPool = new MongoPool();
        }
        return mongoPool;
    }

    private void initializeMongoClient() {
//        https://www.mongodb.com/docs/drivers/java/sync/v4.3/fundamentals/indexes/#unique-indexes
        MongoCollection<Document> collection = mongoClient.getDatabase("chat").getCollection("globalchat");
        IndexOptions indexOptions = new IndexOptions().unique(true);
        collection.createIndex(Indexes.ascending("username"), indexOptions);

    }

    private MongoPool() throws IOException {
        this.mongoClient = MongoClients.create(ConfigLoader.getConfigLoader().getMongoUrl());
        initializeMongoClient();
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public void start() throws IOException {
        getMongoPoolInstance();
        LOG.info("Started new MongoClient {}",mongoClient);

    }

    public void destroy() {

        if (mongoPool != null) {
            mongoClient.close();
        }
        LOG.info("Closing mongoClient");

    }
}
