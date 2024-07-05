package com.dbexperiment.redisproject.dao;

import com.dbexperiment.redisproject.bean.Goods;
import com.dbexperiment.redisproject.util.RedisUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Repository
public interface GoodsDao {
    public boolean hsetGoods(Goods goods);
    public boolean addGoods(Goods goods) ;
    public boolean deleteGoods(String id);
    public boolean updateGoods(Goods goods);
    public Goods getGoodsById(String id);
    public List<Goods> getAll();
    public List<Goods> getGoodsByName(String name);
    public List<Goods> getGoodsByBrand(String brand);
    public List<Goods> getGoodsByType(String type);
    public boolean setExpire(String id,long time);
    public void publish(String channel,String message);
}

@Repository
class GoodsDaoImpl implements GoodsDao{

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisUtils redisUtils;

    // JSON序列化工具
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean hsetGoods(Goods goods) {
        String id = "goods:"+goods.getId();
        boolean f1 = redisUtils.hsetnx(id,"id",goods.getId());
        boolean f2 = redisUtils.hset(id,"name",goods.getName());
        boolean f3 = redisUtils.hset(id,"brand",goods.getBrand());
        boolean f4 = redisUtils.hset(id,"type",goods.getType());
        boolean f5 = redisUtils.hset(id,"unit",goods.getUnit());
        boolean f6 = redisUtils.hset(id,"price",goods.getPrice());
        return f1&&f2&&f3&&f4&&f5&&f6;
    }

    @Override
    public boolean addGoods(Goods goods){
        String id = "goods:"+goods.getId();
        String message = "新增商品信息：\n商品编号："+goods.getId()+" 商品名称："+goods.getName()+" 商品品牌："+goods.getBrand()
                +" 商品类型："+goods.getType()+" 商品单位："+goods.getUnit()+" 商品价格："+goods.getPrice()
                +" 过期时间：" + goods.getTime() + "s";

        // 数据库操作订阅
        stringRedisTemplate.convertAndSend("商品操作", message);

        // 设置相同name的id集合，便于后期查找
        stringRedisTemplate.convertAndSend(goods.getName(), message);
        stringRedisTemplate.opsForZSet().addIfAbsent("goods:p-name:"+goods.getName(),goods.getId(), goods.getPrice());
        // 设置相同brand的id集合，便于后期查找
        stringRedisTemplate.convertAndSend(goods.getBrand(), message);
        stringRedisTemplate.opsForZSet().addIfAbsent("goods:p-brand:"+goods.getBrand(),goods.getId(), goods.getPrice());
        // 设置相同type的id集合，便于后期查找
        stringRedisTemplate.convertAndSend(goods.getType(), message);
        stringRedisTemplate.opsForZSet().addIfAbsent("goods:p-type:"+goods.getType(),goods.getId(), goods.getPrice());
        // 设置相同price的id集合，便于后期排序
        stringRedisTemplate.opsForZSet().addIfAbsent("goods:p-id",goods.getId(), goods.getPrice());
//        return redisUtils.setnx(id,goods);
        return redisUtils.setnx(id,goods, goods.getTime());
    }

    @Override
    public boolean deleteGoods(String id) {
        if(stringRedisTemplate.hasKey("goods:"+id)==false) return false;
//        if(getGoodsById(id) == null) return false;
        Goods goods = (Goods) redisUtils.get("goods:"+id);
        String message = "删除商品信息：\n商品编号："+goods.getId()+" 商品名称："+goods.getName()+" 商品品牌："+goods.getBrand()
                +" 商品类型："+goods.getType()+" 商品单位："+goods.getUnit()+" 商品价格："+goods.getPrice()
                +" 过期时间：" + goods.getTime() + "s";

        // 数据库操作订阅
        stringRedisTemplate.convertAndSend("商品操作", message);

        // 设置相同name的id集合，便于后期查找
        stringRedisTemplate.convertAndSend(goods.getName(), message);
        stringRedisTemplate.opsForZSet().remove("goods:p-name:"+goods.getName(),id);
        // 设置相同brand的id集合，便于后期查找
        stringRedisTemplate.convertAndSend(goods.getBrand(), message);
        stringRedisTemplate.opsForZSet().remove("goods:p-brand:"+goods.getBrand(),id);
        // 设置相同type的id集合，便于后期查找
        stringRedisTemplate.convertAndSend(goods.getType(), message);
        stringRedisTemplate.opsForZSet().remove("goods:p-type:"+goods.getType(),id);
        // 设置按价格的id集合，便于后期排序
        stringRedisTemplate.opsForZSet().remove("goods:p-id",id);
        return Boolean.TRUE.equals(stringRedisTemplate.delete("goods:"+id));
    }

    @Override
    public boolean updateGoods(Goods goods) {
        String id = "goods:"+goods.getId();
        if(stringRedisTemplate.hasKey(id)==false) return false;
        // 获取旧的商品信息，并删除原来的信息
        Goods oldgoods = getGoodsById(goods.getId());
        stringRedisTemplate.opsForZSet().remove("goods:p-name:"+oldgoods.getName(),goods.getId());
//        System.out.println("name:" + oldgoods.getName());
        stringRedisTemplate.opsForZSet().remove("goods:p-brand:"+oldgoods.getBrand(),goods.getId());
        stringRedisTemplate.opsForZSet().remove("goods:p-type:"+oldgoods.getType(),goods.getId());
        stringRedisTemplate.opsForZSet().remove("goods:p-id",goods.getId());

        String message = "修改商品信息：\n商品编号："+goods.getId()+" 商品名称："+goods.getName()+" 商品品牌："+goods.getBrand()
                +" 商品类型："+goods.getType()+" 商品单位："+goods.getUnit()+" 商品价格："+goods.getPrice()
                +" 过期时间：" + goods.getTime() + "s";

        // 数据库操作订阅
        stringRedisTemplate.convertAndSend("商品操作", message);

        // 设置相同name的id集合，便于后期查找
        stringRedisTemplate.convertAndSend(goods.getName(), message);
        stringRedisTemplate.opsForZSet().add("goods:p-name:"+goods.getName(),goods.getId(), goods.getPrice());
        // 设置相同brand的id集合，便于后期查找
        stringRedisTemplate.convertAndSend(goods.getBrand(), message);
        stringRedisTemplate.opsForZSet().add("goods:p-brand:"+goods.getBrand(),goods.getId(), goods.getPrice());
//        redisUtils.sSet("goods:brand:"+goods.getBrand(),goods.getId());
        // 设置相同type的id集合，便于后期查找
        stringRedisTemplate.convertAndSend(goods.getType(), message);
        stringRedisTemplate.opsForZSet().add("goods:p-type:"+goods.getType(),goods.getId(), goods.getPrice());
        //redisUtils.sSet("goods:type:"+goods.getType(),goods.getId());
        // 设置相同price的id集合，便于后期排序
        stringRedisTemplate.opsForZSet().add("goods:p-id",goods.getId(), goods.getPrice());
        return redisUtils.set(id,goods, goods.getTime());
    }

    @Override
    public Goods getGoodsById(String id) {
        // 根据key获取对应的value
        Goods goods = (Goods) redisUtils.get("goods:"+id);
        if(goods == null) return goods;
        // 为goods设置当前的过期时间
        goods.setTime(redisUtils.getExpire("goods:"+id));
        return goods;
    }

    @Override
    public List<Goods> getAll() {
        List<Goods> goodsList = new ArrayList<>();
        // 从所有商品id集合中获取所有商品id的集合，返回的结果是按价格从小到大排序的id集合
        val idSet = stringRedisTemplate.opsForZSet().range("goods:p-id",0,-1);
        // 遍历id集合，根据商品id查找商品对象加入到商品列表
        for (Object id:idSet) {
            Goods goods = getGoodsById((String) id);
            if(goods!=null)
                goodsList.add(goods);
        }
        // 返回商品列表
        return goodsList;
    }

    @Override
    public List<Goods> getGoodsByName(String name) {
        List<Goods> goodsList = new ArrayList<>();
        // 从商品名称集合中获取所有商品名称相同id的集合，返回的结果是按价格从小到大排序的id集合
        val idSet = stringRedisTemplate.opsForZSet().range("goods:p-name:"+name,0,-1);
        // 遍历id集合，根据商品id查找商品对象加入到商品列表
        for (Object id:idSet) {
            Goods goods = getGoodsById((String) id);
            if(goods!=null)
                goodsList.add(goods);
        }
        return goodsList;
    }

    @Override
    public List<Goods> getGoodsByBrand(String brand) {
        List<Goods> goodsList = new ArrayList<>();
        // 从商品品牌集合中获取所有商品品牌相同id的集合，返回的结果是按价格从小到大排序的id集合
        val idSet = stringRedisTemplate.opsForZSet().range("goods:p-brand:"+brand,0,-1);
        // 遍历id集合，根据商品id查找商品对象加入到商品列表
        for (Object id:idSet) {
            Goods goods = getGoodsById((String) id);
            if(goods!=null)
                goodsList.add(goods);
        }
        // 返回商品列表，是按价格排序的结果
        return goodsList;
    }

    @Override
    public List<Goods> getGoodsByType(String type) {
        List<Goods> goodsList = new ArrayList<>();
        // 从商品类型集合中获取所有商品类型相同id的集合，返回的结果是按价格从小到大排序的id集合
        val idSet = stringRedisTemplate.opsForZSet().range("goods:p-type:"+type,0,-1);
        // 遍历id集合，根据商品id查找商品对象加入到商品列表
        for (Object id:idSet) {
            Goods goods = getGoodsById((String) id);
            if(goods!=null)
                goodsList.add(goods);
        }
        // 返回商品列表，是按价格排序的结果
        return goodsList;
    }

    @Override
    public boolean setExpire(String id,long time) {
        String msg = "商品编号：" + id + " 设置过期时间：" + time + "s";
        stringRedisTemplate.convertAndSend("商品操作", msg);
        return redisUtils.expire("goods:"+id,time);
    }

    @Override
    public void publish(String channel, String message) {
        stringRedisTemplate.convertAndSend(channel,message);
    }

}
