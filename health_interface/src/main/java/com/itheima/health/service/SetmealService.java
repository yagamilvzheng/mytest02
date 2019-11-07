package com.itheima.health.service;

import com.itheima.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    List<Setmeal> findAll();

    Setmeal findById(int id);

    List<Map> findSetmealCount();

}
