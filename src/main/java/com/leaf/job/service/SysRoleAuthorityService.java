package com.leaf.job.service;

import java.util.HashMap;
import java.util.List;

import com.leaf.job.dto.SysRoleAuthorityDTO;
import com.leaf.job.dto.SysRoleDTO;
import com.leaf.job.dto.common.ResponseDTO;

public interface SysRoleAuthorityService {

	/**
	 * Get All related and non related System Role Authorities For System Role.
	 * related authorities are marked as enable true other are false
	 * 
	 * @param sysRoleDTO
	 * @return{@link List<SysRoleAuthorityDTO>}
	 */
	List<SysRoleAuthorityDTO> getSysRoleAuthorityForSysRole(SysRoleDTO sysRoleDTO);
	
	/**
	 * Getting Reference Data For SysRole Authority Page
	 * @return {@ ResponseDTO<HashMap<String, Object>>}
	 */
	ResponseDTO<HashMap<String, Object>> getReferenceDataForSysRoleAuthority();
}
