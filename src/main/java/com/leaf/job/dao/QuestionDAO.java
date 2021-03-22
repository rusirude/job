package com.leaf.job.dao;

import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.entity.QuestionEntity;

import java.util.List;

public interface QuestionDAO {
	/**
	 * Load Reference Entity
	 * @param id
	 * @return {@link QuestionEntity}
	 */
    QuestionEntity loadQuestionEntity(long id);

	/**
	 * Find Entity from DB By Id
	 * @param id
	 * @return {@link QuestionEntity}
	 */
    QuestionEntity findQuestionEntity(long id);

	/**
	 * Find Entity from DB By Code
	 * @param code
	 * @return {@link QuestionEntity}
	 */
    QuestionEntity findQuestionEntityByCode(String code);
    
    /**
     * Save Question Entity
     * @param questionEntity
     */
    void saveQuestionEntity(QuestionEntity questionEntity);
    
    /**
     * Update Question Entity
     * @param questionEntity
     */
    void updateQuestionEntity(QuestionEntity questionEntity);
    
    /**
     * select - *
     * From - Question
     * where - STATUS <> DELETE
     * 
     * Find Question Entities without delete
     * @return {@link List}
     */
    List<QuestionEntity> findAllQuestionEntities();
    
    
    /**
     * select - *
     * From - Question
     * where - STATUS = statusCode
     * 
     * Find Question Entities By Status Code
     * @return {@link List}
     */
    List<QuestionEntity> findAllQuestionEntities(String statusCode);
    
    /**
     * Getting Data For  Grid 
     * @param dataTableRequestDTO
     * @return 
     */
    <T> T getDataForGrid(DataTableRequestDTO dataTableRequestDTO, String type);
}
