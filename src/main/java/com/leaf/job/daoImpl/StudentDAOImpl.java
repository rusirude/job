package com.leaf.job.daoImpl;

import com.leaf.job.dao.StudentDAO;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.entity.StatusEntity_;
import com.leaf.job.entity.StudentEntity;
import com.leaf.job.entity.StudentEntity_;
import com.leaf.job.entity.TitleEntity_;
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



}
