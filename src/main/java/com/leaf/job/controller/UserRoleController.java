package com.leaf.job.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/userRole")
public class UserRoleController {
	
	@PreAuthorize("hasRole('ROLE_USERROLE')")
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public ModelAndView viewUserRole() {
		ModelAndView  mv = new  ModelAndView(); 
		mv.setViewName("userRole");
		return mv;		
	}
}
