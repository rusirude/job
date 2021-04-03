package com.leaf.job.dao;

import com.leaf.job.entity.StudentExaminationQuestionAnswerEntity;

public interface StudentExaminationQuestionAnswerDAO {
    /**
     * Load Reference Entity
     * @param id
     * @return {@link StudentExaminationQuestionAnswerEntity}
     */
    StudentExaminationQuestionAnswerEntity loadStudentExaminationQuestionAnswerEntity(long id);

    /**
     * Find Entity from DB By Id
     * @param id
     * @return {@link StudentExaminationQuestionAnswerEntity}
     */
    StudentExaminationQuestionAnswerEntity findStudentExaminationQuestionAnswerEntity(long id);

    /**
     * Find Entity from DB By Id
     * @param studentExam
     * @param seq
     * @return {@link StudentExaminationQuestionAnswerEntity}
     */
    StudentExaminationQuestionAnswerEntity findStudentExaminationQuestionAnswerEntityByStudentExaminationAndSeq(long studentExam,int seq);


    /**
     * Save StudentExaminationQuestionAnswer Entity
     * @param studentExaminationQuestionAnswerEntity
     */
    void saveStudentExaminationQuestionAnswerEntity(StudentExaminationQuestionAnswerEntity studentExaminationQuestionAnswerEntity);

    /**
     * Update StudentExaminationQuestionAnswer Entity
     * @param studentExaminationQuestionAnswerEntity
     */
    void updateStudentExaminationQuestionAnswerEntity(StudentExaminationQuestionAnswerEntity studentExaminationQuestionAnswerEntity);
}
