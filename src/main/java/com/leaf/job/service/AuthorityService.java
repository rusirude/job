package com.leaf.job.service;

import java.util.HashMap;

import com.leaf.job.dto.AuthorityDTO;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.ResponseDTO;

public interface AuthorityService {

	/**
	 * Load Reference Data For Authority Page
	 * @return {@link ResponseDTO}
	 */
	ResponseDTO<HashMap<String, Object>> getReferenceDataForAuthority();
	
	
	/**
	 * Find Authority By Code
	 * @param AuthorityDTO - code 
	 * @return {@link ResponseDTO}
	 */
	ResponseDTO<AuthorityDTO> findAuthority(AuthorityDTO authorityDTO);
	
	/**
	 * Get Authorities Data For Data Table
	 * @param dataTableRequestDTO
	 * @return {@link DataTableResponseDTO}
	 */
	DataTableResponseDTO getAuthoritiesForDataTable(DataTableRequestDTO dataTableRequestDTO);
}


