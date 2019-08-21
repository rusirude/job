package com.leaf.job.daoImpl;

import com.leaf.job.dao.CountryDAO;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.entity.StatusEntity_;
import com.leaf.job.entity.SysRoleEntity_;
import com.leaf.job.entity.CountryEntity;
import com.leaf.job.entity.CountryEntity_;
import com.leaf.job.enums.DeleteStatusEnum;
import com.leaf.job.utility.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

;

@Repository
public class CountryDAOImpl implements CountryDAO {
	
	@Autowired
	private EntityManager entityManager;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CountryEntity loadCountryEntity(long id) {
		return entityManager.getReference(CountryEntity.class, id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CountryEntity findCountryEntity(long id) {
		return entityManager.find(CountryEntity.class,id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CountryEntity findCountryEntityByCode(String code) {
		CountryEntity CountryEntity = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CountryEntity> criteriaQuery = criteriaBuilder.createQuery(CountryEntity.class);
        Root<CountryEntity> root = criteriaQuery.from(CountryEntity.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
                criteriaBuilder.and(
                      criteriaBuilder.equal(root.get(CountryEntity_.code), code),
                      criteriaBuilder.notEqual(root.get(CountryEntity_.statusEntity).get(StatusEntity_.code), DeleteStatusEnum.DELETE.getCode())
                )
        );

        try {
            CountryEntity = entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (Exception e) {
            System.err.println(e);
        }

        return CountryEntity;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveCountryEntity(CountryEntity countryEntity) {
		entityManager.persist(countryEntity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateCountryEntity(CountryEntity countryEntity) {
		entityManager.merge(countryEntity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CountryEntity> findAllCountryEntities() {
		List<CountryEntity> countryEntities = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CountryEntity> criteriaQuery = criteriaBuilder.createQuery(CountryEntity.class);
        Root<CountryEntity> root = criteriaQuery.from(CountryEntity.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
        		criteriaBuilder.notEqual(root.get(CountryEntity_.statusEntity).get(StatusEntity_.code), DeleteStatusEnum.DELETE.getCode())
        );

        try {
        	countryEntities = entityManager.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            System.err.println(e);
        }
        
        return countryEntities;
	}
	
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CountryEntity> findAllCountryEntities(String statusCode){
		List<CountryEntity> countryEntities = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CountryEntity> criteriaQuery = criteriaBuilder.createQuery(CountryEntity.class);
        Root<CountryEntity> root = criteriaQuery.from(CountryEntity.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
        		criteriaBuilder.equal(root.get(CountryEntity_.statusEntity).get(StatusEntity_.code), statusCode)
        );

        try {
			countryEntities = entityManager.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            System.err.println(e);
        }
        
        return countryEntities;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getDataForGrid(DataTableRequestDTO dataTableRequestDTO, String type) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CountryEntity> criteriaQuery = criteriaBuilder.createQuery(CountryEntity.class);
        Root<CountryEntity> root = criteriaQuery.from(CountryEntity.class);
        List<Predicate> predicates = createSearchPredicate(dataTableRequestDTO.getSearch(),criteriaBuilder,root);
        criteriaQuery.select(root);
        criteriaQuery.where(
        		criteriaBuilder.and(
        				criteriaBuilder.notEqual(root.get(CountryEntity_.statusEntity).get(StatusEntity_.code), DeleteStatusEnum.DELETE.getCode()),
                		criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]))
        		)
        		
        );

        criteriaQuery.orderBy(createSortOrder(dataTableRequestDTO.getSortColumnName(), dataTableRequestDTO.getSortOrder(), criteriaBuilder, root));
        

        if (CommonConstant.GRID_SEARC_COUNT.equals(type)) {
            long count = entityManager.createQuery(criteriaQuery).getResultList().size();
            return (T) Long.valueOf(count);
        } else {
            TypedQuery<CountryEntity> typedQuery = entityManager.createQuery(criteriaQuery);
            typedQuery.setFirstResult(dataTableRequestDTO.getStart());
            typedQuery.setMaxResults(dataTableRequestDTO.getLength());
            return (T) typedQuery.getResultList();
        }	
	}
	
	private List<Predicate> createSearchPredicate(String searchValue,CriteriaBuilder cb,Root<CountryEntity> root) {
		List<Predicate> predicates = new ArrayList<>();
		if(!searchValue.isEmpty()) {
			predicates.add(cb.like(root.get(CountryEntity_.code),"%"+searchValue+"%"));
			predicates.add(cb.like(root.get(CountryEntity_.description),"%"+searchValue+"%"));
			predicates.add(cb.like(root.get(CountryEntity_.statusEntity).get(StatusEntity_.description),"%"+searchValue+"%"));
			predicates.add(cb.like(root.get(CountryEntity_.createdBy),"%"+searchValue+"%"));
			predicates.add(cb.like(root.get(CountryEntity_.updatedBy),"%"+searchValue+"%"));
		}		
		else {
			predicates.add(cb.conjunction());
		}
		return predicates;
	}
	
	private List<Order> createSortOrder(String sortColumnName,String sortOrder, CriteriaBuilder cb,Root<CountryEntity> root){
		List<Order> orders = new ArrayList<>();		

		Expression<?> ex = root.get(SysRoleEntity_.updatedOn);
		
		
		if("code".equals(sortColumnName)) {
			ex = root.get(CountryEntity_.code);
		}
		else if("description".equals(sortColumnName)) {
			ex = root.get(CountryEntity_.description);
		}
		else if("status".equals(sortColumnName)) {
			ex = root.get(CountryEntity_.statusEntity).get(StatusEntity_.description);
		}
		else if("createdBy".equals(sortColumnName)) {
			ex = root.get(CountryEntity_.createdBy);
		}
		else if("createdOn".equals(sortColumnName)) {
			ex = root.get(CountryEntity_.createdOn);
		}
		else if("updatedBy".equals(sortColumnName)) {
			ex = root.get(CountryEntity_.updatedBy);
		}		
		
		orders.add(("asc".equals(sortOrder))? cb.asc(ex):cb.desc(ex));		
		
		return orders;
	}

}
