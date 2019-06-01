package com.leaf.job.dao;

import java.util.List;

import com.leaf.job.entity.SysUserAuthorityEntity;

public interface SysUserAuthorityDAO {
	
	/**
	 * Select - *
	 * From - SysUserAuthorityEntity
	 * Where - SYS_USER  = username
	 * 
	 * Select all SysUserAuthorityEntity for SYSUSER (username)
	 * @param username
	 * @return {@link List<SysRoleAuthorityEntity>]
	 */
	List<SysUserAuthorityEntity> getSysUserAuthorityEntitiesBySysUser(String username);

}
