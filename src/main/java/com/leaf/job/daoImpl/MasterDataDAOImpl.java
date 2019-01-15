package com.leaf.job.daoImpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.leaf.job.dao.MasterDataDAO;
import com.leaf.job.entity.MasterDataEntity;
import com.leaf.job.entity.MasterDataEntity_;

@Repository
public class MasterDataDAOImpl implements MasterDataDAO {

	private EntityManager entityManager;	
	
	@Autowired
	public MasterDataDAOImpl(EntityManager entityManager) {		
		this.entityManager = entityManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MasterDataEntity findMasterDataEntityByCode(String code) {
		MasterDataEntity masterDataEntity = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MasterDataEntity> criteriaQuery = criteriaBuilder.createQuery(MasterDataEntity.class);
        Root<MasterDataEntity> root = criteriaQuery.from(MasterDataEntity.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
        		criteriaBuilder.equal(root.get(MasterDataEntity_.code), code)
        );

        try {
        	masterDataEntity = entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (Exception e) {
            System.err.println(e);
        }

        return masterDataEntity;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveMasterDataEntity(MasterDataEntity masterDataEntity) {
		entityManager.persist(masterDataEntity);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateMasterDataEntity(MasterDataEntity masterDataEntity) {
		entityManager.merge(masterDataEntity);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MasterDataEntity> findAllMasterDataEntities() {
		List<MasterDataEntity> masterDAtaEntities = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MasterDataEntity> criteriaQuery = criteriaBuilder.createQuery(MasterDataEntity.class);
        Root<MasterDataEntity> root = criteriaQuery.from(MasterDataEntity.class);
        criteriaQuery.select(root);
        try {
        	masterDAtaEntities = entityManager.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            System.err.println(e);
        }
        
        return masterDAtaEntities;
	}

}
