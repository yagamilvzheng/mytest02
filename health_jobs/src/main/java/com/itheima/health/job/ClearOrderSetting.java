package com.itheima.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.service.OrderSettingService;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ClearOrderSetting {

    @Reference
    OrderSettingService orderSettingService;

    public void clearOrderSetting() throws Exception {
        Date date = new Date();

        orderSettingService.clearOrderSetting(date);
        System.out.println("每月一度清除旧数据执行"+date);
    }

}
