package com.jinjin.jintranet.model;

import java.time.LocalDateTime;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
	
	private String createdBy;
	
	@CreatedDate
	private LocalDateTime crtDt;
	
	private String deletedBy;
	
	private String ModifiedBy;
	
	@LastModifiedDate
	private LocalDateTime udtDt;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCrtDt() {
		return crtDt;
	}

	public void setCrtDt(LocalDateTime crtDt) {
		this.crtDt = crtDt;
	}

	public String getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(String deletedBy) {
		this.deletedBy = deletedBy;
	}

	public String getModifiedBy() {
		return ModifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		ModifiedBy = modifiedBy;
	}

	public LocalDateTime getUdtDt() {
		return udtDt;
	}

	public void setUdtDt(LocalDateTime udtDt) {
		this.udtDt = udtDt;
	}
}
