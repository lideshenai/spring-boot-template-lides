package com.company.project.auth;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
	/*@Autowired
	private PermissionDao permissionDao;*/

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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
		//List<Permission> permissions = permissionDao.listByUserId(sysUser.getId());
		//loginUser.setPermissions(permissions);
		return loginUser;
	}

}
