package com.leaf.job.daoImpl;

import com.leaf.job.dao.StudentDAO;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.entity.*;
import com.leaf.job.enums.DeleteStatusEnum;
import com.leaf.job.utility.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentDAOImpl implements StudentDAO {

    @Autowired
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentEntity getStudentEntityByUsername(String username) {
        return entityManager.find(StudentEntity.class, username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveStudentEntity(StudentEntity studentEntity) {
        entityManager.persist(studentEntity);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateStudentEntity(StudentEntity studentEntity) {
        entityManager.merge(studentEntity);

    }

    @Override
    public <T> T getDataForGrid(DataTableRequestDTO dataTableRequestDTO, String type) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SysUserEntity> criteriaQuery = criteriaBuilder.createQuery(SysUserEntity.class);
        Root<SysUserEntity> root = criteriaQuery.from(SysUserEntity.class);
        List<Predicate> predicates = createSearchPredicate(dataTableRequestDTO.getSearch(), criteriaBuilder, root);
        criteriaQuery.select(root);
        criteriaQuery.where(
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get(SysUserEntity_.student),true),
                        criteriaBuilder.notEqual(root.get(SysUserEntity_.statusEntity).get(StatusEntity_.code), DeleteStatusEnum.DELETE.getCode()),
                        criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]))
                )

        );

        criteriaQuery.orderBy(createSortOrder(dataTableRequestDTO.getSortColumnName(), dataTableRequestDTO.getSortOrder(), criteriaBuilder, root));


        if (CommonConstant.GRID_SEARC_COUNT.equals(type)) {
            long count = entityManager.createQuery(criteriaQuery).getResultList().size();
            return (T) Long.valueOf(count);
        } else {
            TypedQuery<SysUserEntity> typedQuery = entityManager.createQuery(criteriaQuery);
            typedQuery.setFirstResult(dataTableRequestDTO.getStart());
            typedQuery.setMaxResults(dataTableRequestDTO.getLength());
            return (T) typedQuery.getResultList();
        }
    }


    private List<Predicate> createSearchPredicate(String searchValue, CriteriaBuilder cb, Root<SysUserEntity> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (!searchValue.isEmpty()) {
            predicates.add(cb.like(root.get(SysUserEntity_.username), "%" + searchValue + "%"));
            predicates.add(cb.like(root.get(SysUserEntity_.titleEntity).get(TitleEntity_.description), "%" + searchValue + "%"));
            predicates.add(cb.like(root.get(SysUserEntity_.name), "%" + searchValue + "%"));
            predicates.add(cb.like(root.get(SysUserEntity_.statusEntity).get(StatusEntity_.description), "%" + searchValue + "%"));
            predicates.add(cb.like(root.get(SysUserEntity_.createdBy), "%" + searchValue + "%"));
            predicates.add(cb.like(root.get(SysUserEntity_.updatedBy), "%" + searchValue + "%"));
        } else {
            predicates.add(cb.conjunction());
        }
        return predicates;
    }


    private List<Order> createSortOrder(String sortColumnName, String sortOrder, CriteriaBuilder cb, Root<SysUserEntity> root) {
        List<Order> orders = new ArrayList<>();

        Expression<?> ex = root.get(SysUserEntity_.updatedOn);


        if ("username".equals(sortColumnName)) {
            ex = root.get(SysUserEntity_.username);
        } else if ("title".equals(sortColumnName)) {
            ex = root.get(SysUserEntity_.titleEntity).get(TitleEntity_.description);
        } else if ("name".equals(sortColumnName)) {
            ex = root.get(SysUserEntity_.name);
        } else if ("status".equals(sortColumnName)) {
            ex = root.get(SysUserEntity_.statusEntity).get(StatusEntity_.description);
        } else if ("createdBy".equals(sortColumnName)) {
            ex = root.get(SysUserEntity_.createdBy);
        } else if ("createdOn".equals(sortColumnName)) {
            ex = root.get(SysUserEntity_.createdOn);
        } else if ("updatedBy".equals(sortColumnName)) {
            ex = root.get(SysUserEntity_.updatedBy);
        }

        orders.add(("asc".equals(sortOrder)) ? cb.asc(ex) : cb.desc(ex));

        return orders;
    }



}
