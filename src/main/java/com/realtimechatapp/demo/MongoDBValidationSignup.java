package com.realtimechatapp.demo;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBValidationSignup implements CommonAuthInterface {
    MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> collection;

    //    So in any class, all we have to do is create an object of MongoPool and keep passing that object in all constructors
    public MongoDBValidationSignup(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        this.database = mongoClient.getDatabase("chat");
        this.collection = database.getCollection("globalchat");

    }

    //    send this into sanitize then come back with proper message
    @Override
    public boolean signupAuth(String username, String password) {
        String bcryptHashString = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        Document document = new Document("username", username).append("password", bcryptHashString);
        try {
            collection.insertOne(document);
        } catch (MongoWriteException e) {
            return false;
        }
        return true;

//        false would mean user already exists. Pick a different username
    }

    @Override
    public String loginAuth(String username, String password) {
        Document query = new Document("username", username);
        Document doc = collection.find(query).first();
        try {
//            https://github.com/patrickfav/bcrypt?tab=readme-ov-file#quickstart
            String bcryptHashString = doc.getString("password");
            if (BCrypt.verifyer().verify(password.toCharArray(), bcryptHashString).verified) {
                return "true";
            }
        } catch (NullPointerException e) {
            return "User doesnt exist";

        }
        return "Incorrect username and password";

    }


}
