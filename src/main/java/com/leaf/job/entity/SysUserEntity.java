package com.leaf.job.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="sys_user")
public class SysUserEntity extends CommonEntity{
	private String username;
	private String password;
	private String name;
	private StatusEntity statusEntity;
	
	private Set<SysUserSysRoleEntity> sysUserSysRoleEntities = new HashSet<>();
	
	@Id
	@Column(name  = "username", length = 25 , nullable = false , unique = true)
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name  = "password", length = 255 , nullable = false)
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name  = "name", length = 100 , nullable = false)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name =  "status" , nullable = false)
	public StatusEntity getStatusEntity() {
		return statusEntity;
	}
	
	public void setStatusEntity(StatusEntity statusEntity) {
		this.statusEntity = statusEntity;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sysUserEntity")
	public Set<SysUserSysRoleEntity> getSysUserSysRoleEntities() {
		return sysUserSysRoleEntities;
	}

	public void setSysUserSysRoleEntities(Set<SysUserSysRoleEntity> sysUserSysRoleEntities) {
		this.sysUserSysRoleEntities = sysUserSysRoleEntities;
	}
	
	
	
	
	
	
}
