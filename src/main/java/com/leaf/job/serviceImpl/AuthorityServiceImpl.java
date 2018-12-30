package com.leaf.job.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leaf.job.dao.AuthorityDAO;
import com.leaf.job.dao.SectionDAO;
import com.leaf.job.dao.StatusCategoryDAO;
import com.leaf.job.dao.StatusDAO;
import com.leaf.job.dto.AuthorityDTO;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.DropDownDTO;
import com.leaf.job.dto.common.ResponseDTO;
import com.leaf.job.entity.AuthorityEntity;
import com.leaf.job.enums.DefaultStatusEnum;
import com.leaf.job.enums.ResponseCodeEnum;
import com.leaf.job.enums.StatusCategoryEnum;
import com.leaf.job.service.AuthorityService;
import com.leaf.job.utility.CommonConstant;
import com.leaf.job.utility.CommonMethod;

@Service
public class AuthorityServiceImpl implements AuthorityService {
	
	private AuthorityDAO authorityDAO;
	private SectionDAO sectionDAO;
	private StatusDAO statusDAO;
	private StatusCategoryDAO statusCategoryDAO;

	private CommonMethod commonMethod;
	
	@Autowired
	public AuthorityServiceImpl(AuthorityDAO authorityDAO, SectionDAO sectionDAO, StatusDAO statusDAO, StatusCategoryDAO statusCategoryDAO,
			CommonMethod commonMethod) {	
		this.authorityDAO = authorityDAO;
		this.sectionDAO = sectionDAO;
		this.statusDAO = statusDAO;
		this.statusCategoryDAO = statusCategoryDAO;
		this.commonMethod = commonMethod;
	}




	@Override
	@Transactional
	public ResponseDTO<HashMap<String, Object>> getReferenceDataForAuthority() {
		HashMap<String, Object> map = new HashMap<>();
		String code = ResponseCodeEnum.FAILED.getCode();
		try {
			List<DropDownDTO> status = statusCategoryDAO.findStatusCategoryByCode(StatusCategoryEnum.DEFAULT.getCode())
					.getStatusEntities().stream().map(s -> new DropDownDTO(s.getCode(), s.getDescription()))
					.collect(Collectors.toList());
			List<DropDownDTO> sections = sectionDAO.findAllSectionEntities(DefaultStatusEnum.ACTIVE.getCode())
					.stream().map(sc -> new DropDownDTO(sc.getCode(), sc.getDescription())).collect(Collectors.toList());

			map.put("status", status);
			map.put("sections",sections);

			code = ResponseCodeEnum.SUCCESS.getCode();
		} catch (Exception e) {
			System.err.println("Section Ref Data Issue");
		}
		return new ResponseDTO<HashMap<String, Object>>(code, map);
	}
	
	@Override
	@Transactional
	public DataTableResponseDTO getAuthoritiesForDataTable(DataTableRequestDTO dataTableRequestDTO) {
		List<AuthorityDTO> list = new ArrayList<>();
		DataTableResponseDTO responseDTO = new DataTableResponseDTO();
		Long numOfRecord = Long.valueOf(0);
		try {
			list = authorityDAO.<List<AuthorityEntity>>getDataForGrid(dataTableRequestDTO, CommonConstant.GRID_SEARCH_LIST)
					.stream().map(entity -> {
						AuthorityDTO dto = new AuthorityDTO();
						dto.setCode(entity.getCode());
						dto.setDescription(entity.getDescription());
						dto.setUrl(entity.getUrl());
						dto.setSectionDescription(entity.getSectionEntity().getDescription());
						dto.setStatusDescription(entity.getStatusEntity().getDescription());
						dto.setCreatedBy(entity.getCreatedBy());
						dto.setCreatedOn(entity.getCreatedOn());
						dto.setUpdatedBy(entity.getUpdatedBy());
						dto.setUpdatedOn(entity.getUpdatedOn());
						return dto;
					}).collect(Collectors.toList());

			numOfRecord = authorityDAO.<Long>getDataForGrid(dataTableRequestDTO, CommonConstant.GRID_SEARC_COUNT);

			responseDTO.setData(list);
			responseDTO.setRecordsTotal(numOfRecord);
			responseDTO.setRecordsFiltered(numOfRecord);
			responseDTO.setDraw(dataTableRequestDTO.getDraw());

		} catch (Exception e) {
		}

		return responseDTO;
	}

}
