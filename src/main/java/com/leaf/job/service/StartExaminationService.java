package com.leaf.job.service;

import com.leaf.job.dto.QuestionDTO;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.ResponseDTO;

import java.util.List;

public interface StartExaminationService {

    DataTableResponseDTO getStudentExaminationForDataTable(DataTableRequestDTO dataTableRequestDTO);

    ResponseDTO<List<QuestionDTO>> setupQuestionForExam(Long id);

    ResponseDTO<List<QuestionDTO>> getQuestionsForExamination(Long id);

}
