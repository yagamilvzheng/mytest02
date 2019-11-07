package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;


import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderMobileController {
    @Reference
    OrderService orderService;
    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody Map map){
        //手机号
        String telephone = ( String ) map.get("telephone");
        //redis里面的验证码
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //网页里面的验证码
        String validatecode = ( String ) map.get("validateCode");
        if(codeInRedis == null || !codeInRedis.equals(validatecode)){
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }
        Result result = null;
        try {
            map.put("orderType",Order.ORDERTYPE_WEIXIN);
            result = orderService.order(map);
        }catch (Exception e){
            e.printStackTrace();
            return result;
        }
        return result;

    }

}
