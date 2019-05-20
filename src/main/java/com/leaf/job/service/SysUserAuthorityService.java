package com.leaf.job.service;

import java.util.HashMap;

import com.leaf.job.dto.SysRoleAuthorityDTO;
import com.leaf.job.dto.SysRoleDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.ResponseDTO;

public interface SysUserAuthorityService {

	/**
	 * Get All related and non related System User Authorities For System User.
	 * related authorities are marked as enable true other are false
	 * 
	 * @param sysRoleDTO
	 * @return{@link DataTableResponseDTO}
	 */
//	DataTableResponseDTO getSysRoleAuthorityForSysRole(SysRoleDTO sysRoleDTO);
	
	/**
	 * Getting Reference Data For SysRole Authority Page
	 * @return {@link ResponseDTO<HashMap<String, Object>>}
	 */
	ResponseDTO<HashMap<String, Object>> getReferenceDataForSysUserAuthority();
	
	/**
	 * Save SysRole Authority
	 * 
	 * @param sysRoleAuthorityDTO
	 * @return {@link ResponseDTO<SysRoleAuthorityDTO>}
	 */
//	ResponseDTO<SysRoleAuthorityDTO> saveSysRoleAuthority(SysRoleAuthorityDTO sysRoleAuthorityDTO);
	
	/**
	 * Delete SysRole Authority
	 * 
	 * @param sysRoleAuthorityDTO
	 * @return {@link ResponseDTO<SysRoleAuthorityDTO>}
	 */
//	ResponseDTO<SysRoleAuthorityDTO> deleteSysRoleAuthority(SysRoleAuthorityDTO sysRoleAuthorityDTO);
}
