package com.shawckz.xperms.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.shawckz.xperms.config.Configuration;
import com.shawckz.xperms.config.annotations.ConfigData;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;


/**
 * The DatabaseManager class
 * Used to handle the connection the MongoClient.
 * The setup method should not be touched except in the PureCore onEnable
 */
public class DatabaseManager extends Configuration {

    protected static boolean instantiated = false;
    private MongoClient mongoClient;
    private MongoDatabase db;


    @ConfigData("database.name")
    private static String databaseName = "xxx";
    @ConfigData("database.authName")
    private static String authDatabaseName = "xxx";
    @ConfigData("database.host")
    private static String host = "xxx";
    @ConfigData("database.port")
    private static int port = 3309;
    @ConfigData("database.credentials.username")
    private static String username = "xxx";
    @ConfigData("database.credentials.password")
    private static String password = "xxx";
    @ConfigData("database.useAuth")
    private boolean useAuth = false;


    public DatabaseManager(Plugin plugin) {
        super(plugin, "database.yml");
        if (!instantiated) {
            instantiated = true;
        } else {
            throw new RuntimeException("DatabaseManager instance already exists");
        }
        load();
        save();
        setup();
    }

    private void setup() {
        if (useAuth) {
            MongoCredential credential = MongoCredential.createCredential(username, authDatabaseName, password.toCharArray());
            MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(50).build();
            mongoClient = new MongoClient(new ServerAddress(host, port), Arrays.asList(credential), options);
            db = mongoClient.getDatabase(databaseName);
        } else {
            mongoClient = new MongoClient(new ServerAddress(host, port));
            db = mongoClient.getDatabase(databaseName);
        }
    }

    public void shutdown() {
        mongoClient.close();
        db = null;
        mongoClient = null;
    }

    public MongoDatabase getDb() {
        return db;
    }

    public void setDb(MongoDatabase db) {
        this.db = db;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }
}
