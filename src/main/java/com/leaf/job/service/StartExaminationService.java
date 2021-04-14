package com.leaf.job.service;

import com.leaf.job.dto.*;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.ResponseDTO;

import java.util.HashMap;

public interface StartExaminationService {

    DataTableResponseDTO getStudentExaminationForDataTable(DataTableRequestDTO dataTableRequestDTO);

    ResponseDTO<Integer> setupQuestionForExam(Long id);

    ResponseDTO<ExamQuestionDTO> getQuestionsForExamination(Long studentExam, Integer seq);

    ResponseDTO<?> saveAnswer(AnswerDTO answerDTO);

    void saveFinalAnswerCalculation(Long studentExam);

    ResponseDTO<FinalResultDTO> getFinalResult(Long studentExam);

    ReportDTO generateAnswerDetailReportDetails(long studentExam);

    ResponseDTO<?> generateAnswerDetailReportDetailsAndSendMail(long studentExam);
;
    DataTableResponseDTO getStudentExaminationForDataTableForAdd(DataTableRequestDTO dataTableRequestDTO);

    ResponseDTO<HashMap<String, Object>> getReferenceDataForStudentExaminationAdd();

    ResponseDTO<?> saveStudentExamination(StudentExaminationDTO studentExaminationDTO);

    ResponseDTO<?> deleteStudentExamination(StudentExaminationDTO studentExaminationDTO);

    ResponseDTO<?> tryToCloseStudentExamination(StudentExaminationDTO studentExaminationDTO);
}
