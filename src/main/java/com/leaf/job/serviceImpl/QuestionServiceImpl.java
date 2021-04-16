package com.leaf.job.serviceImpl;

import com.leaf.job.dao.*;
import com.leaf.job.dto.QuestionAnswerDTO;
import com.leaf.job.dto.QuestionCategoryDTO;
import com.leaf.job.dto.QuestionDTO;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.DropDownDTO;
import com.leaf.job.dto.common.ResponseDTO;
import com.leaf.job.entity.*;
import com.leaf.job.enums.DefaultStatusEnum;
import com.leaf.job.enums.DeleteStatusEnum;
import com.leaf.job.enums.ResponseCodeEnum;
import com.leaf.job.enums.StatusCategoryEnum;
import com.leaf.job.service.QuestionService;
import com.leaf.job.utility.CommonConstant;
import com.leaf.job.utility.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

	private QuestionDAO questionDAO;
	private QuestionCategoryDAO questionCategoryDAO;
	private QuestionAnswerDAO questionAnswerDAO;
	private QuestionQuestionCategoryDAO questionQuestionCategoryDAO;
	private StatusDAO statusDAO;
	private StatusCategoryDAO statusCategoryDAO;
	private CommonMethod commonMethod;

	@Autowired
	public QuestionServiceImpl(QuestionDAO questionDAO, QuestionCategoryDAO questionCategoryDAO, QuestionAnswerDAO questionAnswerDAO, QuestionQuestionCategoryDAO questionQuestionCategoryDAO, StatusDAO statusDAO, StatusCategoryDAO statusCategoryDAO, CommonMethod commonMethod) {
		this.questionDAO = questionDAO;
		this.questionCategoryDAO = questionCategoryDAO;
		this.questionAnswerDAO = questionAnswerDAO;
		this.questionQuestionCategoryDAO = questionQuestionCategoryDAO;
		this.statusDAO = statusDAO;
		this.statusCategoryDAO = statusCategoryDAO;
		this.commonMethod = commonMethod;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ResponseDTO<QuestionDTO> saveQuestion(QuestionDTO questionDTO) {
		String code = ResponseCodeEnum.FAILED.getCode();
		String description = "Question Save Faield";

		QuestionEntity questionEntity;
		try {
			questionEntity = questionDAO.findQuestionEntityByCode(questionDTO.getCode());
			if (questionEntity == null  || DeleteStatusEnum.DELETE.getCode().equals(questionEntity.getStatusEntity().getCode())) {
				final StatusEntity statusEntity = statusDAO.findStatusEntityByCode(questionDTO.getStatusCode());

				questionEntity = new QuestionEntity();
				questionEntity.setCode(questionDTO.getCode());
				questionEntity.setDescription(questionDTO.getDescription());
				questionEntity.setStatusEntity(statusEntity);

				commonMethod.getPopulateEntityWhenInsert(questionEntity);

				questionDAO.saveQuestionEntity(questionEntity);

				final QuestionEntity finalQuestionEntity = questionEntity;
				questionDTO.getQuestionCategories()
						.forEach(questionCategoryDTO -> {
							QuestionCategoryEntity categoryEntity = questionCategoryDAO.findQuestionCategoryEntityByCode(questionCategoryDTO.getCode());

							QuestionQuestionCategoryEntity questionQuestionCategoryEntity = new QuestionQuestionCategoryEntity();
							QuestionQuestionCategoryEntityId questionQuestionCategoryEntityId = new QuestionQuestionCategoryEntityId();

							questionQuestionCategoryEntityId.setQuestion(finalQuestionEntity.getId());
							questionQuestionCategoryEntityId.setQuestionCategory(categoryEntity.getId());

							questionQuestionCategoryEntity.setQuestionQuestionCategoryEntityId(questionQuestionCategoryEntityId);
							questionQuestionCategoryEntity.setQuestionEntity(finalQuestionEntity);
							questionQuestionCategoryEntity.setQuestionCategoryEntity(categoryEntity);

							questionQuestionCategoryDAO.saveQuestionQuestionCategoryEntity(questionQuestionCategoryEntity);


						});

				questionDTO.getQuestionAnswers()
						.forEach(questionAnswerDTO -> {

							QuestionAnswerEntity questionAnswerEntity = new QuestionAnswerEntity();

							questionAnswerEntity.setQuestionEntity(finalQuestionEntity);
							questionAnswerEntity.setStatusEntity(statusEntity);
							questionAnswerEntity.setDescription(questionAnswerDTO.getDescription());
							questionAnswerEntity.setCorrect(questionAnswerDTO.isCorrect());
							questionAnswerEntity.setPosition(questionAnswerDTO.getPosition());

							commonMethod.getPopulateEntityWhenInsert(questionAnswerEntity);

							questionAnswerDAO.saveQuestionAnswerEntity(questionAnswerEntity);


						});

				code = ResponseCodeEnum.SUCCESS.getCode();
				description = "Question Save Successfully";
			} else {
				description = "Question Code is Already Used ";
			}

		} catch (Exception e) {
			System.err.println("Question Save Issue");
		}
		return new ResponseDTO<>(code, description);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ResponseDTO<QuestionDTO> updateQuestion(QuestionDTO questionDTO) {
		String code = ResponseCodeEnum.FAILED.getCode();
		String description = "Question Update Faield";
		try {
			StatusEntity statusEntity = statusDAO.findStatusEntityByCode(questionDTO.getStatusCode());
			StatusEntity activeStatusEntity = statusDAO.findStatusEntityByCode(DefaultStatusEnum.ACTIVE.getCode());
			StatusEntity deleteStatusEntity = statusDAO.findStatusEntityByCode(DeleteStatusEnum.DELETE.getCode());

			QuestionEntity questionEntity = questionDAO.findQuestionEntityByCode(questionDTO.getCode());
			questionEntity.setCode(questionDTO.getCode());
			questionEntity.setDescription(questionDTO.getDescription());
			questionEntity.setStatusEntity(statusEntity);

			commonMethod.getPopulateEntityWhenUpdate(questionEntity);

			questionDAO.updateQuestionEntity(questionEntity);


			questionQuestionCategoryDAO.deleteQuestionQuestionCategoryEntityByQuestion(questionEntity.getId());
			questionDTO.getQuestionCategories()
					.forEach(questionCategoryDTO -> {
						QuestionCategoryEntity categoryEntity = questionCategoryDAO.findQuestionCategoryEntityByCode(questionCategoryDTO.getCode());

						QuestionQuestionCategoryEntity questionQuestionCategoryEntity = new QuestionQuestionCategoryEntity();
						QuestionQuestionCategoryEntityId questionQuestionCategoryEntityId = new QuestionQuestionCategoryEntityId();


						questionQuestionCategoryEntityId.setQuestion(questionEntity.getId());
						questionQuestionCategoryEntityId.setQuestionCategory(categoryEntity.getId());

						questionQuestionCategoryEntity.setQuestionQuestionCategoryEntityId(questionQuestionCategoryEntityId);
						questionQuestionCategoryEntity.setQuestionEntity(questionEntity);
						questionQuestionCategoryEntity.setQuestionCategoryEntity(categoryEntity);

						questionQuestionCategoryDAO.saveQuestionQuestionCategoryEntity(questionQuestionCategoryEntity);


					});

			List<Long> answers = new ArrayList<>();
			questionDTO.getQuestionAnswers()
					.forEach(questionAnswerDTO -> {
						QuestionAnswerEntity questionAnswerEntity = Optional
								.ofNullable(questionAnswerDAO.findQuestionAnswerEntity(Optional.ofNullable(questionAnswerDTO.getId()).orElse(0L)))
								.orElse(new QuestionAnswerEntity());

						questionAnswerEntity.setQuestionEntity(questionEntity);
						questionAnswerEntity.setPosition(questionAnswerDTO.getPosition());
						questionAnswerEntity.setStatusEntity(activeStatusEntity);
						questionAnswerEntity.setDescription(questionAnswerDTO.getDescription());
						questionAnswerEntity.setCorrect(questionAnswerDTO.isCorrect());

						if(Objects.isNull(questionAnswerEntity.getId())){
							commonMethod.getPopulateEntityWhenInsert(questionAnswerEntity);
							questionAnswerDAO.saveQuestionAnswerEntity(questionAnswerEntity);
						}
						else{
							commonMethod.getPopulateEntityWhenUpdate(questionAnswerEntity);
							questionAnswerDAO.updateQuestionAnswerEntity(questionAnswerEntity);
						}
						answers.add(questionAnswerEntity.getId());

					});

			if(!answers.isEmpty()){
				questionAnswerDAO.findAllQuestionAnswerEntitiesByQuestionAndNotInAnswers(questionEntity.getId(),answers)
						.forEach(questionAnswerEntity -> {
							questionAnswerEntity.setStatusEntity(deleteStatusEntity);
							commonMethod.getPopulateEntityWhenUpdate(questionAnswerEntity);
							questionAnswerDAO.updateQuestionAnswerEntity(questionAnswerEntity);
						});
			}





			code = ResponseCodeEnum.SUCCESS.getCode();
			description = "Question Update Successfully";
		} catch (Exception e) {
			System.err.println("Question Update Issue");
		}
		return new ResponseDTO<>(code, description);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ResponseDTO<QuestionDTO> deleteQuestion(QuestionDTO questionDTO) {
		String code = ResponseCodeEnum.FAILED.getCode();
		String description = "Question Delete Faield";
		try {
			StatusEntity statusEntity = statusDAO.findStatusEntityByCode(DeleteStatusEnum.DELETE.getCode());

			QuestionEntity questionEntity = questionDAO.findQuestionEntityByCode(questionDTO.getCode());

			questionEntity.setStatusEntity(statusEntity);

			commonMethod.getPopulateEntityWhenUpdate(questionEntity);

			questionDAO.updateQuestionEntity(questionEntity);

			questionQuestionCategoryDAO.deleteQuestionQuestionCategoryEntityByQuestion(questionEntity.getId());


			code = ResponseCodeEnum.SUCCESS.getCode();
			description = "Question Delete Successfully";
		} catch (Exception e) {
			System.err.println("Question Delete Issue");
		}
		return new ResponseDTO<>(code, description);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ResponseDTO<QuestionDTO> findQuestion(QuestionDTO questionDTO) {
		String code = ResponseCodeEnum.FAILED.getCode();
		String description = "Question is Not Found";
		QuestionDTO dto = new QuestionDTO();
		try {

			QuestionEntity questionEntity = questionDAO.findQuestionEntityByCode(questionDTO.getCode());

			if (questionEntity != null
					&& !DeleteStatusEnum.DELETE.getCode().equals(questionEntity.getStatusEntity().getCode())) {
				code = ResponseCodeEnum.SUCCESS.getCode();
				description = "Question is Found Successfully";

				dto.setCode(questionEntity.getCode());
				dto.setDescription(questionEntity.getDescription());
				dto.setStatusCode(questionEntity.getStatusEntity().getCode());
				dto.setStatusDescription(questionEntity.getStatusEntity().getDescription());
				dto.setCreatedBy(questionEntity.getCreatedBy());
				dto.setCreatedOn(questionEntity.getCreatedOn());
				dto.setUpdatedBy(questionEntity.getUpdatedBy());
				dto.setUpdatedOn(questionEntity.getUpdatedOn());

				List<QuestionCategoryDTO> questionCategoryDTOs = questionQuestionCategoryDAO.getQuestionQuestionCategoryEntity(questionEntity.getId())
						.stream()
						.map(questionQuestionCategoryEntity -> {
							QuestionCategoryDTO questionCategoryDTO = new QuestionCategoryDTO();
							questionCategoryDTO.setCode(questionQuestionCategoryEntity.getQuestionCategoryEntity().getCode());
							questionCategoryDTO.setDescription(questionQuestionCategoryEntity.getQuestionCategoryEntity().getDescription());
							return questionCategoryDTO;
						})
						.collect(Collectors.toList());

				dto.setQuestionCategories(questionCategoryDTOs);

				List<QuestionAnswerDTO> questionAnswerDTOs = questionAnswerDAO.findAllQuestionAnswerEntitiesByQuestion(questionEntity.getId(),DefaultStatusEnum.ACTIVE.getCode())
						.stream()
						.map(questionAnswerEntity -> {
							QuestionAnswerDTO questionAnswerDTO = new QuestionAnswerDTO();
							questionAnswerDTO.setId(questionAnswerEntity.getId());
							questionAnswerDTO.setDescription(questionAnswerEntity.getDescription());
							questionAnswerDTO.setCorrect(questionAnswerEntity.isCorrect());
							return questionAnswerDTO;
						})
						.collect(Collectors.toList());

				dto.setQuestionAnswers(questionAnswerDTOs);



			}

		} catch (Exception e) {
			System.err.println("Question Find Issue");
		}
		return new ResponseDTO<>(code, description, dto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ResponseDTO<HashMap<String, Object>> getReferenceDataForQuestion() {

		HashMap<String, Object> map = new HashMap<>();
		String code = ResponseCodeEnum.FAILED.getCode();
		try {
			List<DropDownDTO> status = statusCategoryDAO.findStatusCategoryByCode(StatusCategoryEnum.DEFAULT.getCode())
					.getStatusEntities().stream()
					.sorted(Comparator.comparing(StatusEntity::getDescription)).map(s -> new DropDownDTO(s.getCode(), s.getDescription()))
					.collect(Collectors.toList());

			List<DropDownDTO> questionCategory = questionCategoryDAO.findAllQuestionCategoryEntities(DefaultStatusEnum.ACTIVE.getCode())
					.stream().map(s -> new DropDownDTO(s.getCode(), s.getDescription()))
					.collect(Collectors.toList());

			map.put("status", status);
			map.put("questionCategory", questionCategory);

			code = ResponseCodeEnum.SUCCESS.getCode();
		} catch (Exception e) {
			System.err.println("Question Ref Data Issue");
		}
		return new ResponseDTO<HashMap<String, Object>>(code, map);
	}

	@Override
	@Transactional
	public DataTableResponseDTO getQuestionsForDataTable(DataTableRequestDTO dataTableRequestDTO) {
		List<QuestionDTO> list = new ArrayList<>();
		DataTableResponseDTO responseDTO = new DataTableResponseDTO();
		Long numOfRecord = Long.valueOf(0);
		try {
			list = questionDAO.<List<QuestionEntity>>getDataForGrid(dataTableRequestDTO, CommonConstant.GRID_SEARCH_LIST)
					.stream().map(entity -> {
						QuestionDTO dto = new QuestionDTO();
						dto.setCode(entity.getCode());
						dto.setDescription(entity.getDescription());
						dto.setStatusDescription(entity.getStatusEntity().getDescription());
						dto.setCreatedBy(entity.getCreatedBy());
						dto.setCreatedOn(entity.getCreatedOn());
						dto.setUpdatedBy(entity.getUpdatedBy());
						dto.setUpdatedOn(entity.getUpdatedOn());
						return dto;
					}).collect(Collectors.toList());

			numOfRecord = questionDAO.<Long>getDataForGrid(dataTableRequestDTO, CommonConstant.GRID_SEARC_COUNT);

			responseDTO.setData(list);
			responseDTO.setRecordsTotal(numOfRecord);
			responseDTO.setRecordsFiltered(numOfRecord);
			responseDTO.setDraw(dataTableRequestDTO.getDraw());

		} catch (Exception e) {
		}

		return responseDTO;
	}

}
