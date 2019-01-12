package com.leaf.job.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leaf.job.dao.StatusCategoryDAO;
import com.leaf.job.dao.StatusDAO;
import com.leaf.job.dao.SysUserDAO;
import com.leaf.job.dao.TitleDAO;
import com.leaf.job.dto.SysUserDTO;
import com.leaf.job.dto.common.DropDownDTO;
import com.leaf.job.dto.common.ResponseDTO;
import com.leaf.job.entity.StatusEntity;
import com.leaf.job.entity.SysUserEntity;
import com.leaf.job.entity.TitleEntity;
import com.leaf.job.enums.DefaultStatusEnum;
import com.leaf.job.enums.ResponseCodeEnum;
import com.leaf.job.enums.StatusCategoryEnum;
import com.leaf.job.service.SysUserService;
import com.leaf.job.utility.CommonMethod;

@Service
public class SysUserServiceImpl implements SysUserService {
	
	private SysUserDAO sysUserDAO;
	private StatusDAO statusDAO;
	private TitleDAO titleDAO;
	private StatusCategoryDAO statusCategoryDAO;

	private CommonMethod commonMethod;
	
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired	
	public SysUserServiceImpl(SysUserDAO sysUserDAO, StatusDAO statusDAO, TitleDAO titleDAO,
			StatusCategoryDAO statusCategoryDAO, CommonMethod commonMethod,BCryptPasswordEncoder bCryptPasswordEncoder) {		
		this.sysUserDAO = sysUserDAO;
		this.statusDAO = statusDAO;
		this.titleDAO = titleDAO;
		this.statusCategoryDAO = statusCategoryDAO;
		this.commonMethod = commonMethod;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ResponseDTO<SysUserDTO> saveSysUser(SysUserDTO sysUserDTO){
		String code = ResponseCodeEnum.FAILED.getCode();
		String description = "User Save Faield";
		SysUserEntity sysUserEntity;
		try {
			sysUserEntity = sysUserDAO.getSysUserEntityByUsername(sysUserDTO.getUsername());
			if(sysUserEntity == null){
				sysUserEntity = new SysUserEntity();
				
				TitleEntity titleEntity = titleDAO.findTitleEntityByCode(sysUserDTO.getTitleCode());
				StatusEntity statusEntity = statusDAO.findStatusEntityByCode(sysUserDTO.getStatusCode());
				
				sysUserEntity.setUsername(sysUserDTO.getUsername());
				sysUserEntity.setPassword(bCryptPasswordEncoder.encode("123"));
				sysUserEntity.setTitleEntity(titleEntity);
				sysUserEntity.setName(sysUserDTO.getName());
				sysUserEntity.setStatusEntity(statusEntity);
				
				commonMethod.getPopulateEntityWhenInsert(sysUserEntity);
				
				sysUserDAO.saveSysUserEntity(sysUserEntity);
				
				code = ResponseCodeEnum.SUCCESS.getCode();
				description = "User Save Successfully";
				
			}
			
			else {
				description = "Username is not Avaialble in the System";
			}
		}
		catch(Exception e) {
			System.err.println("Save Sys User Issue");
		}
		return new ResponseDTO<SysUserDTO>(code,description);
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
			System.err.println("Sys User Ref Data Issue");
		}
		return new ResponseDTO<HashMap<String, Object>>(code, map);
	}

}
