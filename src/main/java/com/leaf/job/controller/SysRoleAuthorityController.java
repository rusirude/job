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
import com.leaf.job.service.SysRoleAuthorityService;

@Controller
@RequestMapping(path = "/sysRoleAuthority")
public class SysRoleAuthorityController {
	
	private SysRoleAuthorityService sysRoleAuthorityService;

	@Autowired
	public SysRoleAuthorityController(SysRoleAuthorityService sysRoleAuthorityService) {
		this.sysRoleAuthorityService = sysRoleAuthorityService;
	}
	
	@PreAuthorize("hasRole('ROLE_ROLEAUTHORITY')")
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public ModelAndView viewSection() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("sysRoleAuthority");
		return mv;
	}
	
	@PreAuthorize("hasRole('ROLE_ROLEAUTHORITY')")
	@RequestMapping(path = "/loadRefDataForSysRoleAuthority", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<HashMap<String, Object>> loadSectionRefereceData() {
		return sysRoleAuthorityService.getReferenceDataForSysRoleAuthority();
	}

	
}
