package com.jinjin.jintranet.member.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.jinjin.jintranet.member.dto.MemberSearchDTO;
import com.jinjin.jintranet.model.DepartmentType;
import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.PositionType;
import com.jinjin.jintranet.model.Qfile.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberDslRepository {
	
	private final JPAQueryFactory jPAQueryFactory;
	
	
	QMember member = QMember.member;
	public Page<MemberSearchDTO> findMembers(Member m, Pageable pageable, String n, String p, String d) {
		List<MemberSearchDTO> list = jPAQueryFactory.select(
				Projections.constructor(MemberSearchDTO.class ,member.id , member.name , member.memberId ,
						member.position , member.department
						, member.phoneNo , member.mobileNo))
				.from(member)
				.where(searchNameEq(n) , searchPositionEq(p) , searchDepartmentEq(d) , member.deletedBy.isNull())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		Long count = jPAQueryFactory
				.select(member.count())
				.from(member)
				.fetchOne();
		
		return new PageImpl<>(list , pageable ,count);
	}
	
	private BooleanExpression searchNameEq(String n) {
		if(n == null || "".equals(n)) {
			return null;
		}
		return member.name.contains(n);
	}
	
	private BooleanExpression searchPositionEq(String p) {
		if(p == null || "".equals(p)) {
			return null;
		}
		return member.position.eq(PositionType.valueOf(p));
	}
	
	private BooleanExpression searchDepartmentEq(String d) {
		if(d == null || "".equals(d)) {
			return null;
		}
		return member.department.eq(DepartmentType.valueOf(d));
	}
	
}
