package com.jinjin.jintranet.schedule.repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.Schedule;
import com.jinjin.jintranet.model.Qfile.QMember;
import com.jinjin.jintranet.model.Qfile.QSchedule;
import com.jinjin.jintranet.schedule.dto.ScheduleSearchDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ScheduleDslRepository {
	
	private final JPAQueryFactory jPAQueryFactory;
	
	QSchedule schedule = QSchedule.schedule;
	
	QMember member = QMember.member;
	public List<ScheduleSearchDTO> findSchedule(Schedule s) {
		List<String> ids = Arrays.stream(s.getType().split(",")).collect(Collectors.toList());
		
		return jPAQueryFactory.select( Projections.constructor(ScheduleSearchDTO.class , schedule.id , schedule.type , schedule.title,
				schedule.strDt ,schedule.endDt , schedule.status , schedule.color))
				.from(schedule)
				.where(memberEq(s.getMember()),
						schedule.type.in(ids),
						statusEq(s) , 
						schedule.endDt.between(s.getStrDt(), s.getEndDt()) , 
						schedule.status.ne("D")
						)
				.fetch();
	}
	
	private BooleanExpression memberEq(Member memb) {
		if(memb == null) {
			return null;
		}
		return schedule.member.eq(memb);
	}
	
	private BooleanExpression statusEq(Schedule s) {
		if(s.getStatus() == null) {
			return null;
		}
		return schedule.status.eq(s.getStatus());
	}
	
	public Page<Schedule> approvesList(Member member , Integer approveId , List<String> statusList , Pageable pageable) {
		List<Schedule> approvesList =  jPAQueryFactory.selectFrom(schedule)
				.where(approveEq(member),
						memberEq2(approveId) ,
						schedule.status.in(statusList))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		Long count =  jPAQueryFactory
				.select(schedule.count())
				.from(schedule)
				.where(approveEq(member),memberEq2(approveId) ,schedule.status.in(statusList))
				.fetchOne();
		
		return new PageImpl<>(approvesList , pageable , count);
	}
	
	private BooleanExpression approveEq(Member member) {
		if(member == null) {
			return null;
		}
		return schedule.approve.eq(member);
	}
	
	private BooleanExpression memberEq2(Integer approveId) {
		if(approveId == null) {
			return null;
		}
		return schedule.member.id.eq(approveId);
	}
	
	public List<Schedule> vacations(Member member ,int curYear) {
		List<String> ids = Arrays.asList("HV" , "FV");
		
		return jPAQueryFactory.selectFrom(schedule)
				.where(memberEq(member),
						schedule.strDt.year().eq(curYear) ,
						schedule.endDt.year().eq(curYear) ,
						schedule.type.in(ids) ,
						schedule.status.eq("Y")
						)
				.orderBy(schedule.strDt.desc())
				.fetch();
	}
	
	public List<Schedule> mainSchedules(Member m , String year) {
		List<String> ids = Arrays.asList("HV" , "FV");
		return jPAQueryFactory.selectFrom(schedule)
				.where(memberEq(m), yearEq(year) , schedule.status.ne("D") , schedule.type.in(ids))
				.fetch();
	}
	
	private BooleanExpression yearEq(String year) {
		if(year == null || "".equals(year)) {
			return null;
		}
		return schedule.strDt.year().eq(Integer.parseInt(year));
	}
}
