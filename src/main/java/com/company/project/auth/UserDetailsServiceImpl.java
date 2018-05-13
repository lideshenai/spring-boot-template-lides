package com.company.project.auth;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.company.project.dao.PermissionMapper;
import com.company.project.dao.UserMapper;
import com.company.project.model.Permission;
import com.company.project.model.User;
import com.company.project.service.UserService;


/**
 * spring security登陆处理
 * 
 * @author 小威老师
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserService userService;
	@Resource
	private UserMapper userMapperr;
	@Resource
	private PermissionMapper permissionMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if(StringUtils.isBlank(username)) {
			throw new AuthenticationCredentialsNotFoundException("用户名不能为空");
		}
		User sysUser = userService.findBy("username", username);
		if (sysUser == null) {
			throw new AuthenticationCredentialsNotFoundException("用户名不存在");
		} /*else if (sysUser.getStatus() == Status.LOCKED) {
			throw new LockedException("用户被锁定,请联系管理员");
		} else if (sysUser.getStatus() == Status.DISABLED) {
			throw new DisabledException("用户已作废");
		}*/		
		LoginUser loginUser = new LoginUser();
		BeanUtils.copyProperties(sysUser, loginUser);	
		List<Permission> permissions = permissionMapper.listByUserId(loginUser.getId());		
		loginUser.setPermissions(permissions);
		return loginUser;
	}

}
