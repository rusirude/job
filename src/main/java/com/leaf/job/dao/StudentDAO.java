package com.leaf.job.dao;

import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.entity.StudentEntity;

import java.util.List;

public interface StudentDAO {


    /**
     * Find Entity from DB By username
     *
     * @param username
     * @return {@link StudentEntity}
     */
    StudentEntity getStudentEntityByUsername(String username);


    /**
     * Save Student Entity
     *
     * @param studentEntity
     */
    void saveStudentEntity(StudentEntity studentEntity);

    /**
     * Update Student Entity
     *
     * @param studentEntity
     */
    void updateStudentEntity(StudentEntity studentEntity);



}
