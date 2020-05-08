package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class MemberController {
    @Reference
    MemberService memberService;

    @RequestMapping("/getmembersex")
    public Result findMemberBySex(){

        ArrayList<Map<String,Object>> list1=new ArrayList<>();

        List<Map<String,Object>> list = memberService.findMemberBySex();  //[{"1",9},{"2",10}]
        for (Map map1 : list) {
            Map<String,Object> map3 = new HashMap<>();
            String name = ( String ) map1.get("name");
            Long value = ( Long ) map1.get("value");
            if("1".equals(name)){
                name="男";
            }
            if("2".equals(name)){
                name="女";
            }
            map3.put("name",name);
            map3.put("value",value);
            list1.add(map3);
        }



        return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,list1);
    }


}
