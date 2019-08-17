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

import com.leaf.job.dto.SysRoleDTO;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.ResponseDTO;
import com.leaf.job.service.SysRoleService;

@Controller
@RequestMapping(path = "/sysRole")
public class SysRoleController {

	private SysRoleService sysRoleService;

	@Autowired
	public SysRoleController(SysRoleService sysRoleService) {
		this.sysRoleService = sysRoleService;
	}

	@PreAuthorize("hasRole('ROLE_SYSROLE')")
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public ModelAndView viewSysRole() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("sysRole");
		return mv;
	}

	@PreAuthorize("hasRole('ROLE_SYSROLE')")
	@RequestMapping(path = "/loadRefDataForSysRole", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<HashMap<String, Object>> loadSysRoleRefereceData() {
		return sysRoleService.getReferenceDataForSysRole();
	}

	@PreAuthorize("hasRole('ROLE_SYSROLE')")
	@RequestMapping(path = "/save", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<?> saveSysRole(@RequestBody SysRoleDTO sysRoleDTO) {
		return sysRoleService.saveSysRole(sysRoleDTO);
	}

	@PreAuthorize("hasRole('ROLE_SYSROLE')")
	@RequestMapping(path = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<?> updateSysRole(@RequestBody SysRoleDTO sysRoleDTO) {
		return sysRoleService.updateSysRole(sysRoleDTO);
	}

	@PreAuthorize("hasRole('ROLE_SYSROLE')")
	@RequestMapping(path = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<?> daleteSysRole(@RequestBody SysRoleDTO sysRoleDTO) {
		return sysRoleService.deleteSysRole(sysRoleDTO);
	}

	@PreAuthorize("hasRole('ROLE_SYSROLE')")
	@RequestMapping(path = "/loadSysRoles", method = RequestMethod.POST)
	@ResponseBody
	public DataTableResponseDTO loadSysRoleDataGrid(@RequestBody DataTableRequestDTO dataTableRequestDTO) {
		return sysRoleService.getSysRolesForDataTable(dataTableRequestDTO);
	}

	@PreAuthorize("hasRole('ROLE_SYSROLE')")
	@RequestMapping(path = "/loadSysRoleByCode", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTO<SysRoleDTO> loadSysRoleByCode(@RequestBody SysRoleDTO sysRoleDTO) {
		return sysRoleService.findSysRole(sysRoleDTO);
	}
}
