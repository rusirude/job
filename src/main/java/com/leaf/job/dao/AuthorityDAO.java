package com.leaf.job.dao;

import java.util.List;

import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.entity.AuthorityEntity;
import com.leaf.job.entity.SectionEntity;

public interface AuthorityDAO {
	
	
	
	
	/**
	 * select *
	 * From - Authority
	 * Where - CODE = code
	 * 
	 * Find Entity from DB By Code
	 *  
	 * @param code
	 * @return {@link SectionEntity}
	 */
	AuthorityEntity findAuthorityEntityByCode(String code);

	/**
	 * select - *
	 * From - Authority
	 * Where - STATUS = statusCode
	 * 
	 * Find Authorities By Status Code
	 * 
	 * @param statusCode
	 * @return {@link List<AuthorityEntity>}
	 */
	List<AuthorityEntity> findAuthorityEntitiesByStatus(String statusCode);
	
	
    /**
     * Getting Data For  Grid 
     * @param dataTableRequestDTO
     * @return 
     */
    <T> T getDataForGrid(DataTableRequestDTO dataTableRequestDTO, String type);
}
