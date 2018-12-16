package com.leaf.job.dao;

import java.util.List;

import com.leaf.job.entity.SysRoleAuthorityEntity;
import com.leaf.job.entity.SysRoleEntity;

public interface SysRoleAuthorityDAO {
	
	/**
	 * Select - *
	 * From - SysRoleAuthorityEntity
	 * Where - Authority STATUS <> INACTIVE and SYS_ROLE STATUS<> INACTIVE and SYS_ROLE IN (sysRoleEntities)
	 * 
	 * Select all SysRoleAuthorityEntity for SysRole Entities and Authority and SYSROLE both Status are ACTIVE
	 * @param sysRoleEntities
	 * @return List<SysRoleAuthorityEntity>
	 */
	List<SysRoleAuthorityEntity> getSysRoleAuthorityEntitiesBySysRoles(List<SysRoleEntity> sysRoleEntities);
	
	/**
	 * Select - *
	 * From - SysRoleAuthorityEntity
	 * Where - Authority STATUS <> INACTIVE and SYS_ROLE =  SYS_ROLE_ID
	 * 
	 * Select all SysRoleAuthorityEntity for SysRole Entity and AuthorityStatus is ACTIVE
	 * @param sysRoleId
	 * @return List<SysRoleAuthorityEntity>
	 */
	List<SysRoleAuthorityEntity> getSysRoleAuthorityEntitiesBySysRole(long sysRoleId);
	
	/**
	 * where Authority = authorityId
	 * 
	 * Delete Entities by Authority
	 * @param authorityId
	 */
	void deleteSysRoleAuthorityEntityByAuthority(long authorityId);
	
	/**
	 * where SysRole = sysRoleId
	 * 
	 * Delete Entities by SysRole
	 * @param sysRoleId
	 */
	void deleteSysRoleAuthorityEntityBySysRole(long sysRoleId);
	
	
}
