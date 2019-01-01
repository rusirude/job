package com.leaf.job.daoImpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.leaf.job.dao.AuthorityDAO;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.entity.AuthorityEntity;
import com.leaf.job.entity.AuthorityEntity_;
import com.leaf.job.entity.SectionEntity_;
import com.leaf.job.entity.StatusEntity_;
import com.leaf.job.entity.SysRoleEntity_;
import com.leaf.job.enums.DeleteStatusEnum;
import com.leaf.job.utility.CommonConstant;

@Repository
public class AuthorityDAOImpl implements AuthorityDAO{
	
	@Autowired
	private EntityManager entityManager;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveAuthorityEntity(AuthorityEntity authorityEntity) {
		entityManager.persist(authorityEntity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateAuthorityEntity(AuthorityEntity authorityEntity) {
		entityManager.merge(authorityEntity);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AuthorityEntity findAuthorityEntityByCode(String code){
		AuthorityEntity authorityEntity = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AuthorityEntity> criteriaQuery = criteriaBuilder.createQuery(AuthorityEntity.class);
        Root<AuthorityEntity> root = criteriaQuery.from(AuthorityEntity.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
                criteriaBuilder.and(
                      criteriaBuilder.equal(root.get(AuthorityEntity_.code), code),
                      criteriaBuilder.notEqual(root.get(AuthorityEntity_.statusEntity).get(StatusEntity_.code), DeleteStatusEnum.DELETE.getCode())
                )
        );

        try {
            authorityEntity = entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (Exception e) {
            System.err.println(e);
        }

        return authorityEntity;
	}

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
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getDataForGrid(DataTableRequestDTO dataTableRequestDTO, String type) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AuthorityEntity> criteriaQuery = criteriaBuilder.createQuery(AuthorityEntity.class);
        Root<AuthorityEntity> root = criteriaQuery.from(AuthorityEntity.class);
        List<Predicate> predicates = createSearchPredicate(dataTableRequestDTO.getSearch(),criteriaBuilder,root);
        criteriaQuery.select(root);
        criteriaQuery.where(
        		criteriaBuilder.and(
        				criteriaBuilder.notEqual(root.get(AuthorityEntity_.statusEntity).get(StatusEntity_.code), DeleteStatusEnum.DELETE.getCode()),
                		criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]))
        		)
        		
        );

        criteriaQuery.orderBy(createSortOrder(dataTableRequestDTO.getSortColumnName(), dataTableRequestDTO.getSortOrder(), criteriaBuilder, root));
        

        if (CommonConstant.GRID_SEARC_COUNT.equals(type)) {
            long count = entityManager.createQuery(criteriaQuery).getResultList().size();
            return (T) Long.valueOf(count);
        } else {
            TypedQuery<AuthorityEntity> typedQuery = entityManager.createQuery(criteriaQuery);
            typedQuery.setFirstResult(dataTableRequestDTO.getStart());
            typedQuery.setMaxResults(dataTableRequestDTO.getLength());
            return (T) typedQuery.getResultList();
        }	
	}
	
	private List<Predicate> createSearchPredicate(String searchValue,CriteriaBuilder cb,Root<AuthorityEntity> root) {
		List<Predicate> predicates = new ArrayList<>();
		if(!searchValue.isEmpty()) {
			predicates.add(cb.like(root.get(AuthorityEntity_.code),"%"+searchValue+"%"));
			predicates.add(cb.like(root.get(AuthorityEntity_.description),"%"+searchValue+"%"));
			predicates.add(cb.like(root.get(AuthorityEntity_.url),"%"+searchValue+"%"));
			predicates.add(cb.like(root.get(AuthorityEntity_.sectionEntity).get(SectionEntity_.description),"%"+searchValue+"%"));
			predicates.add(cb.like(root.get(AuthorityEntity_.statusEntity).get(StatusEntity_.description),"%"+searchValue+"%"));
			predicates.add(cb.like(root.get(AuthorityEntity_.createdBy),"%"+searchValue+"%"));
			predicates.add(cb.like(root.get(AuthorityEntity_.updatedBy),"%"+searchValue+"%"));
		}		
		else {
			predicates.add(cb.conjunction());
		}
		return predicates;
	}
	
	private List<Order> createSortOrder(String sortColumnName,String sortOrder, CriteriaBuilder cb,Root<AuthorityEntity> root){
		List<Order> orders = new ArrayList<>();		

		Expression<?> ex = root.get(SysRoleEntity_.updatedOn);
		
		
		if("code".equals(sortColumnName)) {
			ex = root.get(AuthorityEntity_.code);
		}
		else if("description".equals(sortColumnName)) {
			ex = root.get(AuthorityEntity_.description);
		}
		else if("url".equals(sortColumnName)) {
			ex = root.get(AuthorityEntity_.url);
		}
		else if("section".equals(sortColumnName)) {
			ex = root.get(AuthorityEntity_.sectionEntity).get(SectionEntity_.description);
		}
		else if("status".equals(sortColumnName)) {
			ex = root.get(AuthorityEntity_.statusEntity).get(StatusEntity_.description);
		}
		else if("createdBy".equals(sortColumnName)) {
			ex = root.get(AuthorityEntity_.createdBy);
		}
		else if("createdOn".equals(sortColumnName)) {
			ex = root.get(AuthorityEntity_.createdOn);
		}
		else if("updatedBy".equals(sortColumnName)) {
			ex = root.get(AuthorityEntity_.updatedBy);
		}		
		
		orders.add(("asc".equals(sortOrder))? cb.asc(ex):cb.desc(ex));		
		
		return orders;
	}

}
