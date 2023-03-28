package com.jinjin.jintranet.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
public class NoticeAttach extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="noticeId")
	private Notice notice;
	
	private String path;
	
	private String originalFileName;
	
	private String storedFileName;
	
	private Long fileSize;
	
	@Builder
	public NoticeAttach(Integer id, Notice notice, String path, String originalFileName, String storedFileName,
			Long fileSize) {
		this.id = id;
		this.notice = notice;
		this.path = path;
		this.originalFileName = originalFileName;
		this.storedFileName = storedFileName;
		this.fileSize = fileSize;
	}
	
}
