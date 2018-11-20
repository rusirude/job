package com.leaf.job.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leaf.job.dao.StatusCategoryDAO;
import com.leaf.job.dao.StatusDAO;
import com.leaf.job.dao.SysRoleDAO;
import com.leaf.job.dto.SysRoleDTO;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.DropDownDTO;
import com.leaf.job.dto.common.ResponseDTO;
import com.leaf.job.enums.ResponseCodeEnum;
import com.leaf.job.enums.StatusCategoryEnum;
import com.leaf.job.service.SysRoleService;

@Service
public class SysRoleServiceImpl implements SysRoleService {
	
	private SysRoleDAO sysRoleDAO;
	private StatusDAO statusDAO;
	private StatusCategoryDAO statusCategoryDAO;
	
	
	@Autowired
	public SysRoleServiceImpl(SysRoleDAO sysRoleDAO, StatusDAO statusDAO, StatusCategoryDAO statusCategoryDAO) {
		this.sysRoleDAO = sysRoleDAO;
		this.statusDAO = statusDAO;
		this.statusCategoryDAO = statusCategoryDAO;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ResponseDTO<SysRoleDTO> saveSysRole(SysRoleDTO sysRoleDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ResponseDTO<SysRoleDTO> updateSysRole(SysRoleDTO sysRoleDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ResponseDTO<SysRoleDTO> deleteSysRole(SysRoleDTO sysRoleDTO) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ResponseDTO<SysRoleDTO> findSysRole(SysRoleDTO sysRoleDTO) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ResponseDTO<HashMap<String, Object>> getReferenceDataForSysRole(){
		
		HashMap<String, Object> map = new HashMap<>();
		String code = ResponseCodeEnum.FAILED.getCode();
		try {
			List<DropDownDTO> status = statusCategoryDAO.findStatusCategoryByCode(StatusCategoryEnum.DEFAULT.getCode())
					.getStatusEntities()
					.stream()
					.map(s-> new DropDownDTO(s.getCode(), s.getDescription())).collect(Collectors.toList());
			
			map.put("status", status);
			code = ResponseCodeEnum.SUCCESS.getCode();
		}
		catch(Exception e) {
			System.err.println("Loan Ref Data Issue");
		}
		return new ResponseDTO<HashMap<String,Object>>(code, map);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public DataTableResponseDTO getSysRolesForDataTable(DataTableRequestDTO dataTableRequestDTO) {
		return null;
	}

}
