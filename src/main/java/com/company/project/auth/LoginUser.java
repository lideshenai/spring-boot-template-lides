package com.company.project.auth;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.company.project.model.Permission;
import com.company.project.model.User;

public class LoginUser extends User implements UserDetails{

	private static final long serialVersionUID = 2220747866633308729L;
	private List<Permission> permissions;
	
	public List<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}
	//获取权限集合
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return permissions.parallelStream().filter(p->StringUtils.isNotEmpty(p.getPermission()))
		.map(p->new SimpleGrantedAuthority(p.getPermission())).collect(Collectors.toSet());		
	}
	
	//校验帐户是否过期
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	//校验帐户是否锁定
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		//未被锁定
		return true;
	}
	//校验密码是否过期
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	//帐户是否激活
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		//激活
		return true;
	}
	
}
