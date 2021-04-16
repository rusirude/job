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

import com.leaf.job.dao.SysRoleDAO;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.entity.StatusEntity_;
import com.leaf.job.entity.SysRoleEntity;
import com.leaf.job.entity.SysRoleEntity_;
import com.leaf.job.enums.DeleteStatusEnum;
import com.leaf.job.utility.CommonConstant;

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
	public List<SysRoleEntity> findAllSysRoleEntities(String statusCode){
		List<SysRoleEntity> sysRoleEntities = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SysRoleEntity> criteriaQuery = criteriaBuilder.createQuery(SysRoleEntity.class);
        Root<SysRoleEntity> root = criteriaQuery.from(SysRoleEntity.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
        		criteriaBuilder.equal(root.get(SysRoleEntity_.statusEntity).get(StatusEntity_.code), statusCode)
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
	public <T> T getDataForGrid(DataTableRequestDTO dataTableRequestDTO,String type) {	
		
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SysRoleEntity> criteriaQuery = criteriaBuilder.createQuery(SysRoleEntity.class);
        Root<SysRoleEntity> root = criteriaQuery.from(SysRoleEntity.class);
        List<Predicate> predicates = createSearchPredicate(dataTableRequestDTO.getSearch(),criteriaBuilder,root);
        criteriaQuery.select(root);
        criteriaQuery.where(
        		criteriaBuilder.and(
						criteriaBuilder.notEqual(root.get(SysRoleEntity_.code),CommonConstant.SYSTEM),
        				criteriaBuilder.notEqual(root.get(SysRoleEntity_.statusEntity).get(StatusEntity_.code), DeleteStatusEnum.DELETE.getCode()),
                		criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]))
        		)
        		
        );

        criteriaQuery.orderBy(createSortOrder(dataTableRequestDTO.getSortColumnName(), dataTableRequestDTO.getSortOrder(), criteriaBuilder, root));
        

        if (CommonConstant.GRID_SEARC_COUNT.equals(type)) {
            long count = entityManager.createQuery(criteriaQuery).getResultList().size();
            return (T) Long.valueOf(count);
        } else {
            TypedQuery<SysRoleEntity> typedQuery = entityManager.createQuery(criteriaQuery);
            typedQuery.setFirstResult(dataTableRequestDTO.getStart());
            typedQuery.setMaxResults(dataTableRequestDTO.getLength());
            return (T) typedQuery.getResultList();
        }	
	}
	
	private List<Predicate> createSearchPredicate(String searchValue,CriteriaBuilder cb,Root<SysRoleEntity> root) {
		List<Predicate> predicates = new ArrayList<>();
		if(!searchValue.isEmpty()) {
			predicates.add(cb.like(root.get(SysRoleEntity_.code),"%"+searchValue+"%"));
			predicates.add(cb.like(root.get(SysRoleEntity_.description),"%"+searchValue+"%"));
			predicates.add(cb.like(root.get(SysRoleEntity_.statusEntity).get(StatusEntity_.description),"%"+searchValue+"%"));
			predicates.add(cb.like(root.get(SysRoleEntity_.createdBy),"%"+searchValue+"%"));
			predicates.add(cb.like(root.get(SysRoleEntity_.updatedBy),"%"+searchValue+"%"));
		}		
		else {
			predicates.add(cb.conjunction());
		}
		return predicates;
	}
	
	private List<Order> createSortOrder(String sortColumnName,String sortOrder, CriteriaBuilder cb,Root<SysRoleEntity> root){
		List<Order> orders = new ArrayList<>();		

		Expression<?> ex = root.get(SysRoleEntity_.updatedOn);
		
		
		if("code".equals(sortColumnName)) {
			ex = root.get(SysRoleEntity_.code);
		}
		else if("description".equals(sortColumnName)) {
			ex = root.get(SysRoleEntity_.description);
		}
		else if("status".equals(sortColumnName)) {
			ex = root.get(SysRoleEntity_.statusEntity).get(StatusEntity_.description);
		}
		else if("createdBy".equals(sortColumnName)) {
			ex = root.get(SysRoleEntity_.createdBy);
		}
		else if("createdOn".equals(sortColumnName)) {
			ex = root.get(SysRoleEntity_.createdOn);
		}
		else if("updatedBy".equals(sortColumnName)) {
			ex = root.get(SysRoleEntity_.updatedBy);
		}		
		
		orders.add(("asc".equals(sortOrder))? cb.asc(ex):cb.desc(ex));		
		
		return orders;
	}


}
