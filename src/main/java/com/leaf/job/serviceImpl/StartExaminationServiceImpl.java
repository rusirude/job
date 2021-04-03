package com.leaf.job.serviceImpl;

import com.leaf.job.dao.*;
import com.leaf.job.dto.*;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.ResponseDTO;
import com.leaf.job.entity.QuestionAnswerEntity;
import com.leaf.job.entity.StatusEntity;
import com.leaf.job.entity.StudentExaminationEntity;
import com.leaf.job.entity.StudentExaminationQuestionAnswerEntity;
import com.leaf.job.enums.DefaultStatusEnum;
import com.leaf.job.enums.ExamStatusEnum;
import com.leaf.job.enums.ResponseCodeEnum;
import com.leaf.job.service.StartExaminationService;
import com.leaf.job.utility.CommonConstant;
import com.leaf.job.utility.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class StartExaminationServiceImpl implements StartExaminationService {

    private StudentExaminationDAO studentExaminationDAO;
    private StatusDAO statusDAO;
    private QuestionDAO questionDAO;
    private QuestionAnswerDAO questionAnswerDAO;
    private StudentExaminationQuestionAnswerDAO studentExaminationQuestionAnswerDAO;
    private CommonMethod commonMethod;
    private ApplicationContext appContext;


    @Autowired
    public StartExaminationServiceImpl(StudentExaminationDAO studentExaminationDAO, StatusDAO statusDAO, QuestionDAO questionDAO, QuestionAnswerDAO questionAnswerDAO, StudentExaminationQuestionAnswerDAO studentExaminationQuestionAnswerDAO, CommonMethod commonMethod, ApplicationContext appContext) {
        this.studentExaminationDAO = studentExaminationDAO;
        this.statusDAO = statusDAO;
        this.questionDAO = questionDAO;
        this.questionAnswerDAO = questionAnswerDAO;
        this.studentExaminationQuestionAnswerDAO = studentExaminationQuestionAnswerDAO;
        this.commonMethod = commonMethod;
        this.appContext = appContext;
    }

    @Override
    @Transactional
    public DataTableResponseDTO getStudentExaminationForDataTable(DataTableRequestDTO dataTableRequestDTO) {
        List<StudentExaminationDTO> list = new ArrayList<>();
        DataTableResponseDTO responseDTO = new DataTableResponseDTO();
        Long numOfRecord = Long.valueOf(0);
        dataTableRequestDTO.setSearch(commonMethod.getUsername());
        try {
            list = studentExaminationDAO.<List<StudentExaminationEntity>>getDataForGridForStudent(dataTableRequestDTO, CommonConstant.GRID_SEARCH_LIST, Arrays.asList(ExamStatusEnum.PENDING.getCode(), ExamStatusEnum.START.getCode(), ExamStatusEnum.CLOSED.getCode()), commonMethod.getSystemDate())
                    .stream().map(entity -> {
                        StudentExaminationDTO dto = new StudentExaminationDTO();
                        dto.setId(entity.getId());
                        dto.setStudent(entity.getSysUserEntity().getUsername());
                        dto.setExaminationCode(entity.getExaminationEntity().getCode());
                        dto.setExaminationDescription(entity.getExaminationEntity().getDescription());
                        dto.setDuration(entity.getExaminationEntity().getDuration());
                        dto.setNoQuestion(entity.getExaminationEntity().getNoQuestion());
                        dto.setStatusCode(entity.getStatusEntity().getCode());
                        dto.setStatusDescription(entity.getStatusEntity().getDescription());
                        return dto;
                    }).collect(Collectors.toList());

            numOfRecord = studentExaminationDAO.<Long>getDataForGridForStudent(dataTableRequestDTO, CommonConstant.GRID_SEARC_COUNT, Arrays.asList(ExamStatusEnum.PENDING.getCode(), ExamStatusEnum.START.getCode(), ExamStatusEnum.CLOSED.getCode()), commonMethod.getSystemDate());

            responseDTO.setData(list);
            responseDTO.setRecordsTotal(numOfRecord);
            responseDTO.setRecordsFiltered(numOfRecord);
            responseDTO.setDraw(dataTableRequestDTO.getDraw());

        } catch (Exception e) {
            System.err.println(e);
        }

        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO<Integer> setupQuestionForExam(Long id) {

        String code = ResponseCodeEnum.FAILED.getCode();
        AtomicInteger i = new AtomicInteger(0);
        int seq = 1;
        try {
            Date systemDate = commonMethod.getSystemDate();
            StudentExaminationEntity studentExaminationEntity = studentExaminationDAO.findStudentExaminationEntity(id);
            StatusEntity statusEntity = statusDAO.findStatusEntityByCode(DefaultStatusEnum.ACTIVE.getCode());
            StatusEntity startExamStatusEntity = statusDAO.findStatusEntityByCode(ExamStatusEnum.START.getCode());
            StatusEntity closedExamStatusEntity = statusDAO.findStatusEntityByCode(ExamStatusEnum.CLOSED.getCode());

            if (ExamStatusEnum.PENDING.getCode().equals(studentExaminationEntity.getStatusEntity().getCode())) {
                questionDAO.findAllQuestionEntitiesRandomly(statusEntity.getId(), studentExaminationEntity.getExaminationEntity().getNoQuestion(), studentExaminationEntity.getExaminationEntity().getQuestionCategoryEntity().getId())
                        .forEach(questionEntity -> {
                            StudentExaminationQuestionAnswerEntity questionAnswerEntity = new StudentExaminationQuestionAnswerEntity();
                            questionAnswerEntity.setStudentExaminationEntity(studentExaminationEntity);
                            questionAnswerEntity.setQuestionEntity(questionEntity);
                            questionAnswerEntity.setSeq(i.incrementAndGet());

                            commonMethod.getPopulateEntityWhenInsert(questionAnswerEntity);
                            studentExaminationQuestionAnswerDAO.saveStudentExaminationQuestionAnswerEntity(questionAnswerEntity);

                        });

                String[] duration = studentExaminationEntity.getExaminationEntity().getDuration().split(":");
                Date endDateTime = commonMethod.addHoursAndMinutesToDate(systemDate, Integer.parseInt(duration[0]), Integer.parseInt(duration[1]));


                studentExaminationEntity.setStatusEntity(startExamStatusEntity);
                studentExaminationEntity.setStartOn(systemDate);
                studentExaminationEntity.setEndOn(endDateTime);

                commonMethod.getPopulateEntityWhenUpdate(studentExaminationEntity);
                studentExaminationDAO.updateStudentExaminationEntity(studentExaminationEntity);
            } else if (ExamStatusEnum.START.getCode().equals(studentExaminationEntity.getStatusEntity().getCode())) {


                String[] duration = studentExaminationEntity.getExaminationEntity().getDuration().split(":");
                Date endDateTime = commonMethod.addHoursAndMinutesToDate(studentExaminationEntity.getStartOn(), Integer.parseInt(duration[0]), Integer.parseInt(duration[1]));
                studentExaminationEntity.setEndOn(endDateTime);
                if (systemDate.compareTo(endDateTime) < 1) {
                    StudentExaminationQuestionAnswerEntity studentAnswerEntity = Optional.ofNullable(studentExaminationQuestionAnswerDAO.findFirstStudentExaminationQuestionAnswerEntityByStudentExaminationAndAnswerIsNull(id))
                            .orElse(new StudentExaminationQuestionAnswerEntity());
                    seq = Optional.ofNullable(studentAnswerEntity.getSeq()).orElse(1);
                } else {
                    studentExaminationEntity.setStatusEntity(closedExamStatusEntity);
                    calculateAnswer(studentExaminationEntity);
                    seq = 0;
                }

                commonMethod.getPopulateEntityWhenUpdate(studentExaminationEntity);
                studentExaminationDAO.updateStudentExaminationEntity(studentExaminationEntity);

            } else {
                calculateAnswer(studentExaminationEntity);
                seq = 0;
            }

        } catch (Exception e) {
            System.err.println(e);
        }

        return new ResponseDTO<>(code, seq);
    }

    @Override
    @Transactional
    public ResponseDTO<ExamQuestionDTO> getQuestionsForExamination(Long studentExam, Integer seq) {
        String code = ResponseCodeEnum.FAILED.getCode();
        QuestionDTO questionDTO = null;
        ExamQuestionDTO examQuestionDTO = null;
        try {
            Date systemDate = commonMethod.getSystemDate();
            StudentExaminationQuestionAnswerEntity studentQuestionAnswerEntity = studentExaminationQuestionAnswerDAO.findStudentExaminationQuestionAnswerEntityByStudentExaminationAndSeq(studentExam, seq);
            StudentExaminationEntity studentExaminationEntity = studentQuestionAnswerEntity.getStudentExaminationEntity();
            StatusEntity closedExamStatusEntity = statusDAO.findStatusEntityByCode(ExamStatusEnum.CLOSED.getCode());

            String[] duration = studentExaminationEntity.getExaminationEntity().getDuration().split(":");
            Date endDateTime = commonMethod.addHoursAndMinutesToDate(studentExaminationEntity.getStartOn(), Integer.parseInt(duration[0]), Integer.parseInt(duration[1]));
            studentExaminationEntity.setEndOn(endDateTime);


            questionDTO = new QuestionDTO();
            questionDTO.setCode(studentQuestionAnswerEntity.getQuestionEntity().getCode());
            questionDTO.setDescription(studentQuestionAnswerEntity.getQuestionEntity().getDescription());

            if (systemDate.compareTo(endDateTime) < 1) {
                List<QuestionAnswerDTO> questionAnswerDTOs = questionAnswerDAO.findAllQuestionAnswerEntitiesByQuestion(studentQuestionAnswerEntity.getQuestionEntity().getId(), DefaultStatusEnum.ACTIVE.getCode())
                        .stream()
                        .sorted(Comparator.comparing(QuestionAnswerEntity::getPosition))
                        .map(questionAnswerEntity -> {
                            QuestionAnswerDTO questionAnswerDTO = new QuestionAnswerDTO();
                            questionAnswerDTO.setId(questionAnswerEntity.getId());
                            questionAnswerDTO.setDescription(questionAnswerEntity.getDescription());
                            boolean isMark = Objects.nonNull(studentQuestionAnswerEntity.getQuestionAnswerEntity()) && (studentQuestionAnswerEntity.getQuestionAnswerEntity().getId().equals(questionAnswerEntity.getId()));
                            questionAnswerDTO.setMark(isMark);
                            return questionAnswerDTO;
                        })
                        .collect(Collectors.toList());

                questionDTO.setQuestionAnswers(questionAnswerDTOs);

                examQuestionDTO = new ExamQuestionDTO();
                examQuestionDTO.setQuestion(questionDTO);
                examQuestionDTO.setDuration(studentQuestionAnswerEntity.getStudentExaminationEntity().getExaminationEntity().getDuration());
                examQuestionDTO.setTotal(studentQuestionAnswerEntity.getStudentExaminationEntity().getExaminationEntity().getNoQuestion());
                examQuestionDTO.setStartTime(studentQuestionAnswerEntity.getStudentExaminationEntity().getStartOn());
                examQuestionDTO.setCurrentTime(systemDate);
                code = ResponseCodeEnum.SUCCESS.getCode();
            } else {
                studentExaminationEntity.setStatusEntity(closedExamStatusEntity);
                calculateAnswer(studentExaminationEntity);
                examQuestionDTO = new ExamQuestionDTO();
                examQuestionDTO.setClosed(true);
                code = ResponseCodeEnum.SUCCESS.getCode();
            }

            commonMethod.getPopulateEntityWhenUpdate(studentExaminationEntity);
            studentExaminationDAO.updateStudentExaminationEntity(studentExaminationEntity);


        } catch (Exception e) {
            System.err.println("Getting Question for examination");
        }
        return new ResponseDTO<>(code, examQuestionDTO);
    }

    @Override
    @Transactional
    public ResponseDTO<?> saveAnswer(AnswerDTO answerDTO) {
        String code = ResponseCodeEnum.FAILED.getCode();
        try {
            StudentExaminationQuestionAnswerEntity studentQuestionAnswerEntity = studentExaminationQuestionAnswerDAO.findStudentExaminationQuestionAnswerEntityByStudentExaminationAndSeq(answerDTO.getStudentExamination(), answerDTO.getSeq());
            QuestionAnswerEntity questionAnswerEntity = questionAnswerDAO.loadQuestionAnswerEntity(answerDTO.getAnswer());
            studentQuestionAnswerEntity.setQuestionAnswerEntity(questionAnswerEntity);

            commonMethod.getPopulateEntityWhenUpdate(studentQuestionAnswerEntity);
            studentExaminationQuestionAnswerDAO.updateStudentExaminationQuestionAnswerEntity(studentQuestionAnswerEntity);

            code = ResponseCodeEnum.SUCCESS.getCode();
        } catch (Exception e) {
            System.err.println("Getting Question for examination");
        }
        return new ResponseDTO<>(code);
    }

    @Override
    @Transactional
    public ResponseDTO<FinalResultDTO> getFinalResult(Long studentExam) {
        String code = ResponseCodeEnum.FAILED.getCode();
        final FinalResultDTO finalResultDTO = new FinalResultDTO();
        try {
            StudentExaminationEntity studentExaminationEntity = studentExaminationDAO.findStudentExaminationEntity(studentExam);
            studentExaminationQuestionAnswerDAO.findStudentExaminationQuestionAnswerEntityByStudentExamination(studentExam)
                    .forEach(studentExaminationQuestionAnswerEntity -> {
                        finalResultDTO.setTotal(finalResultDTO.getTotal() + 1);
                        if (Objects.isNull(studentExaminationQuestionAnswerEntity.getQuestionAnswerEntity()))
                            finalResultDTO.setNotAnswered(finalResultDTO.getNotAnswered() + 1);
                        else if (studentExaminationQuestionAnswerEntity.isCorrect())
                            finalResultDTO.setCorrect(finalResultDTO.getCorrect() + 1);
                        else
                            finalResultDTO.setWrong(finalResultDTO.getWrong() + 1);
                    });
            finalResultDTO.setName(studentExaminationEntity.getSysUserEntity().getTitleEntity().getDescription() + studentExaminationEntity.getSysUserEntity().getName());
            if (finalResultDTO.getTotal() > 0)
                finalResultDTO.setFinalMark(((double) finalResultDTO.getCorrect() / (double) finalResultDTO.getTotal()) * 100);
            else
                finalResultDTO.setFinalMark(0.00);

            code = ResponseCodeEnum.SUCCESS.getCode();
        } catch (Exception e) {
            System.err.println("Getting Question for examination");
        }
        return new ResponseDTO<>(code, finalResultDTO);
    }

    @Override
    @Transactional
    public ReportDTO generateAnswerDetailReportDetails(long studentExam) {


        try {


        } catch (Exception e) {
            System.err.println("Getting Question for examination");
        }
        return null;
    }

    private void calculateAnswer(StudentExaminationEntity studentExaminationEntity) {

        studentExaminationQuestionAnswerDAO.findStudentExaminationQuestionAnswerEntityByStudentExamination(studentExaminationEntity.getId())
                .forEach(studentExaminationQuestionAnswerEntity -> {
                    if (Objects.nonNull(studentExaminationQuestionAnswerEntity.getQuestionAnswerEntity()))
                        studentExaminationQuestionAnswerEntity.setCorrect(studentExaminationQuestionAnswerEntity.getQuestionAnswerEntity().isCorrect());
                    else
                        studentExaminationQuestionAnswerEntity.setCorrect(false);

                    commonMethod.getPopulateEntityWhenUpdate(studentExaminationQuestionAnswerEntity);
                    studentExaminationQuestionAnswerDAO.updateStudentExaminationQuestionAnswerEntity(studentExaminationQuestionAnswerEntity);
                });

    }
}
