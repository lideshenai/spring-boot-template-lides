package com.company.project.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.company.project.core.Mapper;
import com.company.project.model.Permission;

public interface PermissionMapper extends Mapper<Permission> {
	//查询所有权限
	@Select("select * from sys_permission t order by t.sort")
	List<Permission> listAll();
	//查询类型为1的权限
	@Select("select * from sys_permission t where t.type = 1 order by t.sort")
	List<Permission> listParents();
	//通过用户id查找权限
	@Select("select distinct p.* from sys_permission p inner join sys_role_permission rp on p.id = rp.permissionId inner join sys_role_user ru on ru.roleId = rp.roleId where ru.userId = #{userId} order by p.sort")
	List<Permission> listByUserId(Integer userId);
	//通过角色查找id
	@Select("select p.* from sys_permission p inner join sys_role_permission rp on p.id = rp.permissionId where rp.roleId = #{roleId} order by p.sort")
	List<Permission> listByRoleId(Integer roleId);
}