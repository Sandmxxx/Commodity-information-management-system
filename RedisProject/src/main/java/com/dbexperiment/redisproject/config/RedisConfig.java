package com.dbexperiment.redisproject.config;


import com.dbexperiment.redisproject.listener.RedisDeleteListener;
import com.dbexperiment.redisproject.listener.RedisExpiredListener;
import com.dbexperiment.redisproject.listener.RedisMessageListener;
import com.dbexperiment.redisproject.listener.RedisUpdateAndAddListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory){
        // 创建RedisTemplate对象
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 设置连接工厂
        template.setConnectionFactory(connectionFactory);
        // 创建JSON序列化工具
        GenericJackson2JsonRedisSerializer jsonRedisSerializer =
                new GenericJackson2JsonRedisSerializer();
        // 设置Key的序列化
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        // 设置Value的序列化
        template.setValueSerializer(jsonRedisSerializer);
        template.setHashValueSerializer(jsonRedisSerializer);
        // 返回
        return template;
    }

    /**
     * 监听redis的过期事件
     */
//    @Bean
//    @Qualifier("exprie")
//    public RedisMessageListenerContainer container(RedisConnectionFactory factory) {
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(factory);
//        return container;
//    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUpdateAndAddListener redisUpdateAndAddListener;

    @Autowired
    private RedisDeleteListener redisDeleteListener;

    @Autowired
    private RedisExpiredListener redisExpiredListener;

    @Autowired
    private RedisMessageListener redisMessageListener;

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory factory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        //订阅频道redis.news 和 redis.life  这个container 可以添加多个 messageListener
//        container.addMessageListener(listener, new ChannelTopic("redis.life"));
//        container.addMessageListener(listener, new ChannelTopic("redis.news"));
//        container.addMessageListener(redisMessageListener,new PatternTopic("__keyspace@0__:goods:p-brand:华为"));
//        container.addMessageListener(redisMessageListener,new PatternTopic("__key*__:*"));
//        container.addMessageListener(redisMessageListener,new ChannelTopic("redis.news"));
//        //监听所有的key的set事件
//        container.addMessageListener(redisUpdateAndAddListener, redisUpdateAndAddListener.getTopic());
//        //监听所有key的删除事件
//        container.addMessageListener(redisDeleteListener,redisDeleteListener.getTopic());
        //监听所有key的过期事件
        container.addMessageListener(redisExpiredListener,redisExpiredListener.getTopic());

        return container;
    }
}
