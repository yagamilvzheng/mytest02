package com.itheima.health.dao;

import java.util.Map;

public interface ReportDao {
    Map<String,Object> getBusinessReport()throws Exception;
}
