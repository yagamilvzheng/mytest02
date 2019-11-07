package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.List;
import java.util.Map;
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
    @Override
    public Result order(Map map) throws Exception {
        //一：手机验证码比对
        //1：获取页面传递的手机号和验证码
        //2：从Redis中，通过手机号获取redis中存储的验证码
        //3：页面输入的验证码和redis中的验证码进行比对，如果比对不成功，提示：“验证码输入有误”
        //二：完成体检预约


        try {
            //1：使用体检日期，查询预约设置表，判断当前时间在预约设置表中是否存在数据，返回OrderSetting，如果没有数据，提示：“当前时间不可以进行预约”
            String orderDate = ( String ) map.get("orderDate");
            Date date = DateUtils.parseString2Date(orderDate);
            OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
            if (orderSetting==null){
                return  new Result(false,MessageConstant.SELECTED_DATE_CANNOT_ORDER);
            }
            //2：从OrderSetting中获取number（预约最大人数），获取reservations（已经预约的人数），如果reservations>=number，提示：“预约已满”
            int number = orderSetting.getNumber();
            int reservations = orderSetting.getReservations();
            if (reservations>=number){
                return  new Result(false,MessageConstant.ORDER_FULL);
            }
            //3：获取手机号，使用手机号作为查询条件，查询会员表，判断当前预约人是否是会员
            String telephone = ( String ) map.get("telephone");
            Member member = memberDao.findByTelephone(telephone);
            //如果是会员，使用会员id+预约时间+套餐id作为查询条件，查询体检预约订单表，判断当前时间是否重复预约，如果在预约订单表中存在数据，提示：“不能重复预约”
            if (member != null){
                Integer memberId = member.getId();
                int setmealId = Integer.parseInt(( String ) map.get("setmealId"));
                Order order = new Order(memberId,date,null,null,setmealId);
                List<Order> orderList = orderDao.findByCondition(order);
                if (orderList !=null && orderList.size()>0){
                    return new Result(false,MessageConstant.HAS_ORDERED);
                }
            }
            //如果不是会员，注册会员
            member = new Member();
            member.setName(( String ) map.get("name"));
            member.setSex(( String ) map.get("sex"));
            member.setPhoneNumber(telephone);
            member.setIdCard(( String ) map.get("idCard"));
            member.setRegTime(new Date());
            memberDao.add(member);
            //4：向预约订单表中添加数据，表示预约完成
            Order order = new Order(member.getId(),date,(String)map.get("orderType"),Order.ORDERSTATUS_NO,Integer.parseInt(( String ) map.get("setmealId")));
            orderDao.add(order);

            //5：更新预约设置表，使得reservations字段+1
            orderSettingDao.editReservationsByOrderDate(date);

            return new Result(true,MessageConstant.ORDER_SUCCESS,order);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("提交预约出错");
        }
    }

    //根据id查询预约信息，包括体检人信息、套餐信息
    public Map findById4Detail(Integer id)  {
        Map map = orderDao.findById4Detail(id);
        if(map != null){
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            try {
                map.put("orderDate",DateUtils.parseDate2String(orderDate));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }
        return map;
    }
}
