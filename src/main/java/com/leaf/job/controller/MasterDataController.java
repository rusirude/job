package com.leaf.job.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/masterData")
public class MasterDataController {
	
	@PreAuthorize("hasRole('ROLE_MASTERDATA')")
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public ModelAndView viewMasterData() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("masterData");
		return mv;
	}

}
