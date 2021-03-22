package com.leaf.job.dao;

import com.leaf.job.entity.QuestionAnswerEntity;

import java.util.List;

public interface QuestionAnswerDAO {
	/**
	 * Load Reference Entity
	 * @param id
	 * @return {@link QuestionAnswerEntity}
	 */
    QuestionAnswerEntity loadQuestionAnswerEntity(long id);

	/**
	 * Find Entity from DB By Id
	 * @param id
	 * @return {@link QuestionAnswerEntity}
	 */
    QuestionAnswerEntity findQuestionAnswerEntity(long id);

    
    /**
     * Save QuestionAnswer Entity
     * @param questionEntity
     */
    void saveQuestionAnswerEntity(QuestionAnswerEntity questionEntity);
    
    /**
     * Update QuestionAnswer Entity
     * @param questionEntity
     */
    void updateQuestionAnswerEntity(QuestionAnswerEntity questionEntity);

    
    
    /**
     * select - *
     * From - QuestionAnswer
     * where - STATUS = statusCode
     *
     * Find QuestionAnswer Entities By Status Code
     * @return {@link List}
     */
    List<QuestionAnswerEntity> findAllQuestionAnswerEntitiesByQuestion(long question,String statusCode);


    

}
