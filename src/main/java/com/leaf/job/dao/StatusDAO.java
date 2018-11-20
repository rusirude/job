package com.leaf.job.dao;

import com.leaf.job.entity.StatusEntity;

public interface StatusDAO {
	
	/**
	 * Find Status Entity By Code
	 * @param code
	 * @return {@link StatusEntity}
	 */
	StatusEntity findStatusEntityByCode(String code);
}
