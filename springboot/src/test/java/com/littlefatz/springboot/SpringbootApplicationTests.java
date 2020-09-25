package com.littlefatz.springboot;

import com.littlefatz.springboot.dao.OrderDao;
import com.littlefatz.springboot.dao.RoleDao;
import com.littlefatz.springboot.dao.UserDao;
import com.littlefatz.springboot.dao.UserRoleDao;
import com.littlefatz.springboot.entity.Order;
import com.littlefatz.springboot.entity.Role;
import com.littlefatz.springboot.entity.User;
import com.littlefatz.springboot.entity.UserRole;
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

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private OrderDao orderDao;

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




        List<User> users = userDao.findUserAndRole(1);
        for (int i = 0; i < users.size(); i++) {
            System.out.println(users.get(i));
        }
        List<User> users2 = userDao.findUserAndRole(1);
    }

    @Test
    public void testRole() {
        Role role = new Role();
        role.setId(1);
        role.setName("SE");
        roleDao.insert(role);
    }

    @Test
    public void testUserRole() {
        UserRole userRole = new UserRole();
        userRole.setRoleId(2);
        userRole.setUserId(1);
        userRoleDao.insert(userRole);

        Role role = new Role();
        role.setId(2);
        role.setName("SSE");
        roleDao.insert(role);
    }

    @Test
    public void testOrder() {
//        Order newOrder = new Order();
//        newOrder.setId(1);
//        newOrder.setDescription("test");
//        newOrder.setValue(100);
//        User user = new User();
//        user.setId(1);
//        newOrder.setUser(user);

//
//        orderDao.insert(newOrder);


        Order order = orderDao.findOrderAndUser(1);
        System.out.println(order);
    }

}
