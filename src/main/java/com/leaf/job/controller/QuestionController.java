package com.leaf.job.controller;

import com.leaf.job.dto.QuestionDTO;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.ResponseDTO;
import com.leaf.job.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@Controller
@RequestMapping(path = "/question")
public class QuestionController {

	private QuestionService questionService;

	@Autowired
	public QuestionController(QuestionService questionService) {
		this.questionService = questionService;
	}

	@PreAuthorize("hasRole('ROLE_QUS')")
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public ModelAndView viewQuestion() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("question");
		return mv;
	}

	@PreAuthorize("hasRole('ROLE_QUS')")
	@RequestMapping(path = "/loadRefDataForQuestion", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<HashMap<String, Object>> loadQuestionReferenceData() {
		return questionService.getReferenceDataForQuestion();
	}

	@PreAuthorize("hasRole('ROLE_QUS')")
	@RequestMapping(path = "/save", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<?> saveQuestion(@RequestBody QuestionDTO questionDTO) {
		return questionService.saveQuestion(questionDTO);
	}

	@PreAuthorize("hasRole('ROLE_QUS')")
	@RequestMapping(path = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<?> updateQuestion(@RequestBody QuestionDTO questionDTO) {
		return questionService.updateQuestion(questionDTO);
	}

	@PreAuthorize("hasRole('ROLE_QUS')")
	@RequestMapping(path = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<?> deleteQuestion(@RequestBody QuestionDTO questionDTO) {
		return questionService.deleteQuestion(questionDTO);
	}

	@PreAuthorize("hasRole('ROLE_QUS')")
	@RequestMapping(path = "/loadQuestionCategories", method = RequestMethod.POST)
	@ResponseBody
	public DataTableResponseDTO loadQuestionDataGrid(@RequestBody DataTableRequestDTO dataTableRequestDTO) {
		return questionService.getQuestionsForDataTable(dataTableRequestDTO);
	}

	@PreAuthorize("hasRole('ROLE_QUS')")
	@RequestMapping(path = "/loadQuestionByCode", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<QuestionDTO> loadQuestionByCode(@RequestBody QuestionDTO questionDTO) {
		return questionService.findQuestion(questionDTO);
	}
}
