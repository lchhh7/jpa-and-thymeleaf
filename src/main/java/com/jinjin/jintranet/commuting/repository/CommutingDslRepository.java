package com.jinjin.jintranet.commuting.repository;

import com.jinjin.jintranet.model.Commuting;
import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.QCommuting;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class CommutingDslRepository {
	private final JPAQueryFactory jPAQueryFactory;
	QCommuting commuting = QCommuting.commuting;


	public Commuting goToWorkTime(Member member) {
		return jPAQueryFactory
				.selectFrom(commuting)
				.where(commuting.member.eq(member), onCheck()).orderBy(commuting.commutingTm.desc()).limit(1).fetchOne();
	}

	public Commuting offToWorkTime(Member member) {
		return jPAQueryFactory
				.selectFrom(commuting)
				.where(commuting.member.eq(member) , offCheck()).orderBy(commuting.commutingTm.desc()).limit(1).fetchOne();
	}

	public Commuting workingStatus(Member member) {
		return jPAQueryFactory
				.selectFrom(commuting)
				.where(commuting.member.eq(member) , datecheck(LocalDate.now())).orderBy(commuting.commutingTm.desc()).limit(1).fetchOne();
	}

	public BooleanBuilder datecheck(LocalDate LastCommute) {
		BooleanBuilder builder = new BooleanBuilder();
		StringExpression formattedDate = Expressions.stringTemplate("FUNCTION('DATE_FORMAT' , {0} , '%Y-%m-%d')" , commuting.commutingTm);
		return builder.and(formattedDate.eq(LastCommute.toString().substring(0,10)));
	}
	public BooleanBuilder onCheck() {
		BooleanBuilder builder = new BooleanBuilder();
		return builder.and(commuting.attendYn.eq("Y")).and(datecheck(LocalDate.now()));
	}

	public BooleanBuilder offCheck() {
		BooleanBuilder builder = new BooleanBuilder();

		return builder.and(commuting.attendYn.eq("N").and(datecheck(LocalDate.now())))
				.or(commuting.attendYn.eq("V").and(datecheck(LocalDate.now().plusDays(1))));
	}
}
