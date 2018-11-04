package com.leaf.job.dao;

import com.leaf.job.entity.SysRoleEntity;

public interface SysRoleDAO {

    SysRoleEntity loadSysRoleEntity(long id);

    SysRoleEntity findSysRoleEntity(long id);

    SysRoleEntity findSysRoleEntityByCode(String code);

}
