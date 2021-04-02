package com.leaf.job.serviceImpl;

import com.leaf.job.dao.QuestionDAO;
import com.leaf.job.dao.StatusDAO;
import com.leaf.job.dao.StudentExaminationDAO;
import com.leaf.job.dao.StudentExaminationQuestionAnswerDAO;
import com.leaf.job.dto.QuestionDTO;
import com.leaf.job.dto.StudentExaminationDTO;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.ResponseDTO;
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
    private StudentExaminationQuestionAnswerDAO studentExaminationQuestionAnswerDAO;
    private CommonMethod commonMethod;

    @Autowired
    public StartExaminationServiceImpl(StudentExaminationDAO studentExaminationDAO, StatusDAO statusDAO, QuestionDAO questionDAO, StudentExaminationQuestionAnswerDAO studentExaminationQuestionAnswerDAO, CommonMethod commonMethod) {
        this.studentExaminationDAO = studentExaminationDAO;
        this.statusDAO = statusDAO;
        this.questionDAO = questionDAO;
        this.studentExaminationQuestionAnswerDAO = studentExaminationQuestionAnswerDAO;
        this.commonMethod = commonMethod;
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
    public ResponseDTO<List<QuestionDTO>> setupQuestionForExam(Long id) {
        List<QuestionDTO> list = new ArrayList<>();
        String code = ResponseCodeEnum.FAILED.getCode();
        AtomicInteger i = new AtomicInteger(0);
        try {
            StudentExaminationEntity studentExaminationEntity = studentExaminationDAO.findStudentExaminationEntity(id);
            StatusEntity statusEntity = statusDAO.findStatusEntityByCode(DefaultStatusEnum.ACTIVE.getCode());
            StatusEntity startExamStatusEntity = statusDAO.findStatusEntityByCode(ExamStatusEnum.START.getCode());
            if(ExamStatusEnum.PENDING.getCode().equals(studentExaminationEntity.getStatusEntity().getCode())){
                questionDAO.findAllQuestionEntitiesRandomly(statusEntity.getId(), studentExaminationEntity.getExaminationEntity().getNoQuestion(), studentExaminationEntity.getExaminationEntity().getQuestionCategoryEntity().getId())
                        .forEach(questionEntity -> {
                            StudentExaminationQuestionAnswerEntity questionAnswerEntity = new StudentExaminationQuestionAnswerEntity();
                            questionAnswerEntity.setStudentExaminationEntity(studentExaminationEntity);
                            questionAnswerEntity.setQuestionEntity(questionEntity);
                            questionAnswerEntity.setSeq(i.incrementAndGet());

                            commonMethod.getPopulateEntityWhenInsert(questionAnswerEntity);
                            studentExaminationQuestionAnswerDAO.saveStudentExaminationQuestionAnswerEntity(questionAnswerEntity);

                        });

                Date startDateTime = commonMethod.getSystemDate();
                String[] duration =  studentExaminationEntity.getExaminationEntity().getDuration().split(":");
                Date endDateTime = commonMethod.addHoursAndMinutesToDate(startDateTime,Integer.parseInt(duration[0]),Integer.parseInt(duration[1]));


                studentExaminationEntity.setStatusEntity(startExamStatusEntity);
                studentExaminationEntity.setStartOn(startDateTime);
                studentExaminationEntity.setEndOn(endDateTime);

                commonMethod.getPopulateEntityWhenUpdate(studentExaminationEntity);
                studentExaminationDAO.updateStudentExaminationEntity(studentExaminationEntity);
            }
            else{
                System.out.println("nn");
            }

        } catch (Exception e) {
            System.err.println(e);
        }

        return new ResponseDTO<>(code, list);
    }

    @Override
    @Transactional
    public ResponseDTO<List<QuestionDTO>> getQuestionsForExamination(Long id) {
        List<QuestionDTO> list = new ArrayList<>();
        String code = ResponseCodeEnum.FAILED.getCode();
        try {


        } catch (Exception e) {
            System.err.println(e);
        }

        return new ResponseDTO<>(code, list);
    }
}
