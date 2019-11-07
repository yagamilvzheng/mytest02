package com.itheima.health.dao;

import com.itheima.health.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    List<Order> findByCondition(Order order);

    void add(Order order);

    Map findById4Detail(Integer id);

    Integer findOrderCountByDate(String today);

    Integer findOrderCountBetweenDate(Map<String, Object> weekMap);

    Integer findVisitsCountByDate(String today);

    Integer findVisitsCountAfterDate(Map<String, Object> weekMap2);

    List<Map> findHotSetmeal();

}
