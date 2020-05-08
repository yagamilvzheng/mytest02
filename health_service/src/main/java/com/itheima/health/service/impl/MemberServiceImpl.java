package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Transactional
@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberDao memberDao;
    @Override
    public List<Integer> findMemberCountByMouth(List<String> mouths) {
        List<Integer> list = new ArrayList<Integer>();
        for (String mouth : mouths) {
            mouth= mouth +"-32";
           Integer count = memberDao.findMemberCountBeforeDate(mouth);
           list.add(count);
        }
        return list;
    }

    @Override
    public List findMemberBySex() {
       List<Map<String,Integer>> memberBySex = memberDao.findMemberBySex();

        return memberBySex;
    }
}
