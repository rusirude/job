package com.leaf.job.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.leaf.job.utility.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leaf.job.dao.AuthorityDAO;
import com.leaf.job.dao.SysRoleAuthorityDAO;
import com.leaf.job.dao.SysRoleDAO;
import com.leaf.job.dto.SysRoleAuthorityDTO;
import com.leaf.job.dto.SysRoleDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.DropDownDTO;
import com.leaf.job.dto.common.ResponseDTO;
import com.leaf.job.entity.AuthorityEntity;
import com.leaf.job.entity.SysRoleAuthorityEntity;
import com.leaf.job.entity.SysRoleAuthorityEntityId;
import com.leaf.job.entity.SysRoleEntity;
import com.leaf.job.enums.DefaultStatusEnum;
import com.leaf.job.enums.ResponseCodeEnum;
import com.leaf.job.service.SysRoleAuthorityService;

@Service
public class SysRoleAuthorityServiceImpl implements SysRoleAuthorityService {

	
	private SysRoleDAO sysRoleDAO;
	private SysRoleAuthorityDAO sysRoleAuthorityDAO;
	private AuthorityDAO authorityDAO;

	

	@Autowired
	public SysRoleAuthorityServiceImpl(SysRoleDAO sysRoleDAO,
			SysRoleAuthorityDAO sysRoleAuthorityDAO, AuthorityDAO authorityDAO) {		
		this.sysRoleDAO = sysRoleDAO;
		this.sysRoleAuthorityDAO = sysRoleAuthorityDAO;
		this.authorityDAO = authorityDAO;		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public DataTableResponseDTO getSysRoleAuthorityForSysRole(SysRoleDTO sysRoleDTO) {
		Map<String, SysRoleAuthorityDTO> sysRoleAuthorityMap = new HashMap<>();
		List<SysRoleAuthorityDTO> sysRoleAuthorities = new ArrayList<>();
		DataTableResponseDTO responseDTO = new DataTableResponseDTO();
		try {
			SysRoleEntity sysRoleEntity = sysRoleDAO.findSysRoleEntityByCode(sysRoleDTO.getCode());

			authorityDAO.findAuthorityEntitiesByStatus(DefaultStatusEnum.ACTIVE.getCode()).stream()
					.forEach(authority -> {
						SysRoleAuthorityDTO sysRoleAuthorityDTO = new SysRoleAuthorityDTO();
						sysRoleAuthorityDTO.setSysRoleCode(sysRoleEntity.getCode());
						sysRoleAuthorityDTO.setSysRoleDescription(sysRoleEntity.getDescription());
						sysRoleAuthorityDTO.setAuthorityCode(authority.getCode());
						sysRoleAuthorityDTO.setAuthorityDescription(authority.getDescription());
						sysRoleAuthorityDTO.setEnable(false);

						sysRoleAuthorityMap.put(sysRoleAuthorityDTO.getAuthorityCode(), sysRoleAuthorityDTO);

					});

			sysRoleAuthorityDAO.getSysRoleAuthorityEntitiesBySysRole(sysRoleEntity.getId()).stream()
					.forEach(sysRoleAuthority -> {
						SysRoleAuthorityDTO sysRoleAuthorityDTO = sysRoleAuthorityMap
								.get(sysRoleAuthority.getAuthorityEntity().getCode());
						sysRoleAuthorityDTO.setEnable(true);
						sysRoleAuthorityMap.put(sysRoleAuthorityDTO.getAuthorityCode(), sysRoleAuthorityDTO);
					});

			sysRoleAuthorities = sysRoleAuthorityMap.values().stream().collect(Collectors.toList());
			responseDTO.setData(sysRoleAuthorities);

		} catch (Exception e) {
			System.err.println("Getting SysRole Authority for Sys Role Issue");
			responseDTO.setData(sysRoleAuthorities);
		}
		return responseDTO;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ResponseDTO<HashMap<String, Object>> getReferenceDataForSysRoleAuthority() {

		HashMap<String, Object> map = new HashMap<>();
		String code = ResponseCodeEnum.FAILED.getCode();
		try {
			
			List<DropDownDTO> sysRole = sysRoleDAO.findAllSysRoleEntities(DefaultStatusEnum.ACTIVE.getCode())
					.stream()
					.filter(sysRoleEntity -> !CommonConstant.SYSTEM.equals(sysRoleEntity.getCode()))
					.map(ra-> new DropDownDTO(ra.getCode(),ra.getDescription())).collect(Collectors.toList());

			map.put("sysRole", sysRole);

			code = ResponseCodeEnum.SUCCESS.getCode();
		} catch (Exception e) {
			System.err.println("Sys Role Authority Ref Data Issue");
		}
		return new ResponseDTO<HashMap<String, Object>>(code, map);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ResponseDTO<SysRoleAuthorityDTO> saveSysRoleAuthority(SysRoleAuthorityDTO sysRoleAuthorityDTO){
		String code = ResponseCodeEnum.FAILED.getCode();
		String description = "Sys Role Authority Save Faield";
		try {		
			SysRoleAuthorityEntity sysRoleAuthorityEntity = new SysRoleAuthorityEntity();
			SysRoleAuthorityEntityId id = new SysRoleAuthorityEntityId();
			
			
			SysRoleEntity sysRoleEntity = sysRoleDAO.findSysRoleEntityByCode(sysRoleAuthorityDTO.getSysRoleCode());
			AuthorityEntity authorityEntity = authorityDAO.findAuthorityEntityByCode(sysRoleAuthorityDTO.getAuthorityCode());
			
			id.setSysRole(sysRoleEntity.getId());
			id.setAuthority(authorityEntity.getId());
			
			sysRoleAuthorityEntity.setSysRoleAuthorityEntityId(id);
			sysRoleAuthorityEntity.setSysRoleEntity(sysRoleEntity);
			sysRoleAuthorityEntity.setAuthorityEntity(authorityEntity);
			
			sysRoleAuthorityDAO.saveSysRoleAuthorityentity(sysRoleAuthorityEntity);
			
			description = "Sys Role Authority Save Successfully";
			code = ResponseCodeEnum.SUCCESS.getCode();
		} catch (Exception e) {
			System.err.println("SysRoleAuthority Save Issue");
		}
		return new ResponseDTO<SysRoleAuthorityDTO>(code, description);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ResponseDTO<SysRoleAuthorityDTO> deleteSysRoleAuthority(SysRoleAuthorityDTO sysRoleAuthorityDTO){
		String code = ResponseCodeEnum.FAILED.getCode();
		String description = "Sys Role Authority Delete Faield";
		try {			
			
			SysRoleEntity sysRoleEntity = sysRoleDAO.findSysRoleEntityByCode(sysRoleAuthorityDTO.getSysRoleCode());
			AuthorityEntity authorityEntity = authorityDAO.findAuthorityEntityByCode(sysRoleAuthorityDTO.getAuthorityCode());
			
			sysRoleAuthorityDAO.deleteSysRoleAuthorityEntityBySysRoleAndAuthority(sysRoleEntity.getId(), authorityEntity.getId());
			
			description = "Sys Role Authority Delete Successfully";
			code = ResponseCodeEnum.SUCCESS.getCode();
		} catch (Exception e) {
			System.err.println("SysRoleAuthority Delete Issue");
		}
		return new ResponseDTO<SysRoleAuthorityDTO>(code, description);
	}



}
