package com.leaf.job.daoImpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.leaf.job.dao.SysRoleAuthorityDAO;
import com.leaf.job.entity.AuthorityEntity_;
import com.leaf.job.entity.StatusEntity_;
import com.leaf.job.entity.SysRoleAuthorityEntity;
import com.leaf.job.entity.SysRoleAuthorityEntityId_;
import com.leaf.job.entity.SysRoleAuthorityEntity_;
import com.leaf.job.entity.SysRoleEntity;
import com.leaf.job.entity.SysRoleEntity_;
import com.leaf.job.enums.DefaultStatusEnum;

@Repository
public class SysRoleAuthorityDAOImpl implements SysRoleAuthorityDAO {

	@Autowired
	private EntityManager entityManager;
	
	/**
	 * {@inheritDoc}
	 */
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
        criteriaQuery.where(
        		criteriaBuilder.and(
        				criteriaBuilder.notEqual(root.get(SysRoleAuthorityEntity_.authorityEntity).get(AuthorityEntity_.statusEntity).get(StatusEntity_.code),DefaultStatusEnum.INACTIVE.getCode()),
        				criteriaBuilder.notEqual(root.get(SysRoleAuthorityEntity_.sysRoleEntity).get(SysRoleEntity_.statusEntity).get(StatusEntity_.code),DefaultStatusEnum.INACTIVE.getCode()),
        				sysRoleIn
        				)        		
        		);
        return entityManager.createQuery(criteriaQuery).getResultList();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SysRoleAuthorityEntity> getSysRoleAuthorityEntitiesBySysRole(long sysRoleId){
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SysRoleAuthorityEntity> criteriaQuery = criteriaBuilder.createQuery(SysRoleAuthorityEntity.class);
        Root<SysRoleAuthorityEntity> root = criteriaQuery.from(SysRoleAuthorityEntity.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
        		criteriaBuilder.and(
        				criteriaBuilder.notEqual(root.get(SysRoleAuthorityEntity_.authorityEntity).get(AuthorityEntity_.statusEntity).get(StatusEntity_.code),DefaultStatusEnum.INACTIVE.getCode()),
        				criteriaBuilder.equal(root.get(SysRoleAuthorityEntity_.sysRoleAuthorityEntityId).get(SysRoleAuthorityEntityId_.sysRole),sysRoleId)
        				));
        return entityManager.createQuery(criteriaQuery).getResultList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteSysRoleAuthorityEntityByAuthority(long authorityId) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaDelete<SysRoleAuthorityEntity> criteriaDelete = criteriaBuilder.createCriteriaDelete(SysRoleAuthorityEntity.class);
        Root<SysRoleAuthorityEntity> root = criteriaDelete.from(SysRoleAuthorityEntity.class);        
        criteriaDelete.where(
        		criteriaBuilder.equal(root.get(SysRoleAuthorityEntity_.authorityEntity).get(AuthorityEntity_.id),authorityId)     		
		);
        
        entityManager.createQuery(criteriaDelete).executeUpdate();			
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteSysRoleAuthorityEntityBySysRole(long sysRoleId) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaDelete<SysRoleAuthorityEntity> criteriaDelete = criteriaBuilder.createCriteriaDelete(SysRoleAuthorityEntity.class);
        Root<SysRoleAuthorityEntity> root = criteriaDelete.from(SysRoleAuthorityEntity.class);        
        criteriaDelete.where(
        		criteriaBuilder.equal(root.get(SysRoleAuthorityEntity_.sysRoleEntity).get(SysRoleEntity_.id),sysRoleId)     		
		);
        
        entityManager.createQuery(criteriaDelete).executeUpdate();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteSysRoleAuthorityEntityBySysRoleAndAuthority(long sysRoleId, long authorityId){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaDelete<SysRoleAuthorityEntity> criteriaDelete = criteriaBuilder.createCriteriaDelete(SysRoleAuthorityEntity.class);
        Root<SysRoleAuthorityEntity> root = criteriaDelete.from(SysRoleAuthorityEntity.class);        
        criteriaDelete.where(
        		criteriaBuilder.and(
        				criteriaBuilder.equal(root.get(SysRoleAuthorityEntity_.sysRoleEntity).get(SysRoleEntity_.id),sysRoleId),   
        				criteriaBuilder.equal(root.get(SysRoleAuthorityEntity_.authorityEntity).get(AuthorityEntity_.id), authorityId)
        				)
        		);        
        entityManager.createQuery(criteriaDelete).executeUpdate();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveSysRoleAuthorityentity(SysRoleAuthorityEntity sysRoleAuthorityEntity){
		entityManager.persist(sysRoleAuthorityEntity);
	}
	

}
