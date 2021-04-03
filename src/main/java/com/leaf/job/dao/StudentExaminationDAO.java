package com.leaf.job.dao;

import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.entity.StudentExaminationEntity;
import com.leaf.job.entity.StudentExaminationEntity;

import java.util.Date;
import java.util.List;

public interface StudentExaminationDAO {

    /**
     * Load Reference Entity
     * @param id
     * @return {@link StudentExaminationEntity}
     */
    StudentExaminationEntity loadStudentExaminationEntity(long id);

    /**
     * Find Entity from DB By Id
     * @param id
     * @return {@link StudentExaminationEntity}
     */
    StudentExaminationEntity findStudentExaminationEntity(long id);


    /**
     * Save StudentExaminationEntity Entity
     *
     * @param studentExaminationEntity
     */
    void saveStudentExaminationEntity(StudentExaminationEntity studentExaminationEntity);

    /**
     * Update StudentExaminationEntity Entity
     *
     * @param studentExaminationEntity
     */
    void updateStudentExaminationEntity(StudentExaminationEntity studentExaminationEntity);
    /**
     * Getting Data For  Grid
     * @param dataTableRequestDTO
     * @return
     */
    <T> T getDataForGridForStudent(DataTableRequestDTO dataTableRequestDTO, String type, List<String> status, Date systemDate);


}
