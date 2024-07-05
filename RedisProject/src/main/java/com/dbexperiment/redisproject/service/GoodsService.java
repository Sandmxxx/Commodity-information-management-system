package com.dbexperiment.redisproject.service;

import com.dbexperiment.redisproject.bean.Goods;
import com.dbexperiment.redisproject.config.RedisConfig;
import com.dbexperiment.redisproject.dao.GoodsDao;
import com.dbexperiment.redisproject.listener.RedisMessageListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public interface GoodsService {
    Map<String,Object> addGoods(Goods goods) throws JsonProcessingException;
    Map<String,Object> deleteGoods(String id);
    Map<String,Object> updateGoods(Goods goods);
    Map<String,Object> getAll();
    Map<String,Object> getGoodsById(String id);
    Map<String,Object> getGoodsByName(String name);
    Map<String,Object> getGoodsByBrand(String brand);
    Map<String,Object> getGoodsByType(String type);
    Map<String,Object> setExpire(String id,long time);
    Map<String,Object> subscribe(String pattern);
    Map<String,Object> publish(String channel,String message);
    Map<String,Object> unsub(String channel);
}

@Service
class GoodsServiceImpl implements GoodsService{
    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Autowired
    private RedisMessageListener redisMessageListener;


    @Override
    public Map<String, Object> addGoods(Goods goods) throws JsonProcessingException {
        Map<String, Object> res = new HashMap<String, Object>();
        if(goodsDao.addGoods(goods)) {
            res.put("statusMsg", "success");
        }
        else res.put("statusMsg", "fail");
        return res;
    }

    @Override
    public Map<String, Object> deleteGoods(String id) {
        Map<String, Object> res = new HashMap<String, Object>();
        if(goodsDao.deleteGoods(id)) {
            res.put("statusMsg", "success");
        }
        else res.put("statusMsg", "fail");
        return res;
    }

    @Override
    public Map<String, Object> updateGoods(Goods goods) {
        Map<String, Object> res = new HashMap<String, Object>();
        if(goodsDao.updateGoods(goods)) {
            res.put("statusMsg", "success");
        }
        else res.put("statusMsg", "fail");
        return res;
    }

    @Override
    public Map<String, Object> getAll() {
        Map<String, Object> res = new HashMap<String, Object>();
        List<Goods> goodsList = new ArrayList<>();
        goodsList = goodsDao.getAll();
        res.put("statusMsg", "success");
        res.put("goods",goodsList);
        return res;
    }

    @Override
    public Map<String, Object> getGoodsById(String id) {
        Map<String, Object> res = new HashMap<String, Object>();
        Goods goods = goodsDao.getGoodsById(id);
        res.put("statusMsg", "success");
        res.put("goods", goods);
        return res;
    }

    @Override
    public Map<String, Object> getGoodsByName(String name) {
        Map<String, Object> res = new HashMap<String, Object>();
        List<Goods> goodsList = new ArrayList<>();
        goodsList = goodsDao.getGoodsByName(name);
        res.put("statusMsg", "success");
        res.put("goods", goodsList);
        return res;
    }

    @Override
    public Map<String, Object> getGoodsByBrand(String brand) {
        Map<String, Object> res = new HashMap<String, Object>();
        List<Goods> goodsList = new ArrayList<>();
        goodsList = goodsDao.getGoodsByBrand(brand);
        res.put("statusMsg", "success");
        res.put("goods", goodsList);
        return res;
    }

    @Override
    public Map<String, Object> getGoodsByType(String type) {
        Map<String, Object> res = new HashMap<String, Object>();
        List<Goods> goodsList = new ArrayList<>();
        goodsList = goodsDao.getGoodsByType(type);
        res.put("statusMsg", "success");
        res.put("goods", goodsList);
        return res;
    }

    @Override
    public Map<String, Object> setExpire(String id, long time) {
        Map<String, Object> res = new HashMap<String, Object>();
        if(goodsDao.setExpire(id,time)) {
            res.put("statusMsg", "success");
        }
        else res.put("statusMsg", "fail");
        return res;
    }

    @Override
    public Map<String, Object> subscribe(String pattern) {
        Map<String, Object> res = new HashMap<String, Object>();
        redisMessageListenerContainer.addMessageListener(redisMessageListener,new PatternTopic(pattern));
        res.put("statusMsg", "success");
        return res;
    }

    @Override
    public Map<String, Object> publish(String channel,String message) {
        Map<String, Object> res = new HashMap<String, Object>();
        goodsDao.publish(channel,message);
        res.put("statusMsg", "success");
        return res;
    }

    @Override
    public Map<String, Object> unsub(String channel) {
        Map<String, Object> res = new HashMap<String, Object>();
        redisMessageListenerContainer.removeMessageListener(redisMessageListener,new PatternTopic(channel));
        res.put("statusMsg", "success");
        return res;
    }
}
