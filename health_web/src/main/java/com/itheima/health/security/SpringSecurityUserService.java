package com.itheima.health.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    UserService userService;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //远程调用用户服务，根据用户名查询用户信息
        com.itheima.health.pojo.User user = userService.findUserByUsername(username);
        if(user == null){
            //用户名不存在，抛出异常UsernameNotFoundException
            return null;
        }
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        Set<Role> roles = user.getRoles();
        if(roles!=null && roles.size()>0){
            for (Role role : roles) {
                list.add(new SimpleGrantedAuthority(role.getKeyword()));// 具有ROLE_ADMIN的角色
                Set<Permission> permissions = role.getPermissions();
                if(permissions!=null && permissions.size()>0){
                    for (Permission permission : permissions) {
                        list.add(new SimpleGrantedAuthority(permission.getKeyword()));// 具有add_checkitem的权限
                    }
                }
            }
        }
        UserDetails userDetails = new User(username,user.getPassword(),list);
        return userDetails;
    }


}
