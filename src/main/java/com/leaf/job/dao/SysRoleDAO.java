package com.leaf.job.dao;

import java.util.List;

import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.entity.SysRoleEntity;

public interface SysRoleDAO {

	/**
	 * Load Reference Entity
	 * @param id
	 * @return {@link SysRoleEntity}
	 */
    SysRoleEntity loadSysRoleEntity(long id);

	/**
	 * Find Entity from DB By Id
	 * @param id
	 * @return {@link SysRoleEntity}
	 */
    SysRoleEntity findSysRoleEntity(long id);

	/**
	 * Find Entity from DB By Code
	 * @param id
	 * @return {@link SysRoleEntity}
	 */
    SysRoleEntity findSysRoleEntityByCode(String code);
    
    /**
     * Save SysRole Entity
     * @param sysRoleEntity
     */
    void saveSysRoleEntity(SysRoleEntity sysRoleEntity);
    
    /**
     * Update SysRole Entity
     * @param sysRoleEntity
     */
    void updateSysRoleEntity(SysRoleEntity sysRoleEntity);
    
    /**
     * Find All Not Deleted System Roles
     * @return {@link List}
     */
    List<SysRoleEntity> findAllSysRoleEntities();
    
    /**
     * Getting Data For  Grid 
     * @param dataTableRequestDTO
     * @return 
     */
    <T> T getDataForGrid(DataTableRequestDTO dataTableRequestDTO, String type);
}
