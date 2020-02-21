package com.leyou.test;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.common.utils.CodecUtils;
import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.leyou.user.mapper.UserMapper;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DemoTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;


    @Test
    public void test() {
        User user = new User();
        /*一次性*/
        user.setUsername("lisi");
        user.setPassword("123456");
        //验证码校验通过则生成盐,并且设置到用户上
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);

        //使用盐对密码进行加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));

        // 强制设置不能指定的参数为null
        user.setId(null);
        user.setCreated(new Date());

        //将用户信息添加到数据库
        Boolean flag = this.userMapper.insertSelective(user) == 1;
        if (flag) {
            User user1 = userService.queryUser(user.getUsername(), user.getPassword());
            System.out.println(user1.getUsername() + ":" + user1.getPassword());
        }

    }

    @Test
    public void test2() {
        User recode = new User();
        recode.setUsername("lisi");

        User user = userMapper.selectOne(recode);
        recode.setPassword("123456");
        //校验用户名
        if (user == null) {
            System.out.println("user = null");
        }

        //校验密码
        if (!user.getPassword().equals(CodecUtils.md5Hex(recode.getPassword(), user.getSalt()))) {
            System.out.println("验证失败");
        }

        System.out.println(user);
    }
}

