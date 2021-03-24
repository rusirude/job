package com.leaf.job.daoImpl;

import com.leaf.job.dao.StudentExaminationDAO;
import com.leaf.job.entity.StudentExaminationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class StudentExaminationDAOImpl implements StudentExaminationDAO {

    @Autowired
    private EntityManager entityManager;


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveStudentExaminationEntity(StudentExaminationEntity studentEntity) {
        entityManager.persist(studentEntity);

    }


}
