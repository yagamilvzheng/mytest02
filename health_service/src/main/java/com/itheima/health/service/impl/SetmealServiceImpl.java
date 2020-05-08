package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealDao setmealDao;
    @Autowired
    private JedisPool jedisPool;




    //新增套餐
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        // 1：新增套餐
        setmealDao.add(setmeal);
        // 2：向套餐和检查组的中间表中插入数据
        if(checkgroupIds != null && checkgroupIds.length > 0){
            //绑定套餐和检查组的多对多关系
            setSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
            //将图片名称保存到Redis
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        }
    }



    //绑定套餐和检查组的多对多关系
    private void setSetmealAndCheckGroup(Integer id, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            Map<String,Integer> map = new HashMap<String,Integer>();
            map.put("setmeal_id",id);
            map.put("checkgroup_id",checkgroupId);
            setmealDao.setSetmealAndCheckGroup(map);
        }
    }


    @Override
    public List<Setmeal> findAll() {
        //List<Setmeal> all = setmealDao.findAll();
        //以string的格式存储到redis
        //String string = JSONObject.toJSONString(all);
       // jedisPool.getResource().sadd(SETMEAL_CONTENT_PAGE,string);

        //取出redis里面的数据,转换成前端页面需要的格式
       // String s = jedisPool.getResource().get(SETMEAL_CONTENT_PAGE);
        //System.out.println(s+"------------------------------------------------------------------");
        //JSONObject jsonObject = JSONObject.parseObject(s);
        //JSONArray array = JSONObject.parseArray(s);
        List<Setmeal> setmeals = new ArrayList<>();
        String s = jedisPool.getResource().get(SETMEAL_CONTENT_PAGE);
        setmeals = JSONObject.parseArray(s, Setmeal.class);
        System.out.println("在redis中查询到的结果------------------------01");

            if(setmeals==null){
                setmeals = setmealDao.findAll();
                JSONObject.toJSONString(setmeals);
                System.out.println("在sql数据库中查询到的结果------------------------01");
                return setmeals;
            }
        return setmeals;
    }
    @Autowired
    CheckGroupDao checkGroupDao;

    @Autowired
    CheckItemDao checkItemDao;


    @Override
    public Setmeal findById(int id) {
//        Setmeal setmeal = setmealDao.findById(id);
//        List<CheckGroup> checkGroupList = checkGroupDao.findCheckGroupListBySetmealId(setmeal.getId());
//        // 遍历checkGroupList
//        for(CheckGroup checkgroup:checkGroupList){
//            List<CheckItem> checkItemList = checkItemDao.findCheckItemListByCheckGroupId(checkgroup.getId());
//            checkgroup.setCheckItems(checkItemList);
//        }
//        setmeal.setCheckGroups(checkGroupList);
        //以string的格式存储到redis
        //String string = JSONObject.toJSONString(setmeal);
        //jedisPool.getResource().sadd(SETMEA_LITEM_PAGE, string);

        //取出redis里面的数据,转换成前端页面需要的格式
        //String s = jedisPool.getResource().get(SETMEA_LITEM_PAGE);
        Setmeal setmeal = null;
        String s = jedisPool.getResource().get(SETMEA_LITEM_PAGE);
        setmeal = JSONObject.parseObject(s, Setmeal.class);
        if (setmeal==null){
            setmeal = setmealDao.findById(id);
        List<CheckGroup> checkGroupList = checkGroupDao.findCheckGroupListBySetmealId(setmeal.getId());
        // 遍历checkGroupList
        for(CheckGroup checkgroup:checkGroupList){
            List<CheckItem> checkItemList = checkItemDao.findCheckItemListByCheckGroupId(checkgroup.getId());
            checkgroup.setCheckItems(checkItemList);
        }
        setmeal.setCheckGroups(checkGroupList);
            String string =  JSONObject.toJSONString(setmeal);
            jedisPool.getResource().sadd(SETMEA_LITEM_PAGE, string);
            System.out.println("在sql数据库中查询到的结果------------------------02");
            return setmeal;
        }
        System.out.println("在redis中查询到的结果------------------------02");
        return setmeal;
    }

    @Override
    public List<Map> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

    //华丽的分割线
    //------------------------------------------------------------------------------------------------------------------------------------------------

        static final String SETMEAL_CONTENT_PAGE = "setmealcontentpage";
        static final String SETMEA_LITEM_PAGE = "setmealitempage";



}