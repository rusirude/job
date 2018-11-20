package com.leaf.job.dao;

import com.leaf.job.entity.StatusCategoryEntity;

public interface StatusCategoryDAO {

	/**
	 * Find Status Category By Code
	 * @param code
	 * @return {@link StatusCategoryEntity}
	 */
	StatusCategoryEntity findStatusCategoryByCode(String code);
}
