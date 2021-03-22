package com.leaf.job.service;

import com.leaf.job.dto.QuestionDTO;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.ResponseDTO;

import java.util.HashMap;

public interface QuestionService {
	/**
	 * Save Question
	 * @param questionDTO
	 * @return {@link ResponseDTO}
	 */
	ResponseDTO<QuestionDTO> saveQuestion(QuestionDTO questionDTO);
	
	/**
	 * Update Question
	 * @param questionDTO
	 * @return {@link ResponseDTO}
	 */
	ResponseDTO<QuestionDTO> updateQuestion(QuestionDTO questionDTO);
	
	/**
	 * Delete Question
	 * @param questionDTO - code
	 * @return {@link ResponseDTO}
	 */
	ResponseDTO<QuestionDTO> deleteQuestion(QuestionDTO questionDTO);
	
	/**
	 * Find Question By Code
	 * @param questionDTO - code
	 * @return {@link ResponseDTO}
	 */
	ResponseDTO<QuestionDTO> findQuestion(QuestionDTO questionDTO);
	
	/**
	 * Load Reference Data For Question Page
	 * @return {@link ResponseDTO}
	 */
	ResponseDTO<HashMap<String, Object>> getReferenceDataForQuestion();
	
	/**
	 * Get Questions Data For Data Table
	 * @param dataTableRequestDTO
	 * @return {@link DataTableResponseDTO}
	 */
	DataTableResponseDTO getQuestionsForDataTable(DataTableRequestDTO dataTableRequestDTO);
}
