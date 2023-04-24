package com.jinjin.jintranet.notice.repository;

import com.jinjin.jintranet.model.Qfile.QMember;
import com.jinjin.jintranet.model.Qfile.QNotice;
import com.jinjin.jintranet.model.Qfile.QNoticeAttach;
import com.jinjin.jintranet.notice.dto.NoticeSearchDTO;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NoticeDslRepository {
	
	private final JPAQueryFactory jPAQueryFactory;
	
	QNotice notice = QNotice.notice;

	QMember qMember = QMember.member;
	
	QNoticeAttach noticeAttach = QNoticeAttach.noticeAttach;
	
	public Page<NoticeSearchDTO> findNotices(Pageable pageable , String keyword , String searchType) {

		List<NoticeSearchDTO> list = jPAQueryFactory.select(
				Projections.bean(NoticeSearchDTO.class , notice.id , notice.title, notice.member, notice.crtDt ,
						ExpressionUtils.as(
								JPAExpressions.select(noticeAttach.count())
										.from(noticeAttach)
										.where(noticeAttach.notice.eq(notice) , noticeAttach.deletedBy.isNull()) , "attachesCnt")
				))
				.from(notice).leftJoin(notice.member , qMember)
				.where(notice.deletedBy.isNull() , searchTypeEq(searchType,keyword))
				.orderBy(notice.id.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		/*List<NoticeSearchDTO> list = jPAQueryFactory.selectFrom(notice)
				.leftJoin(notice.member , qMember)
				.fetchJoin()
				.where(notice.deletedBy.isNull() , searchTypeEq(searchType,keyword))
				.orderBy(notice.id.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch()
				.stream().map(n -> new NoticeSearchDTO(n)).toList();*/

		
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
