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
import com.leaf.job.service.SysUserAuthorityService;

@Controller
@RequestMapping(path = "/sysUserAuthority")
public class SysUserAuthorityController {
	
	private SysUserAuthorityService sysUserAuthorityService;	
	
	@Autowired
	public SysUserAuthorityController(SysUserAuthorityService sysUserAuthorityService) {		
		this.sysUserAuthorityService = sysUserAuthorityService;
	}

	@PreAuthorize("hasRole('ROLE_USERAUTHORITY')")
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public ModelAndView viewSysRoleAuthority() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("sysUserAuthority");
		return mv;
	}
	
	@PreAuthorize("hasRole('ROLE_USERAUTHORITY')")
	@RequestMapping(path = "/loadRefDataForSysUserAuthority", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<HashMap<String, Object>> loadysRoleAuthorityRefereceData() {
		return sysUserAuthorityService.getReferenceDataForSysUserAuthority();
	}

}
