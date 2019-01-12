package com.leaf.job.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.leaf.job.dto.SysUserDTO;
import com.leaf.job.dto.common.ResponseDTO;
import com.leaf.job.service.SysUserService;

@Controller
@RequestMapping(path = "/sysUser")
public class SysUserController {
	
	private SysUserService sysUserService;
	
	@Autowired
	public SysUserController(SysUserService sysUserService) {		
		this.sysUserService = sysUserService;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public ModelAndView viewSysRole() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("sysUser");
		return mv;
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(path = "/save", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<?> saveSysUser(@RequestBody SysUserDTO sysUserDTO) {
		return sysUserService.saveSysUser(sysUserDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(path = "/loadRefDataForSysUser", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<HashMap<String, Object>> loadSysUserRefereceData() {
		return sysUserService.getReferenceDataForSysUser();
	}
}
