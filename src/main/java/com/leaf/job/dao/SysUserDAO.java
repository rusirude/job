package com.leaf.job.dao;

import com.leaf.job.entity.SysUserEntity;

public interface SysUserDAO {
	
	
	/**
	 * Find Entity from DB By Username
	 * @param username
	 * @return {@link SysUserEntity}
	 */
	SysUserEntity getSysUserEntityByUsername(String username);
	
    
    /**
     * Save SysUser Entity
     * @param sysUserEntity
     */
    void saveSysUserEntity(SysUserEntity sysUserEntity);
	
	
}
