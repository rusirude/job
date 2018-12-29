package com.leaf.job.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/authority")
public class AuthorityController {

	@PreAuthorize("hasRole('ROLE_AUTHORITY')")
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public ModelAndView viewAuthority() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("authority");
		return mv;
	}

}
