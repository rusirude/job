package com.leaf.job.dao;

import com.leaf.job.entity.QuestionQuestionCategoryEntity;
import com.leaf.job.entity.SysRoleEntity;

import java.util.List;

public interface QuestionQuestionCategoryDAO {
	


	
	/**
	 * where SysRole = sysRoleId
	 * 
	 * Delete Entities by SysRole
	 * @param questionId
	 */
	void deleteQuestionQuestionCategoryEntityByQuestion(long questionId);
	

	
	/**
	 * Save QuestionQuestionCategory Entity
	 * @param questionQuestionCategoryEntity
	 */
	void saveQuestionQuestionCategoryEntity(QuestionQuestionCategoryEntity questionQuestionCategoryEntity);

	/**
	 * Select - *
	 * From - QuestionQuestionCategoryEntity
	 * Where - Question Category STATUS = ACTIVE and Question =  SYS_ROLE_ID
	 *
	 * Select all QuestionQuestionCategoryEntity for Question Entity and Question Category  Status is ACTIVE
	 * @param questionId
	 * @return {@link List<QuestionQuestionCategoryEntity>}
	 */
	List<QuestionQuestionCategoryEntity> getQuestionQuestionCategoryEntity(long questionId);
	
	
}