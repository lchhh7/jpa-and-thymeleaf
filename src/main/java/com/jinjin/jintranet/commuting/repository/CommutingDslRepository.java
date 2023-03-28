package com.jinjin.jintranet.commuting.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jinjin.jintranet.model.Qfile.QCommuting;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommutingDslRepository {
	private final JPAQueryFactory jPAQueryFactory;
	
	QCommuting commuting = QCommuting.commuting;
	QCommuting subCommuting = new QCommuting("subCommuting");
	public List<Tuple> findCommute() {
		
		return jPAQueryFactory.select(commuting.attendYn , commuting.commutingTm , commuting.member)
				.from(commuting)
				.where()
				.fetch();
	}
	
	/*private BooleanExpression DateEq(String strDt , String endDt) {
		if(strDt == null || endDt == null) {
			return null;
		}
		return schedule.member.eq(memb);
	}*/
}
