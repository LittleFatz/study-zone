package com.littlefatz.application.springboot.dao;

import com.littlefatz.application.springboot.entity.Order;
import com.littlefatz.application.springboot.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderDao {

    @Insert("insert into `order`(id,user_id,description,value) values(#{id},#{user.id},#{description},#{value})")
    void insert(Order order);

    @Select("select * from `order` where id=#{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "description", column = "description"),
            @Result(property = "value", column = "value"),
            @Result(property = "user", column = "user_id",
                    javaType = User.class, one = @One(select = "com.littlefatz.springboot.dao.UserDao.findById"))
    })
    Order findOrderAndUser(int id);
}
