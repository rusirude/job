package com.leaf.job.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leaf.job.dao.StatusCategoryDAO;
import com.leaf.job.dao.StatusDAO;
import com.leaf.job.dao.SysRoleDAO;
import com.leaf.job.dao.TitleDAO;
import com.leaf.job.dto.common.DropDownDTO;
import com.leaf.job.dto.common.ResponseDTO;
import com.leaf.job.enums.DefaultStatusEnum;
import com.leaf.job.enums.ResponseCodeEnum;
import com.leaf.job.enums.StatusCategoryEnum;
import com.leaf.job.service.SysUserService;
import com.leaf.job.utility.CommonMethod;

@Service
public class SysUserServiceImpl implements SysUserService {
	
	private SysRoleDAO sysRoleDAO;
	private StatusDAO statusDAO;
	private TitleDAO titleDAO;
	private StatusCategoryDAO statusCategoryDAO;

	private CommonMethod commonMethod;
	
	@Autowired	
	public SysUserServiceImpl(SysRoleDAO sysRoleDAO, StatusDAO statusDAO, TitleDAO titleDAO,
			StatusCategoryDAO statusCategoryDAO, CommonMethod commonMethod) {		
		this.sysRoleDAO = sysRoleDAO;
		this.statusDAO = statusDAO;
		this.titleDAO = titleDAO;
		this.statusCategoryDAO = statusCategoryDAO;
		this.commonMethod = commonMethod;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ResponseDTO<HashMap<String, Object>> getReferenceDataForSysUser() {

		HashMap<String, Object> map = new HashMap<>();
		String code = ResponseCodeEnum.FAILED.getCode();
		try {
			List<DropDownDTO> status = statusCategoryDAO.findStatusCategoryByCode(StatusCategoryEnum.DEFAULT.getCode())
					.getStatusEntities().stream().map(s -> new DropDownDTO(s.getCode(), s.getDescription()))
					.collect(Collectors.toList());
			
			List<DropDownDTO> title = titleDAO.findAllTitleEntities(DefaultStatusEnum.ACTIVE.getCode())
					.stream().map(t-> new DropDownDTO(t.getCode(), t.getDescription()))
					.collect(Collectors.toList());

			map.put("status", status);
			map.put("title", title);

			code = ResponseCodeEnum.SUCCESS.getCode();
		} catch (Exception e) {
			System.err.println("User Role Ref Data Issue");
		}
		return new ResponseDTO<HashMap<String, Object>>(code, map);
	}

}
