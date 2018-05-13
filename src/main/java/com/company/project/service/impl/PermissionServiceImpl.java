package com.company.project.service.impl;

import com.company.project.dao.PermissionMapper;
import com.company.project.model.Permission;
import com.company.project.service.PermissionService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/05/13.
 */
@Service
@Transactional
public class PermissionServiceImpl extends AbstractService<Permission> implements PermissionService {
    @Resource
    private PermissionMapper sysPermissionMapper;

}
