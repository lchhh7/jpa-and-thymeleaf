package com.jinjin.jintranet.commuting.repository;

import java.util.Arrays;
import java.util.List;

import com.jinjin.jintranet.commuting.dto.AdminCommuteRequestViewDTO;
import com.jinjin.jintranet.model.Schedule;
import com.querydsl.core.types.Projections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
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
	
	public Page<AdminCommuteRequestViewDTO> approvesList(Member member , Integer approveId , String status , Pageable pageable) {
		List<AdminCommuteRequestViewDTO> approvesList =  jPAQueryFactory
				.select(
						Projections.bean(AdminCommuteRequestViewDTO.class , commutingRequest.id, commutingRequest.member.name , commutingRequest.type ,
								commutingRequest.requestDt, commutingRequest.content , commutingRequest.status))
				.from(commutingRequest)
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

	public List<CommutingRequest> commutingRequestSearching(Member member,  String st , String y) {
		 List<CommutingRequest> approvesList =  jPAQueryFactory.selectFrom(commutingRequest)
				.where(memberEq2(member.getId()) , typeEq(st) , yearEq(y)).fetch();
		return approvesList;
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

	private BooleanExpression typeEq(String st) {
		if(st == null || st.equals("")) {
			return null;
		}
		return commutingRequest.type.eq(st);
	}

	private BooleanExpression yearEq(String y) {
		if(y == null || y.equals("")) {
			return null;
		}
		return commutingRequest.requestDt.contains(y);
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
