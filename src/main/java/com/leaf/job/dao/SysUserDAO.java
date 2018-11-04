package com.leaf.job.dao;

import com.leaf.job.entity.SysUserEntity;

public interface SysUserDAO {
	
	SysUserEntity getSysUserEntityByUsername(String username);
}
