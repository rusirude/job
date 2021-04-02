package com.leaf.job.daoImpl;

import com.leaf.job.dao.StudentExaminationQuestionAnswerDAO;
import com.leaf.job.entity.StudentExaminationQuestionAnswerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

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
