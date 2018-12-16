package com.leaf.job.service;

import java.util.List;

import com.leaf.job.dto.SysRoleAuthorityDTO;
import com.leaf.job.dto.SysRoleDTO;

public interface SysRoleAuthorityService {

	/**
	 * Get All related and non related System Role Authorities For System Role.
	 * related authorities are marked as enable true other are false
	 * 
	 * @param sysRoleDTO
	 * @return{@link List<SysRoleAuthorityDTO>}
	 */
	List<SysRoleAuthorityDTO> getSysRoleAuthorityForSysRole(SysRoleDTO sysRoleDTO);
}
