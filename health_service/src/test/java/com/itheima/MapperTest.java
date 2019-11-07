package com.itheima;

import com.alibaba.druid.support.spring.stat.SpringStatUtils;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.dao.UserDao;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

@ContextConfiguration(locations = "classpath:applicationContext-dao.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class MapperTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Test
    public void test01(){
        User admin = userDao.findUserByUsername("admin");
        System.out.println(admin);
    }

    @Test
    public void test02(){
        Set<Role> rolesByUserId = roleDao.findRolesByUserId(1);
        System.out.println(rolesByUserId);
    }
}
