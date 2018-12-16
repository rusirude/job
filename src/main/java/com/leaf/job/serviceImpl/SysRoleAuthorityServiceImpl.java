package com.leaf.job.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leaf.job.dao.AuthorityDAO;
import com.leaf.job.dao.SysRoleAuthorityDAO;
import com.leaf.job.dao.SysRoleDAO;
import com.leaf.job.dto.SysRoleAuthorityDTO;
import com.leaf.job.dto.SysRoleDTO;
import com.leaf.job.dto.common.DropDownDTO;
import com.leaf.job.dto.common.ResponseDTO;
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
	public List<SysRoleAuthorityDTO> getSysRoleAuthorityForSysRole(SysRoleDTO sysRoleDTO) {
		Map<String, SysRoleAuthorityDTO> sysRoleAuthorityMap = new HashMap<>();
		List<SysRoleAuthorityDTO> sysRoleAuthorities = new ArrayList<>();
		try {
			SysRoleEntity sysRoleEntity = sysRoleDAO.findSysRoleEntityByCode(sysRoleDTO.getCode());

			authorityDAO.findAuthorityEntitiesByStatus(DefaultStatusEnum.ACTIVE.getCode()).stream()
					.forEach(authority -> {
						SysRoleAuthorityDTO sysRoleAuthorityDTO = new SysRoleAuthorityDTO();
						sysRoleAuthorityDTO.setSysRoleCode(sysRoleEntity.getCode());
						sysRoleAuthorityDTO.setSysRoleDecription(sysRoleEntity.getDescription());
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

		} catch (Exception e) {
			System.err.println("Getting SysRole Authority for Sys Role Issue");
		}
		return sysRoleAuthorities;
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
					.map(ra-> new DropDownDTO(ra.getCode(),ra.getDescription())).collect(Collectors.toList());

			map.put("sysRole", sysRole);

			code = ResponseCodeEnum.SUCCESS.getCode();
		} catch (Exception e) {
			System.err.println("Sys Role Authority Ref Data Issue");
		}
		return new ResponseDTO<HashMap<String, Object>>(code, map);
	}


}
