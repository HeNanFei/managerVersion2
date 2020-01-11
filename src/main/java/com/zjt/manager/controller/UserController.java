package com.zjt.manager.controller;
import com.alibaba.fastjson.JSONObject;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.scenario.effect.Identity;
import com.zjt.manager.mapper.UroleMapper;
import com.zjt.manager.mapper.UserMapper;
import com.zjt.manager.pojo.User;
import com.zjt.manager.service.UroleService;
import com.zjt.manager.service.UserService;
import com.zjt.manager.util.Bcry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class UserController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UroleService uroleService;


   @RequestMapping("/user/add")
   public String add(String user1){


       List<Integer> ridList = new ArrayList<>();

       User user2 = JSONObject.parseObject(user1, User.class);
       String type = user2.getType();




       System.out.println("string type"+type.substring(1, type.length() - 1));
       String[] split = type.substring(1, type.length() - 1).split(",");

       System.out.println("spilt"+ split.toString());
       //获取用户角色标识
      for(int i=0;i<split.length;i++){
          String strings = split[i];
          String subString = strings.substring(1,strings.length()-1);

          //System.out.println("角色标识啊"+ Integer.valueOf(strings));
        int i2 = Integer.parseInt(subString);
          System.out.println("转换后的角色标识"+i2);
          ridList.add(i2);

      }

       String password = Bcry.bcry(user2.getPassword());
       user2.setPassword(password);
       userService.insertUser(user2);

       //为用户添加相关角色信息
       User user = userService.selectUserByName(user2.getUsername());
       Integer uid = user.getUid();

       //用户角色插入
       uroleService.insertURole(uid,ridList);








       return "添加成功";
   }
    int temp = 0;
   @RequestMapping("/user/list")
    public Map list(Integer page,Integer limit){
       List<User> list1 = userMapper.selectByLimit((page-1)*limit, limit-1);
       List<User> list = userService.list();

       /*PageHelper.startPage(page,limit);
       List<User> list = userService.list();
       PageInfo<User> pageInfo = new PageInfo<>(list);

       List<User> list1 = pageInfo.getList();*/

       Map<String,Object> map = new HashMap<>();
       map.put("code",0);
       map.put("msg","");
       map.put("count",list.size());
       map.put("data",list1);

       return map;
   }





}
