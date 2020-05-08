package com.itheima.health.service;

import java.util.List;

public interface MemberService {
    List<Integer> findMemberCountByMouth(List<String> list);

    List findMemberBySex();
}
