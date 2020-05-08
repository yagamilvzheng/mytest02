package com.itheima.health.dao;

import com.itheima.health.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberDao {
    Member findByTelephone(String telephone);

    void add(Member member);

    Integer findMemberCountBeforeDate(String date);

    Integer findMemberCountByDate(String today);

    Integer findMemberTotalCount();

    Integer findMemberCountAfterDate(String thisWeekMonday);

    List<Map<String,Integer>> findMemberBySex();
}
