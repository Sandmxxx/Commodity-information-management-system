package com.dbexperiment.redisproject;

import com.dbexperiment.redisproject.bean.Goods;
import com.dbexperiment.redisproject.util.RedisUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class RedisProjectApplicationTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtils redisUtils;

    // JSON序列化工具
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testString() throws JsonProcessingException {
//         创建对象
//        Goods goods = new Goods("HW-dhfjs456","华为Mate60","数码","华为2","台",6999);
//        //Goods goods2 = new Goods("HW-dhfjs488","华为Mate60","数码","华为","台",6999);
//        // 手动序列化
//        String json = mapper.writeValueAsString(goods);
//        // 写入数据
//        stringRedisTemplate.opsForValue().set("goods:HW-dhfjs455", json);
//
//        //redisTemplate.opsForHash().put("HW-dhfjs488","name","huawei2");
//        // 获取数据
//        String jsonUser = stringRedisTemplate.opsForValue().get("goods:HW-dhfjs455");
//        // 手动反序列化
//        Goods goods1 = mapper.readValue(jsonUser, Goods.class);
//        System.out.println("goods1 = " + goods1);
//        boolean f = redisUtils.setnx("goods:"+goods.getCode(),goods);
        Goods goods2 = (Goods) redisUtils.get("goods:HW-00005");
        System.out.println("goods = " + goods2);
    }
@Test
    void publish() {
        // 使用convertAndSend方法向频道redisChat发布消息
        redisTemplate.convertAndSend("brand", "aaa");
        redisTemplate.convertAndSend("brand", "bbb");
    }
}
