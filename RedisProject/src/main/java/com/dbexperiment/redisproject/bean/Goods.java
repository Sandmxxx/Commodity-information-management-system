package com.dbexperiment.redisproject.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goods {
    String id; // 编码
    String name; // 名称
    String brand; // 品牌
    String type; // 类型
    String unit; // 单位
    double price; // 价格
    long time; // 过期时间
}
