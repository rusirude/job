package com.leaf.job.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SysRoleAuthorityEntityId implements Serializable{
	

	private static final long serialVersionUID = 4163190891542249070L;
	private Long sysRole;
	private Long authority;
	
	@Column(name = "sys_role", nullable = false)
	public Long getSysRole() {
		return sysRole;
	}
	
	public void setSysRole(Long sysRole) {
		this.sysRole = sysRole;
	}
	
	@Column(name = "authority", nullable = false)
	public Long getAuthority() {
		return authority;
	}
	
	public void setAuthority(Long authority) {
		this.authority = authority;
	}

	@Override
	public int hashCode() {
		return Objects.hash(authority, sysRole);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SysRoleAuthorityEntityId other = (SysRoleAuthorityEntityId) obj;
		return Objects.equals(authority, other.authority) && Objects.equals(sysRole, other.sysRole);
	}
	
		 
	 
}
