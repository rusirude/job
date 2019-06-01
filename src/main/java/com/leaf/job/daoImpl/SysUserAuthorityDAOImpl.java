package com.leaf.job.daoImpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.leaf.job.dao.SysUserAuthorityDAO;
import com.leaf.job.entity.SysUserAuthorityEntity;
import com.leaf.job.entity.SysUserAuthorityEntity_;
import com.leaf.job.entity.SysUserEntity_;

@Repository
public class SysUserAuthorityDAOImpl implements SysUserAuthorityDAO {
	
	
	@Autowired
	private EntityManager entityManager;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SysUserAuthorityEntity> getSysUserAuthorityEntitiesBySysUser(String username){
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SysUserAuthorityEntity> criteriaQuery = criteriaBuilder.createQuery(SysUserAuthorityEntity.class);
        Root<SysUserAuthorityEntity> root = criteriaQuery.from(SysUserAuthorityEntity.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
        	criteriaBuilder.equal(root.get(SysUserAuthorityEntity_.sysUserEntity).get(SysUserEntity_.username),username)
        );
        return entityManager.createQuery(criteriaQuery).getResultList();
	}

}
