package com.leaf.job.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "status")
public class StatusEntity {
	
	private Long id;
	private String code;
	private String description;
	private StatusCategoryEntity statusCategoryEntity;
	
	
	private Set<UserEntity> userEntities;
	private Set<AuthorityEntity> authorityEntities;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "code", length = 10, nullable = false)
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name = "description", length = 50, nullable = false)
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_category",nullable = false)
	public StatusCategoryEntity getStatusCategoryEntity() {
		return statusCategoryEntity;
	}
	
	public void setStatusCategoryEntity(StatusCategoryEntity statusCategoryEntity) {
		this.statusCategoryEntity = statusCategoryEntity;
	}

	@OneToMany(mappedBy = "statusEntity", fetch = FetchType.LAZY)
	public Set<UserEntity> getUserEntities() {
		return userEntities;
	}

	public void setUserEntities(Set<UserEntity> userEntities) {
		this.userEntities = userEntities;
	}

	@OneToMany(mappedBy = "statusEntity", fetch = FetchType.LAZY)
	public Set<AuthorityEntity> getAuthorityEntities() {
		return authorityEntities;
	}

	public void setAuthorityEntities(Set<AuthorityEntity> authorityEntities) {
		this.authorityEntities = authorityEntities;
	}
	
	
	
	
	
}
