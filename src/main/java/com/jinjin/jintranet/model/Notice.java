package com.jinjin.jintranet.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
public class Notice extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, length = 100)
	private String title;
	
	@Lob
	private String content;
	
	private LocalDateTime postStrDt;
	
	private LocalDateTime postEndDt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memberId")
	private Member member;
	
	//@OnDelete(action = OnDeleteAction.CASCADE)
	@OneToMany(mappedBy = "notice", cascade = CascadeType.PERSIST)
	private List<NoticeAttach> attaches;
	
	/*public void addAttaches(NoticeAttach noticeAttach) {
		this.attaches.add(noticeAttach);
		noticeAttach.setNotice(this);
	}
	
	public void removeAttach(NoticeAttach noticeAttach) {
		this.attaches.remove(noticeAttach);
		noticeAttach.setNotice(null);
	}*/
	
	@Builder
	public Notice(Integer id, String title, String content, LocalDateTime postStrDt, LocalDateTime postEndDt,
			Member member, List<NoticeAttach> attaches) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.postStrDt = postStrDt;
		this.postEndDt = postEndDt;
		this.member = member;
		this.attaches = attaches;
	}
	
	
}
