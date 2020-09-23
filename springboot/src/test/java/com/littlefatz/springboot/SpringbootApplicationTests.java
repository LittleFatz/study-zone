package com.littlefatz.springboot;

import com.littlefatz.springboot.dao.RoleDao;
import com.littlefatz.springboot.dao.UserDao;
import com.littlefatz.springboot.entity.Role;
import com.littlefatz.springboot.entity.User;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@MapperScan("com.littlefatz.sprintboot.dao")
class SpringbootApplicationTests {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Test
    void test() {
        User user = new User();
//        user.setId(2);
        user.setAge(20);
//        user.setMoney(100);
        user.setName("aden");
//
//        userDao.insert(user);
//        List<User> users = userDao.findAll();
//        for (int i = 0; i < users.size(); i++) {
//            System.out.println(users.get(i));
//        }
        List<User> users = userDao.findByNotNullField(user);
        for (int i = 0; i < users.size(); i++) {
            System.out.println(users.get(i));
        }
    }

    @Test
    public void testRole() {
        Role role = new Role();
        role.setId(1);
        role.setName("SE");
    }

}
