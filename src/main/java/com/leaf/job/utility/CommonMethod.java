package com.leaf.job.utility;

import java.util.Date;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.leaf.job.entity.CommonEntity;

@Component
public class CommonMethod {
	public Date getSystemDate() {
		return new Date();
	}
	
	public String getUsername() {
		String username = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
		  username = ((UserDetails)principal).getUsername();
		} else {
		  username = principal.toString();
		}
		
		return username;
	}
	
	public<T extends CommonEntity> void getPopulateEntityWhenInsert(T t) {
		t.setCreatedBy(this.getUsername());
		t.setCreatedOn(this.getSystemDate());
		t.setUpdatedBy(this.getUsername());
		t.setUpdatedOn(this.getSystemDate());
	}
	
	public<T extends CommonEntity> void getPopulateEntityWhenUpdate(T t) {
		t.setUpdatedBy(this.getUsername());
		t.setUpdatedOn(this.getSystemDate());
	}
}
