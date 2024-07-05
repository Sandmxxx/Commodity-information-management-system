package com.dbexperiment.redisproject.util;

import com.dbexperiment.redisproject.config.SpringContextUtil;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/websocket/{channel}") //接受websocket请求路径
@Component  //注册到spring容器中
public class MyWebSocket {

    //保存所有在线socket连接
//    private static Map<String,MyWebSocket> webSocketMap = new LinkedHashMap<>();
    private static Map<String, List<MyWebSocket>> webSocketMap = new ConcurrentHashMap<String, List<MyWebSocket>>();

    //记录当前在线数目
    private static int count=0;

//    private String channelList;

    //当前连接（每个websocket连入都会创建一个MyWebSocket实例
    private Session session;

    private Logger log = LoggerFactory.getLogger(this.getClass());
    //处理连接建立
    @OnOpen
    public void onOpen(@PathParam("channel") String channel,Session session){
//        String [] cList = channelList.split(",");
        this.session=session;
//        for(String channel : cList){
        addWebSocketMap(channel,this);
//        }
//        addCount();
        log.info("新的连接加入：{}",session.getId());
    }

    //接受消息
    @OnMessage
    public void onMessage(String message) throws IOException {
        log.info("收到客户端{}消息：{}",session.getId(),message);
//        String [] cList = channelList.split(",");
//        for(String channel : cList){
//            sendMessage("收到消息" + message,channel);
//        }
        // 取消订阅
        String [] s = message.split(":");
        if(s[0].equals("unSub")){
            removeWebSocketMap(s[1],this);
        }
        // 订阅
        else if(s[0].equals("addSub")){
            addWebSocketMap(s[1],this);
        }
        else{
            this.session.getBasicRemote().sendText("收到消息："+message);
        }
    }

    //处理错误
    @OnError
    public void onError(Throwable error,Session session){
        log.info("发生错误{},{}",session.getId(),error.getMessage());
    }

    //处理连接关闭
    @OnClose
    public void onClose(@PathParam("channel") String channel){
//        String [] cList = channelList.split(",");
//        for(String channel : cList){
//        removeWebSocketMap(channel,this);
//        }
        webSocketMap.forEach(((s, myWebSockets) -> {
            if(myWebSockets.contains(this)){
                myWebSockets.remove(this);
            }
        }));

        reduceCount();
        log.info("连接关闭:{}",this.session.getId());
    }

    //群发消息

    //发送消息
    /**
     * 服务端发送消息给客户端
     */
    public void sendMessage(String message,String subscribeKey) {
        try {
            log.info("服务端给客户端,发送消息{}", message);
            List<MyWebSocket> webSocketServices = webSocketMap.get(subscribeKey);
            if (!CollectionUtils.isEmpty(webSocketServices)){
                for (MyWebSocket webSocketService : webSocketServices) {
                    webSocketService.session.getBasicRemote().sendText(message);
                }
            }

        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败：{}", e.getMessage());
        }
    }


    //广播消息
    public void broadcast(String msg){
//        webSocketMap.forEach(((s, myWebSockets) -> {
//            for(MyWebSocket socket : myWebSockets){
//                try {
//                    socket.session.getBasicRemote().sendText(msg);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }));
        List<MyWebSocket> myWebSocketList = webSocketMap.get("root");
        for(MyWebSocket myWebSocket:myWebSocketList){
            try {
                myWebSocket.session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //获取在线连接数目
    public static int getCount(){
        return count;
    }

    public synchronized void addWebSocketMap(String subscribeKey,MyWebSocket webSocketService){
        if (webSocketMap.get(subscribeKey) != null){
            List<MyWebSocket> webSocketServices = webSocketMap.get(subscribeKey);
            if(!webSocketServices.contains(webSocketService)){
                webSocketServices.add(webSocketService);
            }
        }else {
            List<MyWebSocket> webSocketServices = new ArrayList<>();
            webSocketServices.add(webSocketService);
            webSocketMap.put(subscribeKey,webSocketServices);
        }
    }

    public synchronized void removeWebSocketMap(String subscribeKey,MyWebSocket webSocketService) {
        if (webSocketMap.get(subscribeKey) != null) {
            List<MyWebSocket> webSocketServices = webSocketMap.get(subscribeKey);
            webSocketServices.remove(webSocketService);
        }
    }


        //操作count，使用synchronized确保线程安全
    public static synchronized void addCount(){
        MyWebSocket.count++;
    }

    public static synchronized void reduceCount(){
        MyWebSocket.count--;
    }
}
