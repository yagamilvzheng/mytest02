package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 预约设置
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    OrderSettingDao orderSettingDao;

    //批量添加
    public void add(List<OrderSetting> list) {
        if (list != null && list.size() > 0) {
            for (OrderSetting orderSetting : list) {
                //检查此数据（日期）是否存在
                long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (count > 0) {
                    //已经存在，执行更新操作
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                } else {
                    //不存在，执行添加操作
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    //根据日期查询预约设置数据
    public List<Map> getOrderSettingByMonth(String date) {//2019-03
        // 1.组织查询Map，dateBegin表示月份开始时间，dateEnd月份结束时间
        String dateBegin = date + "-1";//2019-03-1
        String dateEnd = date + "-31";//2019-03-31
        Map map = new HashMap();
        map.put("dateBegin", dateBegin);
        map.put("dateEnd", dateEnd);
        // 2.查询当前月份的预约设置
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> data = new ArrayList<>();
        // 3.将List<OrderSetting>，组织成List<Map>
        for (OrderSetting orderSetting : list) {
            Map orderSettingMap = new HashMap();
            orderSettingMap.put("date", orderSetting.getOrderDate().getDate());//获得日期（几号）
            orderSettingMap.put("number", orderSetting.getNumber());//可预约人数
            orderSettingMap.put("reservations", orderSetting.getReservations());//已预约人数
            data.add(orderSettingMap);
        }
        return data;
    }

    //根据日期修改可预约人数
    public void editNumberByDate(OrderSetting orderSetting) {
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if (count > 0) {
            //当前日期已经进行了预约设置，需要进行修改操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        } else {
            //当前日期没有进行预约设置，进行添加操作
            orderSettingDao.add(orderSetting);
        }
    }
//
//    Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
//ca.setTime(new Date()); //设置时间为当前时间
//ca.add(Calendar.YEAR, -1); //年份减1
//    Date lastMonth = ca.getTime(); //结果
    //求前一月ca.add(Calendar.MONTH, -1)，
//前一天ca.add(Calendar.DATE, -1)
    //定时清除旧数据
    @Override
    public void clearOrderSetting(Date date) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,-3);
        Date time = calendar.getTime();
        String date2String = DateUtils.parseDate2String(time, "yyyy-MM-dd");

        orderSettingDao.clearOrderSetting(date2String);
    }



}


