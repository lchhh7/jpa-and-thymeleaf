package com.jinjin.jintranet.notice.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.jinjin.jintranet.model.Qfile.QNotice;
import com.jinjin.jintranet.model.Qfile.QNoticeAttach;
import com.jinjin.jintranet.notice.dto.NoticeSearchDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NoticeDslRepository {
	
	private final JPAQueryFactory jPAQueryFactory;
	
	QNotice notice = QNotice.notice;
	
	QNoticeAttach noticeAttach = QNoticeAttach.noticeAttach;
	
	public Page<NoticeSearchDTO> findNotices(Pageable pageable , String keyword , String searchType) {


		List<NoticeSearchDTO> list = jPAQueryFactory.selectFrom(notice).distinct()
				.leftJoin(notice.attaches , noticeAttach)
				.where(notice.deletedBy.isNull() , searchTypeEq(searchType,keyword))
				.orderBy(notice.id.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch()
				.stream().map(n -> new NoticeSearchDTO(n)).toList();
		
		Long count = jPAQueryFactory
				.select(notice.count())
				.from(notice)
				.where(notice.deletedBy.isNull() , searchTypeEq(searchType,keyword))
				.fetchOne();
		
		return new PageImpl<>(list , pageable ,count);
	}
	
	private BooleanExpression searchTypeEq(String searchType , String keyword) { //searchTitle , searchCrtId
		if(searchType == null || "".equals(searchType) || keyword == null || "".equals(keyword)) {
			return null;
		}
		
		if(searchType.equals("searchTitle")) return notice.title.contains(keyword);
		else return notice.member.name.contains(keyword);
	}
}
