package com.littlefatz.application.springboot;

import com.littlefatz.application.springboot.dao.OrderDao;
import com.littlefatz.application.springboot.dao.RoleDao;
import com.littlefatz.application.springboot.dao.UserDao;
import com.littlefatz.application.springboot.dao.UserRoleDao;
import com.littlefatz.application.springboot.entity.Order;
import com.littlefatz.application.springboot.entity.Role;
import com.littlefatz.application.springboot.entity.User;
import com.littlefatz.application.springboot.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
//        User user = new User();
////        user.setId(2);
//        user.setAge(20);
////        user.setMoney(100);
//        user.setName("aden");
//
//        userDao.insert(user);
//        List<User> users = userDao.findAll();
//        for (int i = 0; i < users.size(); i++) {
//            System.out.println(users.get(i));
//        }


//        List<User> users = userDao.findUserAndRole(1);
//        for (int i = 0; i < users.size(); i++) {
//            System.out.println(users.get(i));
//        }
//        List<User> users2 = userDao.findUserAndRole(1);

        User user1 = userDao.findById(1);
        User user2 = userDao.findById(1);
        System.out.println(user1 == user2);
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

//    @Test
//    public void test1() {
//        SqlSession sqlSession = sessionFactory.openSession();
//        UserDao userMapper = sqlSession.getMapper(UserDao.class);
//    }

}
