package com.leaf.job.controller;

import com.leaf.job.dto.*;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.ResponseDTO;
import com.leaf.job.service.CountryService;
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
@RequestMapping(path = "/studentExams")
public class StartExaminationController {

	private StartExaminationService startExaminationService;
	private ReportUtil reportUtil;

	@Autowired
	public StartExaminationController(StartExaminationService startExaminationService, ReportUtil reportUtil) {
		this.startExaminationService = startExaminationService;
		this.reportUtil = reportUtil;
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
	public ModelAndView viewStudentStartExams(@PathVariable Long id) {
		ResponseDTO<Integer> responseDTO = startExaminationService.setupQuestionForExam(id);
		ModelAndView mv = new ModelAndView();
		if(responseDTO.getData()>0){
			mv.setViewName("startExams");
			mv.addObject("id",id);
			mv.addObject("current",responseDTO.getData());
		}
		else {
			ResponseDTO<FinalResultDTO> dto = startExaminationService.getFinalResult(id);
			mv.addObject("id",id);
			mv.addObject("data",dto.getData());
			mv.setViewName("finalResult");
		}

		return mv;
	}

	@PreAuthorize("hasRole('ROLE_STUEXAM')")
	@RequestMapping(path = "/question/{studentExam}/{seq}", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<?> getQuestion(@PathVariable Long studentExam,@PathVariable Integer seq) {
		return startExaminationService.getQuestionsForExamination(studentExam,seq);
	}

	@PreAuthorize("hasRole('ROLE_STUEXAM')")
	@RequestMapping(path = "/saveAnswer", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<?> saveSection(@RequestBody AnswerDTO answerDTO) {
		return startExaminationService.saveAnswer(answerDTO);
	}

	@PreAuthorize("hasRole('ROLE_STUEXAM')")
	@RequestMapping(path = "/end/{id}", method = RequestMethod.GET)
	public ModelAndView viewStudentEndExam(@PathVariable Long id) {
		startExaminationService.saveFinalAnswerCalculation(id);
		ModelAndView mv = new ModelAndView();
		ResponseDTO<FinalResultDTO> dto = startExaminationService.getFinalResult(id);
		mv.addObject("id",id);
		mv.addObject("data",dto.getData());
		mv.setViewName("finalResult");

		return mv;
	}
	@PreAuthorize("hasAnyRole('ROLE_STUEXAM','ROLE_STUEXAMADD')")
	@RequestMapping(value = "generateReport/{studentExams}", method = RequestMethod.GET)
	public void generateAnswerList(HttpServletResponse response, @PathVariable long studentExams) {

		try {
			ReportDTO reportDTO = new ReportDTO();
			reportDTO.setReportName("answer.jrxml");
			reportDTO.setDownloadName("answers");
			reportDTO.setReportPath(CommonConstant.REPORT_PATH);
			reportUtil.createReportDownload(response, reportDTO);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
