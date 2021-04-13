package com.leaf.job.serviceImpl;

import com.leaf.job.dao.*;
import com.leaf.job.dto.*;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.DropDownDTO;
import com.leaf.job.dto.common.ResponseDTO;
import com.leaf.job.entity.*;
import com.leaf.job.enums.*;
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
    private StudentDAO studentDAO;
    private SysUserDAO sysUserDAO;
    private ExaminationDAO examinationDAO;
    private CommonMethod commonMethod;
    private ApplicationContext appContext;


    @Autowired
    public StartExaminationServiceImpl(StudentExaminationDAO studentExaminationDAO, StatusDAO statusDAO, QuestionDAO questionDAO, QuestionAnswerDAO questionAnswerDAO, StudentExaminationQuestionAnswerDAO studentExaminationQuestionAnswerDAO, StudentDAO studentDAO, SysUserDAO sysUserDAO, ExaminationDAO examinationDAO, CommonMethod commonMethod, ApplicationContext appContext) {
        this.studentExaminationDAO = studentExaminationDAO;
        this.statusDAO = statusDAO;
        this.questionDAO = questionDAO;
        this.questionAnswerDAO = questionAnswerDAO;
        this.studentExaminationQuestionAnswerDAO = studentExaminationQuestionAnswerDAO;
        this.studentDAO = studentDAO;
        this.sysUserDAO = sysUserDAO;
        this.examinationDAO = examinationDAO;
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
            list = studentExaminationDAO.<List<StudentExaminationEntity>>getDataForGridForStudentBetweenSystemDate(dataTableRequestDTO, CommonConstant.GRID_SEARCH_LIST, Arrays.asList(ExamStatusEnum.PENDING.getCode(), ExamStatusEnum.START.getCode(), ExamStatusEnum.CLOSED.getCode()), commonMethod.getSystemDate())
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

            numOfRecord = studentExaminationDAO.<Long>getDataForGridForStudentBetweenSystemDate(dataTableRequestDTO, CommonConstant.GRID_SEARC_COUNT, Arrays.asList(ExamStatusEnum.PENDING.getCode(), ExamStatusEnum.START.getCode(), ExamStatusEnum.CLOSED.getCode()), commonMethod.getSystemDate());

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
                Date expectedTime = commonMethod.addHoursAndMinutesToDate(systemDate, Integer.parseInt(duration[0]), Integer.parseInt(duration[1]));
                Date endDateTime = getEndDateTimeForExam(expectedTime,studentExaminationEntity.getExaminationEntity().getExpireOn());

                studentExaminationEntity.setStatusEntity(startExamStatusEntity);
                studentExaminationEntity.setStartOn(systemDate);
                studentExaminationEntity.setEndOn(endDateTime);

                commonMethod.getPopulateEntityWhenUpdate(studentExaminationEntity);
                studentExaminationDAO.updateStudentExaminationEntity(studentExaminationEntity);
            } else if (ExamStatusEnum.START.getCode().equals(studentExaminationEntity.getStatusEntity().getCode())) {


                String[] duration = studentExaminationEntity.getExaminationEntity().getDuration().split(":");
                Date expectedTime = commonMethod.addHoursAndMinutesToDate(studentExaminationEntity.getStartOn(), Integer.parseInt(duration[0]), Integer.parseInt(duration[1]));
                Date endDateTime = getEndDateTimeForExam(expectedTime,studentExaminationEntity.getExaminationEntity().getExpireOn());

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
            Integer notAnswer = Optional.ofNullable(studentExaminationQuestionAnswerDAO.findStudentExaminationQuestionAnswerEntityByStudentExaminationAndAnswerIsNull(studentExam))
                    .orElse(Collections.emptyList()).size();
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
                examQuestionDTO.setDone(studentQuestionAnswerEntity.getStudentExaminationEntity().getExaminationEntity().getNoQuestion()-notAnswer);
                examQuestionDTO.setStartTime(studentQuestionAnswerEntity.getStudentExaminationEntity().getStartOn());
                examQuestionDTO.setEndTime(studentQuestionAnswerEntity.getStudentExaminationEntity().getEndOn());
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
    public void saveFinalAnswerCalculation(Long studentExam) {
        try {
           finaStateChangeStudentExamination(studentExam);
        } catch (Exception e) {
            System.err.println("Getting Question for examination");
        }
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


    @Override
    @Transactional
    public DataTableResponseDTO getStudentExaminationForDataTableForAdd(DataTableRequestDTO dataTableRequestDTO) {
        List<StudentExaminationDTO> list = new ArrayList<>();
        DataTableResponseDTO responseDTO = new DataTableResponseDTO();
        Long numOfRecord = Long.valueOf(0);
        try {
            list = studentExaminationDAO.<List<StudentExaminationEntity>>getDataForGridForStudent(dataTableRequestDTO, CommonConstant.GRID_SEARCH_LIST)
                    .stream().map(entity -> {
                        StudentExaminationDTO dto = new StudentExaminationDTO();
                        dto.setId(entity.getId());
                        dto.setStudent(entity.getSysUserEntity().getUsername());
                        dto.setStudentName(entity.getSysUserEntity().getTitleEntity().getDescription()+" "+entity.getSysUserEntity().getName());
                        dto.setExaminationCode(entity.getExaminationEntity().getCode());
                        dto.setExaminationDescription(entity.getExaminationEntity().getDescription());
                        dto.setDuration(entity.getExaminationEntity().getDuration());
                        dto.setNoQuestion(entity.getExaminationEntity().getNoQuestion());
                        dto.setStatusCode(entity.getStatusEntity().getCode());
                        dto.setStatusDescription(entity.getStatusEntity().getDescription());
                        return dto;
                    }).collect(Collectors.toList());

            numOfRecord = studentExaminationDAO.<Long>getDataForGridForStudent(dataTableRequestDTO, CommonConstant.GRID_SEARC_COUNT);

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
    public ResponseDTO<HashMap<String, Object>> getReferenceDataForStudentExaminationAdd() {
        HashMap<String, Object> map = new HashMap<>();
        String code = ResponseCodeEnum.FAILED.getCode();
        try {
            Date sysDate = commonMethod.getSystemDate();
            List<DropDownDTO> examination = examinationDAO.findAllExaminationEntities(DefaultStatusEnum.ACTIVE.getCode())
                    .stream()
                    .filter(examinationEntity -> {
                        return examinationEntity.getExpireOn().compareTo(sysDate)>0;
                    })
                    .map(t-> new DropDownDTO(t.getCode(), t.getDescription()))
                    .collect(Collectors.toList());

            List<DropDownDTO> student = studentDAO.findStudents(DefaultStatusEnum.ACTIVE.getCode())
                    .stream().map(t-> new DropDownDTO(t.getUsername(), t.getTitleEntity().getDescription()+" "+t.getName()+"-"+t.getUsername()))
                    .collect(Collectors.toList());

            map.put("student", student);
            map.put("exam", examination);

            code = ResponseCodeEnum.SUCCESS.getCode();
        } catch (Exception e) {
            System.err.println("StudentExaminationAdd Ref Data Issue");
        }
        return new ResponseDTO<HashMap<String, Object>>(code, map);
    }

    @Override
    @Transactional
    public ResponseDTO<?> saveStudentExamination(StudentExaminationDTO studentExaminationDTO) {
        String code = ResponseCodeEnum.FAILED.getCode();
        String description = "Examination  Save Failed";
        SysUserEntity sysUserEntity;
        try {
            sysUserEntity = Optional.ofNullable(sysUserDAO.getSysUserEntityByUsername(studentExaminationDTO.getStudent())).orElse(new SysUserEntity());

            StatusEntity examStatusEntity = statusDAO.findStatusEntityByCode(ExamStatusEnum.PENDING.getCode());
            ExaminationEntity examinationEntity = examinationDAO.findExaminationEntityByCode(studentExaminationDTO.getExaminationCode());

            StudentExaminationEntity studentExaminationEntity = new StudentExaminationEntity();
            studentExaminationEntity.setSysUserEntity(sysUserEntity);
            studentExaminationEntity.setExaminationEntity(examinationEntity);
            studentExaminationEntity.setStatusEntity(examStatusEntity);

            commonMethod.getPopulateEntityWhenInsert(studentExaminationEntity);

            studentExaminationDAO.saveStudentExaminationEntity(studentExaminationEntity);

            code = ResponseCodeEnum.SUCCESS.getCode();
            description = "Examination  Save Success";

        }
        catch(Exception e) {
            System.err.println("Save Student User Issue");
        }
        return new ResponseDTO<>(code,description);
    }

    @Override
    @Transactional
    public ResponseDTO<?> deleteStudentExamination(StudentExaminationDTO studentExaminationDTO) {
        String code = ResponseCodeEnum.FAILED.getCode();
        String description = "Examination is Removed from Student is Faield";
        try {
            StudentExaminationEntity studentExaminationEntity = studentExaminationDAO.findStudentExaminationEntity(studentExaminationDTO.getId());
            if(ExamStatusEnum.PENDING.getCode().equals(studentExaminationEntity.getStatusEntity().getCode())){
                studentExaminationDAO.deleteStudentExaminationEntity(studentExaminationDTO.getId());
                code = ResponseCodeEnum.SUCCESS.getCode();
                description = "Examination is Removed from Student is Success";
            }
            else{

                description = "Examination State is Not Pending";
            }


        } catch (Exception e) {
            System.err.println("Getting Question for examination");
        }
        return new ResponseDTO<>(code, description);
    }

    @Override
    @Transactional
    public ResponseDTO<?> tryToCloseStudentExamination(StudentExaminationDTO studentExaminationDTO) {
        String code = ResponseCodeEnum.FAILED.getCode();
        String description = "Examination is stopped  Faield";
        try {
            Date systemDate = commonMethod.getSystemDate();
            StudentExaminationEntity studentExaminationEntity = studentExaminationDAO.findStudentExaminationEntity(studentExaminationDTO.getId());
            if(ExamStatusEnum.START.getCode().equals(studentExaminationEntity.getStatusEntity().getCode())){
                if (systemDate.compareTo(studentExaminationEntity.getEndOn()) >= 0) {
                    finaStateChangeStudentExamination(studentExaminationDTO.getId());
                    code = ResponseCodeEnum.SUCCESS.getCode();
                    description = "Examination is Removed from Student is Success";
                } else {
                    description = "Examination is not able to stop now. Please try later";
                }

            }
            else{

                description = "Examination State is Close or Pending";
            }


        } catch (Exception e) {
            System.err.println("Getting Question for examination");
        }
        return new ResponseDTO<>(code, description);
    }

    private Date getEndDateTimeForExam(Date expectedTime, Date examEndTime){
        return (expectedTime.compareTo(examEndTime) < 1)?expectedTime:examEndTime;
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
    private void finaStateChangeStudentExamination(Long studentExam){
        StudentExaminationEntity studentExaminationEntity = studentExaminationDAO.findStudentExaminationEntity(studentExam);
        StatusEntity closedExamStatusEntity = statusDAO.findStatusEntityByCode(ExamStatusEnum.CLOSED.getCode());

        studentExaminationEntity.setStatusEntity(closedExamStatusEntity);
        calculateAnswer(studentExaminationEntity);

        commonMethod.getPopulateEntityWhenUpdate(studentExaminationEntity);
        studentExaminationDAO.updateStudentExaminationEntity(studentExaminationEntity);
    }
}
