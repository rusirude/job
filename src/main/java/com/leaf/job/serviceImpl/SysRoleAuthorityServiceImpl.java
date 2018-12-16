package com.leaf.job.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.leaf.job.dao.AuthorityDAO;
import com.leaf.job.dao.StatusDAO;
import com.leaf.job.dao.SysRoleAuthorityDAO;
import com.leaf.job.dao.SysRoleDAO;
import com.leaf.job.dto.SysRoleAuthorityDTO;
import com.leaf.job.dto.SysRoleDTO;
import com.leaf.job.entity.SysRoleEntity;
import com.leaf.job.enums.DefaultStatusEnum;
import com.leaf.job.service.SysRoleAuthorityService;
import com.leaf.job.utility.CommonMethod;

public class SysRoleAuthorityServiceImpl implements SysRoleAuthorityService {

	private StatusDAO statusDAO;
	private SysRoleDAO sysRoleDAO;
	private SysRoleAuthorityDAO sysRoleAuthorityDAO;
	private AuthorityDAO authorityDAO;

	private CommonMethod commonMethod;

	@Autowired
	public SysRoleAuthorityServiceImpl(StatusDAO statusDAO, SysRoleDAO sysRoleDAO,
			SysRoleAuthorityDAO sysRoleAuthorityDAO, AuthorityDAO authorityDAO, CommonMethod commonMethod) {
		this.statusDAO = statusDAO;
		this.sysRoleDAO = sysRoleDAO;
		this.sysRoleAuthorityDAO = sysRoleAuthorityDAO;
		this.authorityDAO = authorityDAO;
		this.commonMethod = commonMethod;
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

}
