package com.leaf.job.dao;

import java.util.List;

import com.leaf.job.entity.MasterDataEntity;

public interface MasterDataDAO {
	
    /**
     * select - *
     * From - MasterDataEntity
     * where - code = code
     * 
     * Find MasterDataEntity Entity By Code
     * @return {@link MasterDataEntity}
     */
	MasterDataEntity findMasterDataEntityByCode(String code);
	
    /**
     * Save MasterDataEntity Entity
     * @param masterDataEntity
     */
	void saveMasterDataEntity(MasterDataEntity masterDataEntity);
	
    /**
     * Update MasterDataEntity Entity
     * @param masterDataEntity
     */
	void updateMasterDataEntity(MasterDataEntity masterDataEntity);
	
    /**
     * select - *
     * From - MasterDataEntity    
     * 
     * Find All MasterDataEntity Entities
     * @return {@link List}
     */
	List<MasterDataEntity> findAllMasterDataEntities();
	
}
