package com.itheima;

import com.alibaba.fastjson.JSONObject;
import com.itheima.health.dao.*;
import com.itheima.health.pojo.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Set;

@ContextConfiguration(locations = {"classpath*:applicationContext-dao.xml","classpath*:applicationContext-redis.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class JedisTest {

    @Autowired
    private JedisPool jedisPool;

@Autowired
private SetmealDao setmealDao;
@Autowired
CheckGroupDao checkGroupDao;
@Autowired
    CheckItemDao checkItemDao;

    @Test
    public void test02(){
        List<Setmeal> all = setmealDao.findAll();
        //以string的格式存储到redis
        String string = JSONObject.toJSONString(all);
        String setmeal = jedisPool.getResource().set("class",string);
        String setmeal2 = jedisPool.getResource().get("class");
        List<Setmeal> setmeals = JSONObject.parseArray(setmeal2, Setmeal.class);

        System.out.println(setmeals);
    }

    @Test
    public void test03(){

        String s1 = jedisPool.getResource().get("123");
        Setmeal setmeal = JSONObject.parseObject(s1, Setmeal.class);
        System.out.println("在redis中查询到的结果------------------------02"+setmeal);
        if (setmeal==null){
            setmeal = setmealDao.findById(13);
            List<CheckGroup> checkGroupList = checkGroupDao.findCheckGroupListBySetmealId(setmeal.getId());
            // 遍历checkGroupList
            for(CheckGroup checkgroup:checkGroupList){
                List<CheckItem> checkItemList = checkItemDao.findCheckItemListByCheckGroupId(checkgroup.getId());
                checkgroup.setCheckItems(checkItemList);
            }
            setmeal.setCheckGroups(checkGroupList);
            String string =  JSONObject.toJSONString(setmeal);
            jedisPool.getResource().sadd("123", string);
            System.out.println("在sql数据库中查询到的结果------------------------02"+setmeal);
        }


    }
}
