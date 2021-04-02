package com.leaf.job.utility;

import com.leaf.job.entity.CommonEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class CommonMethod {
    public Date getSystemDate() {
        return new Date();
    }

    public Date addHoursAndMinutesToDate(Date date,int hour,int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR,hour);
        calendar.add(Calendar.MINUTE,minute);
        return calendar.getTime();
    }

    public String getUsername() {
        String username = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return username;
    }

    public <T extends CommonEntity> void getPopulateEntityWhenInsert(T t) {
        t.setCreatedBy(this.getUsername());
        t.setCreatedOn(this.getSystemDate());
        t.setUpdatedBy(this.getUsername());
        t.setUpdatedOn(this.getSystemDate());
    }

    public <T extends CommonEntity> void getPopulateEntityWhenUpdate(T t) {
        t.setUpdatedBy(this.getUsername());
        t.setUpdatedOn(this.getSystemDate());
    }

    public <T extends CommonEntity> void getPopulateEntityWhenInsert(T t, String username) {
        t.setCreatedBy(this.getUsername());
        t.setCreatedOn(this.getSystemDate());
        t.setUpdatedBy(this.getUsername());
        t.setUpdatedOn(this.getSystemDate());
    }

    public <T extends CommonEntity> void getPopulateEntityWhenUpdate(T t, String username) {
        t.setUpdatedBy(this.getUsername());
        t.setUpdatedOn(this.getSystemDate());
    }
}
