package com.leaf.job.daoImpl;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.leaf.job.dao.SysUserDAO;
import com.leaf.job.entity.SysUserEntity;

@Repository
public class SysUserDAOImpl implements SysUserDAO {

	@Autowired
	private EntityManager entityManager;
	
	@Override
	public SysUserEntity getSysUserEntityByUsername(String username) {
		return entityManager.find(SysUserEntity.class,username);
	}

	@Override
	public void saveSysUserEntity(SysUserEntity sysUserEntity) {
		entityManager.persist(sysUserEntity);
		
	}

}
