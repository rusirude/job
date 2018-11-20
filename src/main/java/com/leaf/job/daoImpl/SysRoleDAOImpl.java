package com.leaf.job.daoImpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.leaf.job.dao.SysRoleDAO;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.entity.StatusEntity_;
import com.leaf.job.entity.SysRoleEntity;
import com.leaf.job.entity.SysRoleEntity_;
import com.leaf.job.enums.DeleteStatusEnum;

@Repository
public class SysRoleDAOImpl implements SysRoleDAO {
	
	@Autowired
	private EntityManager entityManager;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SysRoleEntity loadSysRoleEntity(long id) {
		return entityManager.getReference(SysRoleEntity.class, id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SysRoleEntity findSysRoleEntity(long id) {
		return entityManager.find(SysRoleEntity.class, id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SysRoleEntity findSysRoleEntityByCode(String code) {
		SysRoleEntity sysRoleEntity = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SysRoleEntity> criteriaQuery = criteriaBuilder.createQuery(SysRoleEntity.class);
        Root<SysRoleEntity> root = criteriaQuery.from(SysRoleEntity.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
                criteriaBuilder.and(
                      criteriaBuilder.equal(root.get(SysRoleEntity_.code), code),
                      criteriaBuilder.notEqual(root.get(SysRoleEntity_.statusEntity).get(StatusEntity_.code), DeleteStatusEnum.DELETE.getCode())
                )
        );

        try {
            sysRoleEntity = entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (Exception e) {
            System.err.println(e);
        }

        return sysRoleEntity;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveSysRoleEntity(SysRoleEntity sysRoleEntity) {
		entityManager.persist(sysRoleEntity);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateSysRoleEntity(SysRoleEntity sysRoleEntity) {
		entityManager.merge(sysRoleEntity);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SysRoleEntity> findAllSysRoleEntities(){
		List<SysRoleEntity> sysRoleEntities = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SysRoleEntity> criteriaQuery = criteriaBuilder.createQuery(SysRoleEntity.class);
        Root<SysRoleEntity> root = criteriaQuery.from(SysRoleEntity.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
        		criteriaBuilder.notEqual(root.get(SysRoleEntity_.statusEntity).get(StatusEntity_.code), DeleteStatusEnum.DELETE.getCode())
        );

        try {
        	sysRoleEntities = entityManager.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            System.err.println(e);
        }
        
        return sysRoleEntities;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T getDataForGrid(DataTableRequestDTO dataTableRequestDTO) {
		
		return null;
	}


}
