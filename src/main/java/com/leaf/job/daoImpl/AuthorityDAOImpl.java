package com.leaf.job.daoImpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.leaf.job.dao.AuthorityDAO;
import com.leaf.job.entity.AuthorityEntity;
import com.leaf.job.entity.AuthorityEntity_;
import com.leaf.job.entity.StatusEntity_;

@Repository
public class AuthorityDAOImpl implements AuthorityDAO{
	
	@Autowired
	private EntityManager entityManager;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<AuthorityEntity> findAuthorityEntitiesByStatus(String statusCode) {
		
		List<AuthorityEntity> authoritiesEntities = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AuthorityEntity> criteriaQuery = criteriaBuilder.createQuery(AuthorityEntity.class);
        Root<AuthorityEntity> root = criteriaQuery.from(AuthorityEntity.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
        		criteriaBuilder.equal(root.get(AuthorityEntity_.statusEntity).get(StatusEntity_.code), statusCode)
        );

        try {
        	authoritiesEntities = entityManager.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            System.err.println(e);
        }
        
        return authoritiesEntities;
	}

}
