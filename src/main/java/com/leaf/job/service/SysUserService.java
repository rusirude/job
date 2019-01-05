package com.leaf.job.service;

import java.util.HashMap;

import com.leaf.job.dto.common.ResponseDTO;

public interface SysUserService {

	/**
	 * Load Reference Data For SysUser Page
	 * @return {@link ResponseDTO}
	 */
	ResponseDTO<HashMap<String, Object>> getReferenceDataForSysUser();
}
