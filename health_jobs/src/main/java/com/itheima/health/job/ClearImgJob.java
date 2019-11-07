package com.itheima.health.job;
import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.RedisConstant;

import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.DateUtils;
import com.itheima.health.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
/**
 * 定时任务：清理垃圾图片
 */

@Component
public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;


    //清理图片
    public void clearImg(){
        //计算redis中两个集合的差值，获取垃圾图片名称
        Set<String> set = jedisPool.getResource().sdiff(
                RedisConstant.SETMEAL_PIC_RESOURCES,
                RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        Iterator<String> iterator = set.iterator();
        while(iterator.hasNext()){
            String pic = iterator.next();
            System.out.println("删除图片的名称是："+pic);
            //删除图片服务器中的图片文件
            QiniuUtils.deleteFileFromQiniu(pic);
            //删除redis中的数据
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,pic);
        }
    }
}
