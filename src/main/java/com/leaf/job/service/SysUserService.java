package com.leaf.job.service;

import java.util.HashMap;

import com.leaf.job.dto.SysUserDTO;
import com.leaf.job.dto.common.ResponseDTO;

public interface SysUserService {
	
	
	/**
	 * Save System User
	 * @param SysUserDTO
	 * @return {@link ResponseDTO}
	 */
	ResponseDTO<SysUserDTO> saveSysUser(SysUserDTO sysUserDTO);

	/**
	 * Load Reference Data For SysUser Page
	 * @return {@link ResponseDTO}
	 */
	ResponseDTO<HashMap<String, Object>> getReferenceDataForSysUser();	
	
}
