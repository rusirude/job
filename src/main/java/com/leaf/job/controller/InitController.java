package com.leaf.job.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class InitController {
	@RequestMapping(path = "")	
	public ModelAndView init() {
		ModelAndView  mv = new  ModelAndView(); 
		mv.setViewName("index");
		return mv;		
	}
}

