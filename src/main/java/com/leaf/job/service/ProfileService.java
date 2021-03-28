package com.leaf.job.service;

import com.leaf.job.dto.SysUserDTO;
import com.leaf.job.dto.common.ResponseDTO;

import java.util.HashMap;

public interface ProfileService {
    ResponseDTO<HashMap<String, Object>> getReferenceDataForProfile();

    ResponseDTO<?> saveProfile(SysUserDTO SysUserDTO);
}
