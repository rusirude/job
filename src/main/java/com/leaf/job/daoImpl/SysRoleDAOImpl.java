package com.leaf.job.daoImpl;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.leaf.job.dao.SysRoleDAO;
import com.leaf.job.entity.SysRoleEntity;
import com.leaf.job.enums.DeleteStatusEnum;

@Repository
public class SysRoleDAOImpl implements SysRoleDAO {
	
	@Autowired
	private EntityManager entityManager;

	@Override
	public SysRoleEntity loadSysRoleEntity(long id) {
		return entityManager.getReference(SysRoleEntity.class, id);
	}

	@Override
	public SysRoleEntity findSysRoleEntity(long id) {
		return entityManager.find(SysRoleEntity.class, id);
	}

	@Override
	public SysRoleEntity findSysRoleEntityByCode(String code) {
		SysRoleEntity sysRoleEntity = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SysRoleEntity> criteriaQuery = criteriaBuilder.createQuery(SysRoleEntity.class);
        Root<SysRoleEntity> root = criteriaQuery.from(SysRoleEntity.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
                criteriaBuilder.and(
                      //criteriaBuilder.equal(root.get(SysRole_.code), code),
                      //  criteriaBuilder.notEqual(root.get(SysRole_.status).get(Status_.code), DeleteStatusEnum.DELETE.getCode())
                )
        );

        try {
            sysRoleEntity = entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (Exception e) {
            System.err.println(e);
        }

        return sysRoleEntity;
	}

}
