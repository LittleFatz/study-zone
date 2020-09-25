package com.littlefatz.springboot.dao;

import com.littlefatz.springboot.entity.Role;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleDao {

    @Insert("insert into role(id,name) values(#{id},#{name})")
    void insert(Role role);

    @Select("select * from role")
    List<Role> findAll();

    @Select("select * from role r, user_role ur where r.id=ur.role_id and ur.user_id=#{id}")
    List<Role> findByUserId(int id);
}
