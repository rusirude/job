package com.leaf.job.controller;

import com.leaf.job.dao.MasterDataDAO;
import com.leaf.job.dto.SysUserDTO;
import com.leaf.job.dto.common.ResponseDTO;
import com.leaf.job.entity.MasterDataEntity;
import com.leaf.job.service.SysUserService;
import com.leaf.job.utility.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class InitController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private MasterDataDAO masterDataDAO;
	@Autowired
	private CommonMethod commonMethod;

	@RequestMapping(path={"/", "/login"}, method = RequestMethod.GET)	
	public ModelAndView init() {
		ModelAndView  mv = new  ModelAndView(); 
		mv.setViewName("index");
		return mv;		
	}
	
	@RequestMapping(path="/home", method = RequestMethod.GET)	
	public ModelAndView home() {
		SysUserDTO sysUserDTO = new SysUserDTO();
		sysUserDTO.setUsername(commonMethod.getUsername());
		ResponseDTO<SysUserDTO> sysUser = sysUserService.findSysUser(sysUserDTO);
		ModelAndView  mv = new  ModelAndView();
		mv.addObject("user",(!sysUser.getData().getTitleCode().equals("CODE_NONE"))?sysUser.getData().getTitleDescription()+" "+sysUser.getData().getName():sysUser.getData().getName());
		mv.addObject("company", Optional.ofNullable(masterDataDAO.findMasterDataEntity("COMPANY_NAME")).orElse(new MasterDataEntity()).getValue());
		mv.addObject("logo", Optional.ofNullable(masterDataDAO.findMasterDataEntity("COMPANY_LOGO")).orElse(new MasterDataEntity()).getValue());
		mv.setViewName("home");
		return mv;		
	}
}

