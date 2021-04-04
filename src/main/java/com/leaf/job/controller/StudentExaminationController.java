package com.leaf.job.controller;

import com.leaf.job.dto.*;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.ResponseDTO;
import com.leaf.job.service.StartExaminationService;
import com.leaf.job.utility.CommonConstant;
import com.leaf.job.utility.ReportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Controller
@RequestMapping(path = "/studentExamination")
public class StudentExaminationController {

	private StartExaminationService startExaminationService;
	private ReportUtil reportUtil;

	@Autowired
	public StudentExaminationController(StartExaminationService startExaminationService, ReportUtil reportUtil) {
		this.startExaminationService = startExaminationService;
		this.reportUtil = reportUtil;
	}

	@PreAuthorize("hasRole('ROLE_STUEXAMADD')")
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public ModelAndView viewStudentExam() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("studentExaminationAdd");
		return mv;
	}


	@PreAuthorize("hasRole('ROLE_STUEXAMADD')")
	@RequestMapping(path = "/exams", method = RequestMethod.POST)
	@ResponseBody
	public DataTableResponseDTO loadCountryDataGrid(@RequestBody DataTableRequestDTO dataTableRequestDTO) {
		return startExaminationService.getStudentExaminationForDataTableForAdd(dataTableRequestDTO);
	}

	@PreAuthorize("hasRole('ROLE_STUEXAMADD')")
	@RequestMapping(path = "/loadRefDataForStudentExaminationAdd", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<HashMap<String, Object>> loadCountryReferenceData() {
		return startExaminationService.getReferenceDataForStudentExaminationAdd();
	}

	@PreAuthorize("hasRole('ROLE_STUEXAMADD')")
	@RequestMapping(path = "/save", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<?> saveStudentExamination(@RequestBody StudentExaminationDTO studentExaminationDTO) {
		return startExaminationService.saveStudentExamination(studentExaminationDTO);
	}

	@PreAuthorize("hasRole('ROLE_STUEXAMADD')")
	@RequestMapping(path = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<?> deleteStudentExamination(@RequestBody StudentExaminationDTO studentExaminationDTO) {
		return startExaminationService.deleteStudentExamination(studentExaminationDTO);
	}

	@PreAuthorize("hasRole('ROLE_STUEXAMADD')")
	@RequestMapping(path = "/tryToClose", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<?> tryToCloseStudentExamination(@RequestBody StudentExaminationDTO studentExaminationDTO) {
		return startExaminationService.tryToCloseStudentExamination(studentExaminationDTO);
	}


}
