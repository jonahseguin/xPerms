package com.shawckz.xperms.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.shawckz.xperms.config.Configuration;
import com.shawckz.xperms.config.annotations.ConfigData;
import com.shawckz.xperms.exception.PermissionsException;
import org.bukkit.plugin.Plugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Arrays;


/**
 * The DatabaseManager class
 * Used to handle the connection the MongoClient.
 * The setup method should not be touched except in the PureCore onEnable
 */
public class DatabaseManager extends Configuration {

    private static boolean instantiated = false;
    private MongoClient mongoClient;
    private MongoDatabase db;
    private JedisPool jedisPool;

    /*
    MONGO
     */

    @ConfigData("database.mongo.name")
    private static String mongoDatabaseName = "minecraft";

    @ConfigData("database.mongo.auth-name")
    private static String mongoAuthDatabaseName = "admin";

    @ConfigData("database.mongo.host")
    private static String mongoHost = "localhost";

    @ConfigData("database.mongo.port")
    private static int mongoPort = 3309;

    @ConfigData("database.mongo.username")
    private static String mongoUsername = "username";

    @ConfigData("database.mongo.password")
    private static String mongoPassword = "password";

    @ConfigData("database.mongo.use-auth")
    private boolean useMongoAuth = false;

    /*
    REDIS
     */

    @ConfigData("database.redis.use-redis")
    private boolean useRedis = false;

    @ConfigData("database.redis.host")
    private String redisHost = "localhost";

    @ConfigData("database.redis.port")
    private int redisPort = 6379;

    @ConfigData("database.redis.use-auth")
    private boolean useRedisAuth = false;

    @ConfigData("database.redis.password")
    private String redisPassword = "password";

    public DatabaseManager(Plugin plugin) {
        super(plugin, "database.yml");
        if (!instantiated) {
            instantiated = true;
        } else {
            throw new PermissionsException("DatabaseManager instance already exists");
        }
        load();
        save();
        setup();
    }

    private void setup() {
        if (useMongoAuth) {
            MongoCredential credential = MongoCredential.createCredential(mongoUsername, mongoAuthDatabaseName, mongoPassword.toCharArray());
            MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(50).build();
            mongoClient = new MongoClient(new ServerAddress(mongoHost, mongoPort), Arrays.asList(credential), options);
            db = mongoClient.getDatabase(mongoDatabaseName);
        } else {
            mongoClient = new MongoClient(new ServerAddress(mongoHost, mongoPort));
            db = mongoClient.getDatabase(mongoDatabaseName);
        }

        if (useRedis) {
            jedisPool = new JedisPool(new JedisPoolConfig(), redisHost, redisPort);
        }

    }

    public Jedis getJedisResource() {
        Jedis jedis = jedisPool.getResource();
        if (useRedisAuth) {
            jedis.auth(redisPassword);
        }
        return jedis;
    }

    public void returnJedisResource(Jedis jedis) {
        jedis.close();
    }

    public void shutdown() {
        mongoClient.close();
        jedisPool.close();
        jedisPool = null;
        db = null;
        mongoClient = null;
    }

    public boolean isRedisEnabled() {
        return useRedis;
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
