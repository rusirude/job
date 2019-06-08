package com.leaf.job.service;

import java.util.HashMap;

import com.leaf.job.dto.SysUserAuthorityDTO;
import com.leaf.job.dto.SysUserDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.ResponseDTO;

public interface SysUserAuthorityService {

	/**
	 * Get All related and non related System User Authorities For System User.
	 * related authorities are marked as enable true other are false
	 * 
	 * @param sysUserDTO
	 * @return{@link DataTableResponseDTO}
	 */
	DataTableResponseDTO getSysUserAuthorityForSysUser(SysUserDTO sysUserDTO);
	
	/**
	 * Getting Reference Data For SysRole Authority Page
	 * @return {@link ResponseDTO<HashMap<String, Object>>}
	 */
	ResponseDTO<HashMap<String, Object>> getReferenceDataForSysUserAuthority();
	
	/**
	 * Save SysUser Authority
	 * 
	 * @param sysUserAuthorityDTO
	 * @return {@link ResponseDTO<SysUserAuthorityDTO>}
	 */
	ResponseDTO<SysUserAuthorityDTO> saveSysUserAuthority(SysUserAuthorityDTO sysUserAuthorityDTO);
	
	/**
	 * Delete SysUserhority
	 * 
	 * @param sysUserAuthorityDTO
	 * @return {@link ResponseDTO<SysUserAuthorityDTO>}
	 */
	ResponseDTO<SysUserAuthorityDTO> deleteSysUserAuthority(SysUserAuthorityDTO sysUsereAuthorityDTO);
}
