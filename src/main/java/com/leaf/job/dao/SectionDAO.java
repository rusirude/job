package com.leaf.job.dao;

import java.util.List;

import com.leaf.job.dto.common.DataTableRequestDTO;
import com.leaf.job.entity.SectionEntity;

public interface SectionDAO {
	/**
	 * Load Reference Entity
	 * @param id
	 * @return {@link SectionEntity}
	 */
    SectionEntity loadSectionEntity(long id);

	/**
	 * Find Entity from DB By Id
	 * @param id
	 * @return {@link SectionEntity}
	 */
    SectionEntity findSectionEntity(long id);

	/**
	 * Find Entity from DB By Code
	 * @param code
	 * @return {@link SectionEntity}
	 */
    SectionEntity findSectionEntityByCode(String code);
    
    /**
     * Save Section Entity
     * @param sectionEntity
     */
    void saveSectionEntity(SectionEntity sectionEntity);
    
    /**
     * Update Section Entity
     * @param sectionEntity
     */
    void updateSectionEntity(SectionEntity sectionEntity);
    
    /**
     * Find All Not Deleted System Roles
     * @return {@link List}
     */
    List<SectionEntity> findAllSectionEntities();
    
    /**
     * Getting Data For  Grid 
     * @param dataTableRequestDTO
     * @return 
     */
    <T> T getDataForGrid(DataTableRequestDTO dataTableRequestDTO, String type);
}
