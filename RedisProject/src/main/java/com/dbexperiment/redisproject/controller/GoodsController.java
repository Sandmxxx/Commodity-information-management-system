package com.dbexperiment.redisproject.controller;

import com.dbexperiment.redisproject.bean.Goods;
import com.dbexperiment.redisproject.service.GoodsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "goods", method = RequestMethod.POST)
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @RequestMapping(value = "addGoods", method = RequestMethod.POST)
    public Map<String,Object> addGoods(Goods goods) throws JsonProcessingException {
        return goodsService.addGoods(goods);
    }

    @RequestMapping(value = "deleteGoods", method = RequestMethod.POST)
    public Map<String,Object> deleteGoods(String id){
        return goodsService.deleteGoods(id);
    }

    @RequestMapping(value = "updateGoods", method = RequestMethod.POST)
    public Map<String,Object> updateGoods(Goods goods){
        return goodsService.updateGoods(goods);
    }

    @RequestMapping(value = "getAll", method = RequestMethod.GET)
    public Map<String,Object> getAll(){
        return goodsService.getAll();
    }

    @RequestMapping(value = "getGoodsById", method = RequestMethod.GET)
    public Map<String,Object> getGoodsById(String id){
        return goodsService.getGoodsById(id);
    }

    @RequestMapping(value = "getGoodsByName", method = RequestMethod.GET)
    public Map<String,Object> getGoodsByName(String name){
        return goodsService.getGoodsByName(name);
    }

    @RequestMapping(value = "getGoodsByBrand", method = RequestMethod.GET)
    public Map<String,Object> getGoodsByBrand(String brand){
        return  goodsService.getGoodsByBrand(brand);
    }

    @RequestMapping(value = "getGoodsByType", method = RequestMethod.GET)
    public Map<String,Object> getGoodsByType(String type){
        return goodsService.getGoodsByType(type);
    }

    @RequestMapping(value = "setExpire", method = RequestMethod.POST)
    public Map<String,Object> setExpire(String id,long time){
        return goodsService.setExpire(id,time);
    }

    @RequestMapping(value = "subscribe", method = RequestMethod.POST)
    public Map<String,Object> subscribe(String channel){
        // String nPattern  = "__keyspace@0__:goods:p-brand:" + pattern;
        return goodsService.subscribe(channel);
    }

    @RequestMapping(value = "publish", method = RequestMethod.POST)
    public Map<String,Object> publish(String channel,String message){
        return goodsService.publish(channel,message);
    }

    @RequestMapping(value = "unsub", method = RequestMethod.POST)
    public Map<String,Object> unsub(String channel){
        return goodsService.unsub(channel);
    }
}
