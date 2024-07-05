package com.dbexperiment.redisproject.listener;

import com.dbexperiment.redisproject.util.MyWebSocket;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.stereotype.Component;

@Data
@Component
public class RedisExpiredListener implements MessageListener {

    //监听主题
    private  final PatternTopic topic = new PatternTopic("__keyevent@*__:expired");

    @Autowired
    private MyWebSocket myWebSocket;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String topic = new String(pattern);
        String msg = new String(message.getBody());
        String output;
        if(msg.length()>=7) {
            output = "--- 商品：" + msg.substring(6) + " 过期 ---";
            System.out.println("--- 商品：" + msg.substring(6) + " 过期 ---");
        }
        else {
            output = "--- 商品：" + msg + " 过期 ---";
            System.out.println("--- 商品：" + msg + " 过期 ---");
        }
        // 向客户端广播过期消息
        myWebSocket.broadcast(output);
    }
}
