package com.leaf.job.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leaf.job.dao.AuthorityDAO;
import com.leaf.job.dao.SysRoleAuthorityDAO;
import com.leaf.job.dao.SysRoleDAO;
import com.leaf.job.dao.SysUserAuthorityDAO;
import com.leaf.job.dao.SysUserDAO;
import com.leaf.job.dao.SysUserSysRoleDAO;
import com.leaf.job.dto.SysUserAuthorityDTO;
import com.leaf.job.dto.SysUserDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.DropDownDTO;
import com.leaf.job.dto.common.ResponseDTO;
import com.leaf.job.entity.SysRoleEntity;
import com.leaf.job.entity.SysUserEntity;
import com.leaf.job.enums.DefaultStatusEnum;
import com.leaf.job.enums.ResponseCodeEnum;
import com.leaf.job.service.SysUserAuthorityService;

@Service
public class SysUserAuthorityServiceImpl implements SysUserAuthorityService {

	
	private SysRoleDAO sysRoleDAO;
	private SysUserDAO sysUserDAO;
	private SysRoleAuthorityDAO sysRoleAuthorityDAO;
	private SysUserAuthorityDAO sysUserAuthorityDAO;
	private SysUserSysRoleDAO sysUserSysRoleDAO;
	private AuthorityDAO authorityDAO;

	

	@Autowired
	public SysUserAuthorityServiceImpl(SysRoleDAO sysRoleDAO, SysUserDAO sysUserDAO,
			SysRoleAuthorityDAO sysRoleAuthorityDAO, SysUserAuthorityDAO sysUserAuthorityDAO, SysUserSysRoleDAO sysUserSysRoleDAO, AuthorityDAO authorityDAO) {		
		this.sysRoleDAO = sysRoleDAO;
		this.sysUserDAO =sysUserDAO;
		this.sysRoleAuthorityDAO = sysRoleAuthorityDAO;
		this.sysUserAuthorityDAO = sysUserAuthorityDAO;
		this.sysUserSysRoleDAO = sysUserSysRoleDAO;
		this.authorityDAO = authorityDAO;		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public DataTableResponseDTO getSysUserAuthorityForSysUser(SysUserDTO sysUserDTO) {
		Map<String, SysUserAuthorityDTO> sysUserAuthorityMap = new HashMap<>();
		List<SysUserAuthorityDTO> sysUserAuthorities = new ArrayList<>();
		DataTableResponseDTO responseDTO = new DataTableResponseDTO();
		try {
			List<SysRoleEntity> sysRoleEntities = sysUserSysRoleDAO.getSysUserSysRoleEntitiesBySysUser(sysUserDTO.getUsername())
					.stream()
					.map(_entity -> _entity.getSysRoleEntity())
					.collect(Collectors.toList());
			SysUserEntity sysUserEntity = sysUserDAO.getSysUserEntityByUsername(sysUserDTO.getUsername());

			authorityDAO.findAuthorityEntitiesByStatus(DefaultStatusEnum.ACTIVE.getCode()).stream()
					.forEach(authority -> {
						SysUserAuthorityDTO sysUserAuthorityDTO = new SysUserAuthorityDTO();
						sysUserAuthorityDTO.setUsername(sysUserDTO.getUsername());
						sysUserAuthorityDTO.setTitleDescripton(sysUserEntity.getTitleEntity().getDescription());
						sysUserAuthorityDTO.setName(sysUserEntity.getName());
						sysUserAuthorityDTO.setAuthorityCode(authority.getCode());
						sysUserAuthorityDTO.setAuthorityDescription(authority.getDescription());
						sysUserAuthorityDTO.setEnable(false);

						sysUserAuthorityMap.put(sysUserAuthorityDTO.getAuthorityCode(), sysUserAuthorityDTO);

					});
			
			if(! sysRoleEntities.isEmpty()) {
				sysRoleAuthorityDAO.getSysRoleAuthorityEntitiesBySysRoles(sysRoleEntities).stream()
					.forEach(sysRoleAuthority -> {
						SysUserAuthorityDTO sysUserAuthorityDTO = Optional.ofNullable(sysUserAuthorityMap
								.get(sysRoleAuthority.getAuthorityEntity().getCode())).orElse(null);
						if(sysUserAuthorityDTO != null) {
							sysUserAuthorityDTO.setEnable(true);
							sysUserAuthorityMap.put(sysUserAuthorityDTO.getAuthorityCode(), sysUserAuthorityDTO);
						}
	
					});
			}
			
			sysUserAuthorityDAO.getSysUserAuthorityEntitiesBySysUser(sysUserDTO.getUsername()).stream()
					.forEach(sysUserAuthorty -> {
						SysUserAuthorityDTO sysUserAuthorityDTO = Optional.ofNullable(sysUserAuthorityMap
								.get(sysUserAuthorty.getAuthorityEntity().getCode())).orElse(null);
						if(sysUserAuthorityDTO != null) {
							sysUserAuthorityDTO.setEnable(sysUserAuthorty.getIsGrant()==1);
							sysUserAuthorityMap.put(sysUserAuthorityDTO.getAuthorityCode(), sysUserAuthorityDTO);
						}
					});
			

			sysUserAuthorities = sysUserAuthorityMap.values().stream().collect(Collectors.toList());
			responseDTO.setData(sysUserAuthorities);

		} catch (Exception e) {
			System.err.println("Getting SysUser Authority for Sys User Issue");
			responseDTO.setData(sysUserAuthorities);
		}
		return responseDTO;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ResponseDTO<HashMap<String, Object>> getReferenceDataForSysUserAuthority() {

		HashMap<String, Object> map = new HashMap<>();
		String code = ResponseCodeEnum.FAILED.getCode();
		try {
			
			List<DropDownDTO> sysUser = sysUserDAO.findAllSysUsereEntities(DefaultStatusEnum.ACTIVE.getCode())
					.stream()
					.map(ra-> new DropDownDTO(ra.getUsername(),ra.getUsername())).collect(Collectors.toList());
			
			

			map.put("sysUser", sysUser);

			code = ResponseCodeEnum.SUCCESS.getCode();
		} catch (Exception e) {
			System.err.println("Sys User Authority Ref Data Issue");
		}
		return new ResponseDTO<HashMap<String, Object>>(code, map);
	}
	
	/**
	 * {@inheritDoc}
	 */
//	@Override
//	@Transactional
//	public ResponseDTO<SysRoleAuthorityDTO> saveSysRoleAuthority(SysRoleAuthorityDTO sysRoleAuthorityDTO){
//		String code = ResponseCodeEnum.FAILED.getCode();
//		String description = "Sys Role Authority Save Faield";
//		try {		
//			SysRoleAuthorityEntity sysRoleAuthorityEntity = new SysRoleAuthorityEntity();
//			SysRoleAuthorityEntityId id = new SysRoleAuthorityEntityId();
//			
//			
//			SysRoleEntity sysRoleEntity = sysRoleDAO.findSysRoleEntityByCode(sysRoleAuthorityDTO.getSysRoleCode());
//			AuthorityEntity authorityEntity = authorityDAO.findAuthorityEntityByCode(sysRoleAuthorityDTO.getAuthorityCode());
//			
//			id.setSysRole(sysRoleEntity.getId());
//			id.setAuthority(authorityEntity.getId());
//			
//			sysRoleAuthorityEntity.setSysRoleAuthorityEntityId(id);
//			sysRoleAuthorityEntity.setSysRoleEntity(sysRoleEntity);
//			sysRoleAuthorityEntity.setAuthorityEntity(authorityEntity);
//			
//			sysRoleAuthorityDAO.saveSysRoleAuthorityentity(sysRoleAuthorityEntity);
//			
//			description = "Sys Role Authority Save Successfully";
//			code = ResponseCodeEnum.SUCCESS.getCode();
//		} catch (Exception e) {
//			System.err.println("SysRoleAuthority Save Issue");
//		}
//		return new ResponseDTO<SysRoleAuthorityDTO>(code, description);
//	}
//	
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	@Transactional
//	public ResponseDTO<SysRoleAuthorityDTO> deleteSysRoleAuthority(SysRoleAuthorityDTO sysRoleAuthorityDTO){
//		String code = ResponseCodeEnum.FAILED.getCode();
//		String description = "Sys Role Authority Delete Faield";
//		try {			
//			
//			SysRoleEntity sysRoleEntity = sysRoleDAO.findSysRoleEntityByCode(sysRoleAuthorityDTO.getSysRoleCode());
//			AuthorityEntity authorityEntity = authorityDAO.findAuthorityEntityByCode(sysRoleAuthorityDTO.getAuthorityCode());
//			
//			sysRoleAuthorityDAO.deleteSysRoleAuthorityEntityBySysRoleAndAuthority(sysRoleEntity.getId(), authorityEntity.getId());
//			
//			description = "Sys Role Authority Delete Successfully";
//			code = ResponseCodeEnum.SUCCESS.getCode();
//		} catch (Exception e) {
//			System.err.println("SysRoleAuthority Delete Issue");
//		}
//		return new ResponseDTO<SysRoleAuthorityDTO>(code, description);
//	}



}
