package com.itheima;

import com.alibaba.fastjson.JSONObject;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.dao.UserDao;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.pojo.User;
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
}
