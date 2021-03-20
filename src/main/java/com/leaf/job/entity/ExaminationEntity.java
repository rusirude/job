package com.leaf.job.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "examination")
public class ExaminationEntity extends CommonEntity{
	
	private Long id;
    private String code;
	private String description;
    private StatusEntity statusEntity;
	private Date effectiveOn;
	private Date expireOn;


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
	@JoinColumn(name =  "status" , nullable = false)
	public StatusEntity getStatusEntity() {
		return statusEntity;
	}
	
	public void setStatusEntity(StatusEntity statusEntity) {
		this.statusEntity = statusEntity;
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
