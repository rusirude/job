package com.leaf.job.service;

import com.leaf.job.dto.CountryDTO;
import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.dto.common.DataTableResponseDTO;
import com.leaf.job.dto.common.ResponseDTO;

import java.util.HashMap;

public interface CountryService {
	/**
	 * Save Country
	 * @param countryDTO
	 * @return {@link ResponseDTO}
	 */
	ResponseDTO<CountryDTO> saveCountry(CountryDTO countryDTO);
	
	/**
	 * Update Country
	 * @param countryDTO
	 * @return {@link ResponseDTO}
	 */
	ResponseDTO<CountryDTO> updateCountry(CountryDTO countryDTO);
	
	/**
	 * Delete Country
	 * @param countryDTO - code
	 * @return {@link ResponseDTO}
	 */
	ResponseDTO<CountryDTO> deleteCountry(CountryDTO countryDTO);
	
	/**
	 * Find Country By Code
	 * @param countryDTO - code
	 * @return {@link ResponseDTO}
	 */
	ResponseDTO<CountryDTO> findCountry(CountryDTO countryDTO);
	
	/**
	 * Load Reference Data For Country Page
	 * @return {@link ResponseDTO}
	 */
	ResponseDTO<HashMap<String, Object>> getReferenceDataForCountry();
	
	/**
	 * Get Countries Data For Data Table
	 * @param dataTableRequestDTO
	 * @return {@link DataTableResponseDTO}
	 */
	DataTableResponseDTO getCountrysForDataTable(DataTableRequestDTO dataTableRequestDTO);
}
