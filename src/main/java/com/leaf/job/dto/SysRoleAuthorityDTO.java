package com.leaf.job.dto;

public class SysRoleAuthorityDTO {
	
	private String sysRoleCode;
	private String sysRoleDecription;
	private String authorityCode;
	private String authorityDescription;
	private boolean enable;
	
	public String getSysRoleCode() {
		return sysRoleCode;
	}
	public void setSysRoleCode(String sysRoleCode) {
		this.sysRoleCode = sysRoleCode;
	}
	public String getSysRoleDecription() {
		return sysRoleDecription;
	}
	public void setSysRoleDecription(String sysRoleDecription) {
		this.sysRoleDecription = sysRoleDecription;
	}
	public String getAuthorityCode() {
		return authorityCode;
	}
	public void setAuthorityCode(String authorityCode) {
		this.authorityCode = authorityCode;
	}
	public String getAuthorityDescription() {
		return authorityDescription;
	}
	public void setAuthorityDescription(String authorityDescription) {
		this.authorityDescription = authorityDescription;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
	

}
