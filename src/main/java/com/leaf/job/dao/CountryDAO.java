package com.leaf.job.dao;

import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.entity.CountryEntity;

import java.util.List;

public interface CountryDAO {
	/**
	 * Load Reference Entity
	 * @param id
	 * @return {@link CountryEntity}
	 */
    CountryEntity loadCountryEntity(long id);

	/**
	 * Find Entity from DB By Id
	 * @param id
	 * @return {@link CountryEntity}
	 */
    CountryEntity findCountryEntity(long id);

	/**
	 * Find Entity from DB By Code
	 * @param code
	 * @return {@link CountryEntity}
	 */
    CountryEntity findCountryEntityByCode(String code);
    
    /**
     * Save Country Entity
     * @param countryEntity
     */
    void saveCountryEntity(CountryEntity countryEntity);
    
    /**
     * Update Country Entity
     * @param countryEntity
     */
    void updateCountryEntity(CountryEntity countryEntity);
    
    /**
     * select - *
     * From - Country
     * where - STATUS <> DELETE
     * 
     * Find Country Entities without delete
     * @return {@link List}
     */
    List<CountryEntity> findAllCountryEntities();
    
    
    /**
     * select - *
     * From - Country
     * where - STATUS = statusCode
     * 
     * Find Country Entities By Status Code
     * @return {@link List}
     */
    List<CountryEntity> findAllCountryEntities(String statusCode);
    
    /**
     * Getting Data For  Grid 
     * @param dataTableRequestDTO
     * @return 
     */
    <T> T getDataForGrid(DataTableRequestDTO dataTableRequestDTO, String type);
}
