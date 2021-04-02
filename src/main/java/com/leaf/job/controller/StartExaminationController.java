package com.leaf.job.controller;

import com.leaf.job.dto.CountryDTO;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.ResponseDTO;
import com.leaf.job.service.CountryService;
import com.leaf.job.service.StartExaminationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@Controller
@RequestMapping(path = "/studentExams")
public class StartExaminationController {

	private StartExaminationService startExaminationService;

	@Autowired
	public StartExaminationController(StartExaminationService startExaminationService) {
		this.startExaminationService = startExaminationService;
	}

	@PreAuthorize("hasRole('ROLE_STUEXAM')")
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public ModelAndView viewStudentExam() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("studentExams");
		return mv;
	}


	@PreAuthorize("hasRole('ROLE_STUEXAM')")
	@RequestMapping(path = "/pendingExamsForStudent", method = RequestMethod.POST)
	@ResponseBody
	public DataTableResponseDTO loadCountryDataGrid(@RequestBody DataTableRequestDTO dataTableRequestDTO) {
		return startExaminationService.getStudentExaminationForDataTable(dataTableRequestDTO);
	}

	@PreAuthorize("hasRole('ROLE_STUEXAM')")
	@RequestMapping(path = "/start/{id}", method = RequestMethod.GET)
	public ModelAndView viewStudentStartNoe(@PathVariable Long id) {
		startExaminationService.setupQuestionForExam(id);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("startExams");
		mv.addObject("id",id);
		return mv;
	}
}
