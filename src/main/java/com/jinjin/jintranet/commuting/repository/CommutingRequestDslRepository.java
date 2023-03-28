package com.jinjin.jintranet.commuting.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.jinjin.jintranet.model.CommutingRequest;
import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.Qfile.QCommutingRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommutingRequestDslRepository {
	private final JPAQueryFactory jPAQueryFactory;
	
	QCommutingRequest commutingRequest = QCommutingRequest.commutingRequest;
	
	public Page<CommutingRequest> approvesList(Member member , Integer approveId , String status , Pageable pageable) {
		List<CommutingRequest> approvesList =  jPAQueryFactory.selectFrom(commutingRequest)
				.where(approveEq(member),memberEq2(approveId) ,statusBb(status))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		Long count =  jPAQueryFactory
				.select(commutingRequest.count())
				.from(commutingRequest)
				.where(approveEq(member),memberEq2(approveId) ,statusBb(status))
				.fetchOne();
		
		return new PageImpl<>(approvesList , pageable , count);
	}
	
	private BooleanExpression approveEq(Member member) {
		if(member == null) {
			return null;
		}
		return commutingRequest.approve.eq(member);
	}
	
	private BooleanExpression memberEq2(Integer approveId) {
		if(approveId == null) {
			return null;
		}
		return commutingRequest.member.id.eq(approveId);
	}
	
	private BooleanBuilder statusBb(String status) {
		BooleanBuilder builder = new BooleanBuilder();
		String[] typeArr = status.split(",");
		/*if(typeArr[0] == "R") {
			return builder.or(schedule.status.eq(typeArr[0])).or(schedule.status.eq(typeArr[1])).or(schedule.status.eq(typeArr[2])).or(schedule.status.eq("R"));
		}*/
		return builder.or(commutingRequest.status.eq(typeArr[0])).or(commutingRequest.status.eq(typeArr[1])).or(commutingRequest.status.eq(typeArr[2]));
	}
}
