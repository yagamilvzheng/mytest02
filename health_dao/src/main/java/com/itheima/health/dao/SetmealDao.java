package com.itheima.health.dao;

import com.itheima.health.pojo.Setmeal;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SetmealDao {
    void add(Setmeal setmeal);

    void setSetmealAndCheckGroup(Map<String, Integer> map);

    List<Setmeal> findAll();

    Setmeal findById(int id);

    List<Map> findSetmealCount();
}
