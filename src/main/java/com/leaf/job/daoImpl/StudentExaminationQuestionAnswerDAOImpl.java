package com.leaf.job.daoImpl;

import com.leaf.job.dao.StudentExaminationQuestionAnswerDAO;
import com.leaf.job.entity.*;
import com.leaf.job.enums.DefaultStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class StudentExaminationQuestionAnswerDAOImpl implements StudentExaminationQuestionAnswerDAO {

    @Autowired
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentExaminationQuestionAnswerEntity loadStudentExaminationQuestionAnswerEntity(long id) {
        return entityManager.getReference(StudentExaminationQuestionAnswerEntity.class, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentExaminationQuestionAnswerEntity findStudentExaminationQuestionAnswerEntity(long id) {
        return entityManager.find(StudentExaminationQuestionAnswerEntity.class, id);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public StudentExaminationQuestionAnswerEntity findStudentExaminationQuestionAnswerEntityByStudentExaminationAndSeq(long studentExam,int seq){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<StudentExaminationQuestionAnswerEntity> criteriaQuery = criteriaBuilder.createQuery(StudentExaminationQuestionAnswerEntity.class);
        Root<StudentExaminationQuestionAnswerEntity> root = criteriaQuery.from(StudentExaminationQuestionAnswerEntity.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get(StudentExaminationQuestionAnswerEntity_.studentExaminationEntity).get(StudentExaminationEntity_.id), studentExam),
                        criteriaBuilder.equal(root.get(StudentExaminationQuestionAnswerEntity_.seq),seq)
                ));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentExaminationQuestionAnswerEntity findFirstStudentExaminationQuestionAnswerEntityByStudentExaminationAndAnswerIsNull(long studentExam){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<StudentExaminationQuestionAnswerEntity> criteriaQuery = criteriaBuilder.createQuery(StudentExaminationQuestionAnswerEntity.class);
        Root<StudentExaminationQuestionAnswerEntity> root = criteriaQuery.from(StudentExaminationQuestionAnswerEntity.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get(StudentExaminationQuestionAnswerEntity_.studentExaminationEntity).get(StudentExaminationEntity_.id), studentExam),
                        criteriaBuilder.isNull(root.get(StudentExaminationQuestionAnswerEntity_.questionAnswerEntity))
                ));
        return entityManager.createQuery(criteriaQuery).setMaxResults(1).getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<StudentExaminationQuestionAnswerEntity> findStudentExaminationQuestionAnswerEntityByStudentExamination(long studentExam) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<StudentExaminationQuestionAnswerEntity> criteriaQuery = criteriaBuilder.createQuery(StudentExaminationQuestionAnswerEntity.class);
        Root<StudentExaminationQuestionAnswerEntity> root = criteriaQuery.from(StudentExaminationQuestionAnswerEntity.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
                criteriaBuilder.equal(root.get(StudentExaminationQuestionAnswerEntity_.studentExaminationEntity).get(StudentExaminationEntity_.id), studentExam)
        );

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveStudentExaminationQuestionAnswerEntity(StudentExaminationQuestionAnswerEntity studentExaminationQuestionAnswerEntity) {
        entityManager.persist(studentExaminationQuestionAnswerEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateStudentExaminationQuestionAnswerEntity(StudentExaminationQuestionAnswerEntity studentExaminationQuestionAnswerEntity) {
        entityManager.merge(studentExaminationQuestionAnswerEntity);
    }
}
