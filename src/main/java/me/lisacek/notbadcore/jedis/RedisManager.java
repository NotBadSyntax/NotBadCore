package me.lisacek.notbadcore.jedis;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import redis.clients.jedis.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class RedisManager {

    private JedisPool jedisPool;
    private final String server = UUID.randomUUID() +"_"+ System.currentTimeMillis();

    public void connect(String host, int port, String password) {
        jedisPool = new JedisPool(new JedisPoolConfig(), host, port, Protocol.DEFAULT_TIMEOUT, password);
    }

    public void disconnect() {
        if(jedisPool != null) jedisPool.destroy();
    }

    public void execute(Consumer<Jedis> action) {
        try(Jedis jedis = jedisPool.getResource()) {
            action.accept(jedis);
        }
    }

    public void listen(Consumer<JsonObject> function, String channel) {
        if(jedisPool == null) return;
        new Thread(() -> execute((jedis) -> jedis.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String ch, String message) {
                if(!ch.equalsIgnoreCase(channel)) return;
                JsonObject json = new JsonParser().parse(message).getAsJsonObject();
                if(json.get("redis-manager-id").getAsString().equals(server)) return;
                function.accept(json);
            }
        }, channel))).start();
    }

    public void publish(String channel, JsonObject message) {
        if(jedisPool == null) return;
        execute((jedis) -> {
            message.addProperty("redis-manager-id", server);
            jedis.publish(channel, message.toString());
        });
    }

    public void asyncPublish(String channel, JsonObject message) {
        if(jedisPool == null) return;
        CompletableFuture.runAsync(() -> {
            try {
                execute((jedis) -> {
                    message.addProperty("redis-manager-id", server);
                    jedis.publish(channel, message.toString());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public String getData(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        }
    }

    public void setData(String key, String data){
        if(jedisPool == null) return;
        execute((jedis) -> jedis.set(key, data));
    }

    public void deleteData(String key){
        if(jedisPool == null) return;
        execute((jedis) -> jedis.del(key));
    }

}