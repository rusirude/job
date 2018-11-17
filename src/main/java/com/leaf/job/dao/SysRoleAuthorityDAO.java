package com.leaf.job.dao;

import java.util.List;

import com.leaf.job.entity.SysRoleAuthorityEntity;
import com.leaf.job.entity.SysRoleEntity;

public interface SysRoleAuthorityDAO {
	List<SysRoleAuthorityEntity> getSysRoleAuthorityEntitiesBySysRoles(List<SysRoleEntity> sysRoleEntities);
}
