package com.leaf.job.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="student")
public class StudentEntity extends CommonEntity{
    private String username;
    private Date dob;
    private String email;
    private String telephone;
    private Date effectiveOn;
    private Date expireOn;

    @Id
    @Column(name  = "username", length = 25 , nullable = false , unique = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dob")
    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    @Column(name  = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name  = "telephone")
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "effective_on")
    public Date getEffectiveOn() {
        return effectiveOn;
    }

    public void setEffectiveOn(Date effectiveOn) {
        this.effectiveOn = effectiveOn;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expier_on")
    public Date getExpireOn() {
        return expireOn;
    }

    public void setExpireOn(Date expireOn) {
        this.expireOn = expireOn;
    }
}