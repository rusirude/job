package com.leaf.job.service;

import com.leaf.job.dto.AnswerDTO;
import com.leaf.job.dto.ExamQuestionDTO;
import com.leaf.job.dto.FinalResultDTO;
import com.leaf.job.dto.ReportDTO;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.ResponseDTO;

public interface StartExaminationService {

    DataTableResponseDTO getStudentExaminationForDataTable(DataTableRequestDTO dataTableRequestDTO);

    ResponseDTO<Integer> setupQuestionForExam(Long id);

    ResponseDTO<ExamQuestionDTO> getQuestionsForExamination(Long studentExam, Integer seq);

    ResponseDTO<?> saveAnswer(AnswerDTO answerDTO);

    ResponseDTO<FinalResultDTO> getFinalResult(Long studentExam);

    ReportDTO generateAnswerDetailReportDetails(long studentExam);


}
