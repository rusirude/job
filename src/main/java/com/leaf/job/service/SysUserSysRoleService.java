package com.leaf.job.service;

import java.util.HashMap;

import com.leaf.job.dto.SysUserDTO;
import com.leaf.job.dto.SysUserSysRoleDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.ResponseDTO;

public interface SysUserSysRoleService {

	/**
	 * Get All related and non related System Roles  For System user.
	 * related system roles are marked as enable true other are false
	 * 
	 * @param sysUserDTO
	 * @return{@link DataTableResponseDTO}
	 */
	DataTableResponseDTO getSysUserSysRoleForSysUser(SysUserDTO sysUserDTO);
	
	/**
	 * Getting Reference Data For System User's System Role Page
	 * @return {@link ResponseDTO<HashMap<String, Object>>}
	 */
	ResponseDTO<HashMap<String, Object>> getReferenceDataForSysUserSysRole();
	
	/**
	 * Save SysUser SysRole
	 * 
	 * @param SysUserSysRoleDTO
	 * @return {@link ResponseDTO<SysUserSysRoleDTO>}
	 */
	ResponseDTO<SysUserSysRoleDTO> saveSysUserSysRole(SysUserSysRoleDTO sysUserSysRoleDTO);
	
	/**
	 * Delete SysUser SysRole
	 * 
	 * @param SysUserSysRoleDTO
	 * @return {@link ResponseDTO<SysUserSysRoleDTO>}
	 */
	ResponseDTO<SysUserSysRoleDTO> deleteSysUserSysRole(SysUserSysRoleDTO sysUserSysRoleDTO);
}
