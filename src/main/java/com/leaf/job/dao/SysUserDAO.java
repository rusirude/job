package com.leaf.job.dao;

import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.entity.SysUserEntity;

public interface SysUserDAO {
	
	
	/**
	 * Find Entity from DB By username
	 * @param username
	 * @return {@link SysUserEntity}
	 */
	SysUserEntity getSysUserEntityByUsername(String username);
	
    
    /**
     * Save SysUser Entity
     * @param sysUserEntity
     */
    void saveSysUserEntity(SysUserEntity sysUserEntity);
    
    /**
     * Update SysUser Entity
     * @param sysUserEntity
     */
    void updateSysUserEntity(SysUserEntity sysUserEntity);
	
    /**
     * Getting Data For  Grid 
     * @param dataTableRequestDTO
     * @return 
     */
    <T> T getDataForGrid(DataTableRequestDTO dataTableRequestDTO, String type);
	
}
