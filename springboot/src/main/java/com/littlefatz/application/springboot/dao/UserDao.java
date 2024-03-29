package com.littlefatz.application.springboot.dao;

import com.littlefatz.application.springboot.entity.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;


import java.util.List;

@Mapper
public interface UserDao {

    @Select("select * from user")
    List<User> findAll();

    @Select("select * from user where name=#{name}")
    List<User> findAllByName(String name);

    @Select("select * from user where id=#{id}")
    User findById(int id);

    @Insert("insert into user(id,name,age,money) values(#{id},#{name},#{age},#{money})")
    void insert(User user);

    @Delete("delete from user where id=#{id}")
    void delete(int id);

    @Update("update user set name=#{name}, age=#{age}, money=#{money} where id=#{id}")
    void update(User user);

    @SelectProvider(type = UserDaoProvider.class, method = "findByNotNullField")
    List<User> findByNotNullField(User user);

    @Select("select * from user where id=#{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "age", column = "age"),
            @Result(property = "money", column = "money"),
            @Result(property = "roles", column = "id",
                    javaType = List.class, many = @Many(select = "com.littlefatz.springboot.dao.RoleDao.findByUserId"))
    })
    List<User> findUserAndRole(int id);

    class UserDaoProvider {

        public String findByNotNullField(User user) {

            SQL sql = new SQL().SELECT("*").FROM("user");
            if (user.getAge() != 0) {
                sql.WHERE("age = #{age}");
            }

            sql.OR();
            if (user.getName() != null) {
                sql.WHERE("name = #{name}");
            }

            return sql.toString();
        }
    }
}
