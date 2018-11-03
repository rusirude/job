package com.leaf.job.entity;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class AuthorityEntity {
	private Long id;
	private String code;
	private String description;
	private String authCode;
	private StatusEntity statusEntity;
	
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
	
	@Column(name = "auth_code", length = 20, nullable = false)
	public String getAuthCode() {
		return authCode;
	}
	
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name =  "status" , nullable = false)
	public StatusEntity getStatusEntity() {
		return statusEntity;
	}

	public void setStatusEntity(StatusEntity statusEntity) {
		this.statusEntity = statusEntity;
	}
	
	
	
	
}
