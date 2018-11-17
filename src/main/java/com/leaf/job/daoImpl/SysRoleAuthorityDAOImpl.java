package com.leaf.job.daoImpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.leaf.job.dao.SysRoleAuthorityDAO;
import com.leaf.job.entity.SysRoleAuthorityEntity;
import com.leaf.job.entity.SysRoleAuthorityEntity_;
import com.leaf.job.entity.SysRoleEntity;
import com.leaf.job.entity.SysRoleEntity_;

@Repository
public class SysRoleAuthorityDAOImpl implements SysRoleAuthorityDAO {

	@Autowired
	private EntityManager entityManager;
	
	@Override
	public List<SysRoleAuthorityEntity> getSysRoleAuthorityEntitiesBySysRoles(List<SysRoleEntity> sysRoleEntities) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SysRoleAuthorityEntity> criteriaQuery = criteriaBuilder.createQuery(SysRoleAuthorityEntity.class);
        Root<SysRoleAuthorityEntity> root = criteriaQuery.from(SysRoleAuthorityEntity.class);
        criteriaQuery.select(root).distinct(true);
        CriteriaBuilder.In<Long> sysRoleIn = criteriaBuilder.in(root.get(SysRoleAuthorityEntity_.sysRoleEntity).get(SysRoleEntity_.id));
        sysRoleEntities.forEach(sysRole -> {
            sysRoleIn.value(sysRole.getId());
        });
        criteriaQuery.where(sysRoleIn);
        return entityManager.createQuery(criteriaQuery).getResultList();
	}

}
