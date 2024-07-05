package com.dbexperiment.redisproject.listener;

import com.dbexperiment.redisproject.util.MyWebSocket;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.channels.Channel;

@Component
@Data
public class RedisMessageListener implements MessageListener {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MyWebSocket myWebSocket;

    //监听的主题
    private  final ChannelTopic channel = new ChannelTopic("redis.life");

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 获取消息
        byte[] messageBody = message.getBody();
        // 使用值序列化器转换
        Object msg = stringRedisTemplate.getValueSerializer().deserialize(messageBody);
        // 获取监听的频道
        byte[] channelByte = message.getChannel();
        // 使用字符串序列化器转换
        Object channel = stringRedisTemplate.getStringSerializer().deserialize(channelByte);
        // 渠道名称转换
        String patternStr = new String(pattern);
        System.out.println(patternStr);
        System.out.println("---频道---: " + channel);
        System.out.println("---消息内容---: " + msg);

//        System.out.println(myWebSocket.toString());

        String output = "--- 来自频道：" + channel + "      消息内容: " + msg + " ---";
//        System.out.println("111222");
        // 向订阅该频道的客户端发送消息
        myWebSocket.sendMessage(output, (String) channel);
    }
}
