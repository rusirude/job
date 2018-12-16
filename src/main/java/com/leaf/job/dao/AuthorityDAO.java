package com.leaf.job.dao;

import java.util.List;

import com.leaf.job.entity.AuthorityEntity;

public interface AuthorityDAO {

	/**
	 * select - *
	 * From - Authority
	 * Where = STATUS = statusCode
	 * 
	 * Find Authorities By Status Code
	 * 
	 * @param statusCode
	 * @return {@link List<AuthorityEntity>}
	 */
	List<AuthorityEntity> findAuthorityEntitiesByStatus(String statusCode);
}
