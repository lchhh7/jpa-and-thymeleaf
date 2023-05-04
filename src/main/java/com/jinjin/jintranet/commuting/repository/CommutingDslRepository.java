package com.jinjin.jintranet.commuting.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommutingDslRepository {
	private final JPAQueryFactory jPAQueryFactory;

}
