package com.leaf.job.daoImpl;

import com.leaf.job.dao.StudentExaminationDAO;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.entity.*;
import com.leaf.job.enums.DeleteStatusEnum;
import com.leaf.job.utility.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

@Repository
public class StudentExaminationDAOImpl implements StudentExaminationDAO {

    @Autowired
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentExaminationEntity loadStudentExaminationEntity(long id) {
        return entityManager.getReference(StudentExaminationEntity.class, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentExaminationEntity findStudentExaminationEntity(long id) {
        return entityManager.find(StudentExaminationEntity.class,id);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveStudentExaminationEntity(StudentExaminationEntity studentExaminationEntity) {
        entityManager.persist(studentExaminationEntity);

    }

    /**
     * {@inheritDoc}
	 */
    @Override
    public void updateStudentExaminationEntity(StudentExaminationEntity studentExaminationEntity) {
        entityManager.merge(studentExaminationEntity);
    }

    @Override
    public <T> T getDataForGridForStudent(DataTableRequestDTO dataTableRequestDTO, String type, List<String> status, Date systemDate) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<StudentExaminationEntity> criteriaQuery = criteriaBuilder.createQuery(StudentExaminationEntity.class);
        Root<StudentExaminationEntity> root = criteriaQuery.from(StudentExaminationEntity.class);
        criteriaQuery.select(root);
        CriteriaBuilder.In<String> codeIn = criteriaBuilder.in(root.get(StudentExaminationEntity_.statusEntity).get(StatusEntity_.code));
        status.forEach(codeIn::value);
        criteriaQuery.where(
                criteriaBuilder.and(
                        codeIn,
                        criteriaBuilder.equal(root.get(StudentExaminationEntity_.sysUserEntity).get(SysUserEntity_.username),dataTableRequestDTO.getSearch()),
                        criteriaBuilder.lessThanOrEqualTo(root.get(StudentExaminationEntity_.examinationEntity).get(ExaminationEntity_.effectiveOn),systemDate),
                        criteriaBuilder.greaterThanOrEqualTo(root.get(StudentExaminationEntity_.examinationEntity).get(ExaminationEntity_.expireOn),systemDate)
                )

        );

        criteriaQuery.orderBy(
                criteriaBuilder.asc(root.get(StudentExaminationEntity_.createdOn))
        );


        if (CommonConstant.GRID_SEARC_COUNT.equals(type)) {
            long count = entityManager.createQuery(criteriaQuery).getResultList().size();
            return (T) Long.valueOf(count);
        } else {
            TypedQuery<StudentExaminationEntity> typedQuery = entityManager.createQuery(criteriaQuery);
            typedQuery.setFirstResult(dataTableRequestDTO.getStart());
            typedQuery.setMaxResults(dataTableRequestDTO.getLength());
            return (T) typedQuery.getResultList();
        }
    }


}
