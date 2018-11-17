package com.leaf.job.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class InitController {
	@RequestMapping(path={"/", "/login"}, method = RequestMethod.GET)	
	public ModelAndView init() {
		ModelAndView  mv = new  ModelAndView(); 
		mv.setViewName("index");
		return mv;		
	}
	
	@RequestMapping(path="/home", method = RequestMethod.GET)	
	public ModelAndView home() {
		ModelAndView  mv = new  ModelAndView(); 
		mv.setViewName("home");
		return mv;		
	}
}

