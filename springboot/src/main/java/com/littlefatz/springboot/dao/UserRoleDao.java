package com.littlefatz.springboot.dao;

import com.littlefatz.springboot.entity.UserRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserRoleDao {

    @Insert("insert into user_role(user_id, role_id) values(#{userId}, #{roleId})")
    void insert(UserRole userRole);
}
