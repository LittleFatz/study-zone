package com.littlefatz.application.springboot.dao;

import com.littlefatz.application.springboot.entity.UserRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleDao {

    @Insert("insert into user_role(user_id, role_id) values(#{userId}, #{roleId})")
    void insert(UserRole userRole);
}
