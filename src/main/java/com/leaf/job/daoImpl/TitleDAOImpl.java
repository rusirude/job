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

import com.leaf.job.dao.TitleDAO;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.entity.StatusEntity_;
import com.leaf.job.entity.SysRoleEntity_;
import com.leaf.job.entity.TitleEntity;
import com.leaf.job.entity.TitleEntity_;
import com.leaf.job.enums.DeleteStatusEnum;
import com.leaf.job.utility.CommonConstant;;

@Repository
public class TitleDAOImpl implements TitleDAO {
	
	@Autowired
	private EntityManager entityManager;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TitleEntity loadTitleEntity(long id) {
		return entityManager.getReference(TitleEntity.class, id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TitleEntity findTitleEntity(long id) {
		return entityManager.find(TitleEntity.class,id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TitleEntity findTitleEntityByCode(String code) {
		TitleEntity TitleEntity = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TitleEntity> criteriaQuery = criteriaBuilder.createQuery(TitleEntity.class);
        Root<TitleEntity> root = criteriaQuery.from(TitleEntity.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
                criteriaBuilder.and(
                      criteriaBuilder.equal(root.get(TitleEntity_.code), code),
                      criteriaBuilder.notEqual(root.get(TitleEntity_.statusEntity).get(StatusEntity_.code), DeleteStatusEnum.DELETE.getCode())
                )
        );

        try {
            TitleEntity = entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (Exception e) {
            System.err.println(e);
        }

        return TitleEntity;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveTitleEntity(TitleEntity titleEntity) {
		entityManager.persist(titleEntity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateTitleEntity(TitleEntity titleEntity) {
		entityManager.merge(titleEntity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TitleEntity> findAllTitleEntities() {
		List<TitleEntity> TitleEntities = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TitleEntity> criteriaQuery = criteriaBuilder.createQuery(TitleEntity.class);
        Root<TitleEntity> root = criteriaQuery.from(TitleEntity.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
        		criteriaBuilder.notEqual(root.get(TitleEntity_.statusEntity).get(StatusEntity_.code), DeleteStatusEnum.DELETE.getCode())
        );

        try {
        	TitleEntities = entityManager.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            System.err.println(e);
        }
        
        return TitleEntities;
	}
	
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TitleEntity> findAllTitleEntities(String statusCode){
		List<TitleEntity> sysRoleEntities = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TitleEntity> criteriaQuery = criteriaBuilder.createQuery(TitleEntity.class);
        Root<TitleEntity> root = criteriaQuery.from(TitleEntity.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
        		criteriaBuilder.equal(root.get(TitleEntity_.statusEntity).get(StatusEntity_.code), statusCode)
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
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getDataForGrid(DataTableRequestDTO dataTableRequestDTO, String type) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TitleEntity> criteriaQuery = criteriaBuilder.createQuery(TitleEntity.class);
        Root<TitleEntity> root = criteriaQuery.from(TitleEntity.class);
        List<Predicate> predicates = createSearchPredicate(dataTableRequestDTO.getSearch(),criteriaBuilder,root);
        criteriaQuery.select(root);
        criteriaQuery.where(
        		criteriaBuilder.and(
        				criteriaBuilder.notEqual(root.get(TitleEntity_.statusEntity).get(StatusEntity_.code), DeleteStatusEnum.DELETE.getCode()),
                		criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]))
        		)
        		
        );

        criteriaQuery.orderBy(createSortOrder(dataTableRequestDTO.getSortColumnName(), dataTableRequestDTO.getSortOrder(), criteriaBuilder, root));
        

        if (CommonConstant.GRID_SEARC_COUNT.equals(type)) {
            long count = entityManager.createQuery(criteriaQuery).getResultList().size();
            return (T) Long.valueOf(count);
        } else {
            TypedQuery<TitleEntity> typedQuery = entityManager.createQuery(criteriaQuery);
            typedQuery.setFirstResult(dataTableRequestDTO.getStart());
            typedQuery.setMaxResults(dataTableRequestDTO.getLength());
            return (T) typedQuery.getResultList();
        }	
	}
	
	private List<Predicate> createSearchPredicate(String searchValue,CriteriaBuilder cb,Root<TitleEntity> root) {
		List<Predicate> predicates = new ArrayList<>();
		if(!searchValue.isEmpty()) {
			predicates.add(cb.like(root.get(TitleEntity_.code),"%"+searchValue+"%"));
			predicates.add(cb.like(root.get(TitleEntity_.description),"%"+searchValue+"%"));
			predicates.add(cb.like(root.get(TitleEntity_.statusEntity).get(StatusEntity_.description),"%"+searchValue+"%"));
			predicates.add(cb.like(root.get(TitleEntity_.createdBy),"%"+searchValue+"%"));
			predicates.add(cb.like(root.get(TitleEntity_.updatedBy),"%"+searchValue+"%"));
		}		
		else {
			predicates.add(cb.conjunction());
		}
		return predicates;
	}
	
	private List<Order> createSortOrder(String sortColumnName,String sortOrder, CriteriaBuilder cb,Root<TitleEntity> root){
		List<Order> orders = new ArrayList<>();		

		Expression<?> ex = root.get(SysRoleEntity_.updatedOn);
		
		
		if("code".equals(sortColumnName)) {
			ex = root.get(TitleEntity_.code);
		}
		else if("description".equals(sortColumnName)) {
			ex = root.get(TitleEntity_.description);
		}
		else if("status".equals(sortColumnName)) {
			ex = root.get(TitleEntity_.statusEntity).get(StatusEntity_.description);
		}
		else if("createdBy".equals(sortColumnName)) {
			ex = root.get(TitleEntity_.createdBy);
		}
		else if("createdOn".equals(sortColumnName)) {
			ex = root.get(TitleEntity_.createdOn);
		}
		else if("updatedBy".equals(sortColumnName)) {
			ex = root.get(TitleEntity_.updatedBy);
		}		
		
		orders.add(("asc".equals(sortOrder))? cb.asc(ex):cb.desc(ex));		
		
		return orders;
	}

}
