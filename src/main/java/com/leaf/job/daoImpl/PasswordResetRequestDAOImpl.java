package com.leaf.job.daoImpl;

import com.leaf.job.dao.PasswordResetRequestDAO;
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
public class PasswordResetRequestDAOImpl implements PasswordResetRequestDAO {

    @Autowired
    private EntityManager entityManager;


    /**
     * {@inheritDoc}
     */
    @Override
    public void savePasswordResetRequestEntity(PasswordResetRequestEntity passwordResetRequestEntity) {
        entityManager.persist(passwordResetRequestEntity);

    }

    @Override
    public void updatePasswordResetRequestEntity(PasswordResetRequestEntity passwordResetRequestEntity) {
        entityManager.merge(passwordResetRequestEntity);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<PasswordResetRequestEntity> findAllPasswordResetRequestsBySysUserAndStatus(String username, String statusCode) {
        List<PasswordResetRequestEntity> passwordResetRequestEntities = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PasswordResetRequestEntity> criteriaQuery = criteriaBuilder.createQuery(PasswordResetRequestEntity.class);
        Root<PasswordResetRequestEntity> root = criteriaQuery.from(PasswordResetRequestEntity.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get(PasswordResetRequestEntity_.statusEntity).get(StatusEntity_.code), statusCode),
                        criteriaBuilder.equal(root.get(PasswordResetRequestEntity_.sysUserEntity).get(SysUserEntity_.username), username)

                )
        );

        try {
            passwordResetRequestEntities = entityManager.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            System.err.println(e);
        }

        return passwordResetRequestEntities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PasswordResetRequestEntity findPasswordResetRequest(long id) {
        return entityManager.find(PasswordResetRequestEntity.class, id);
    }


    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getDataForGrid(DataTableRequestDTO dataTableRequestDTO, String type) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PasswordResetRequestEntity> criteriaQuery = criteriaBuilder.createQuery(PasswordResetRequestEntity.class);
        Root<PasswordResetRequestEntity> root = criteriaQuery.from(PasswordResetRequestEntity.class);
        List<Predicate> predicates = createSearchPredicate(dataTableRequestDTO.getSearch(), criteriaBuilder, root);
        criteriaQuery.select(root);
        criteriaQuery.where(
                criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]))


        );

        criteriaQuery.orderBy(createSortOrder(dataTableRequestDTO.getSortColumnName(), dataTableRequestDTO.getSortOrder(), criteriaBuilder, root));


        if (CommonConstant.GRID_SEARC_COUNT.equals(type)) {
            long count = entityManager.createQuery(criteriaQuery).getResultList().size();
            return (T) Long.valueOf(count);
        } else {
            TypedQuery<PasswordResetRequestEntity> typedQuery = entityManager.createQuery(criteriaQuery);
            typedQuery.setFirstResult(dataTableRequestDTO.getStart());
            typedQuery.setMaxResults(dataTableRequestDTO.getLength());
            return (T) typedQuery.getResultList();
        }
    }


    private List<Predicate> createSearchPredicate(String searchValue, CriteriaBuilder cb, Root<PasswordResetRequestEntity> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (!searchValue.isEmpty()) {
            predicates.add(cb.like(root.get(PasswordResetRequestEntity_.sysUserEntity).get(SysUserEntity_.username), "%" + searchValue + "%"));
            predicates.add(cb.like(root.get(PasswordResetRequestEntity_.sysUserEntity).get(SysUserEntity_.titleEntity).get(TitleEntity_.description), "%" + searchValue + "%"));
            predicates.add(cb.like(root.get(PasswordResetRequestEntity_.sysUserEntity).get(SysUserEntity_.name), "%" + searchValue + "%"));
            predicates.add(cb.like(root.get(PasswordResetRequestEntity_.statusEntity).get(StatusEntity_.description), "%" + searchValue + "%"));
        } else {
            predicates.add(cb.conjunction());
        }
        return predicates;
    }


    private List<Order> createSortOrder(String sortColumnName, String sortOrder, CriteriaBuilder cb, Root<PasswordResetRequestEntity> root) {
        List<Order> orders = new ArrayList<>();

        Expression<?> ex = root.get(SysUserEntity_.updatedOn);


        if ("username".equals(sortColumnName)) {
            ex = root.get(PasswordResetRequestEntity_.sysUserEntity).get(SysUserEntity_.username);
        } else if ("title".equals(sortColumnName)) {
            ex = root.get(PasswordResetRequestEntity_.sysUserEntity).get(SysUserEntity_.titleEntity).get(TitleEntity_.description);
        } else if ("name".equals(sortColumnName)) {
            ex = root.get(PasswordResetRequestEntity_.sysUserEntity).get(SysUserEntity_.name);
        } else if ("status".equals(sortColumnName)) {
            ex = root.get(PasswordResetRequestEntity_.statusEntity).get(StatusEntity_.description);
        }

        orders.add(("asc".equals(sortOrder)) ? cb.asc(ex) : cb.desc(ex));

        return orders;
    }
}
