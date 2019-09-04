package com.leaf.job.service;

import java.util.HashMap;
import java.util.List;

import com.leaf.job.dto.MasterDataDTO;
import com.leaf.job.dto.common.ResponseDTO;

public interface MasterDataService {

	/**
	 * Load Reference Data For Master Data Page
	 * @return {@link ResponseDTO}
	 */
	ResponseDTO<HashMap<String, Object>> getReferenceDataForMasterData();

	/**
	 * Get All Master Data
	 * @return {@link ResponseDTO}
	 */
	ResponseDTO<List<MasterDataDTO>> loadAllMasterData();
	
	/**
	 * Save or Update Master Data
	 * @param masterDataDTOS
	 * @return {@link ResponseDTO}
	 */
	ResponseDTO<MasterDataDTO> saveOrUpdateMasterData(List<MasterDataDTO> masterDataDTOS);
}
