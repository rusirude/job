package com.leaf.job.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.leaf.job.dto.common.ResponseDTO;
import com.leaf.job.service.SysRoleService;

@Controller
@RequestMapping(path = "/userRole")
public class SysRoleController {
	
	private SysRoleService sysRoleService;
	
	
	@Autowired
	public SysRoleController(SysRoleService sysRoleService) {	
		this.sysRoleService = sysRoleService;
	}

	@PreAuthorize("hasRole('ROLE_USERROLE')")
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public ModelAndView viewSysRole() {
		ModelAndView  mv = new  ModelAndView(); 
		mv.setViewName("userRole");
		return mv;		
	}
	
	@PreAuthorize("hasRole('ROLE_USERROLE')")
	@RequestMapping(path = "/loadRefDataForSysRole", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<HashMap<String, Object>> loadRefereceDataForSysRole() {
		return sysRoleService.getReferenceDataForSysRole();
	}
}
