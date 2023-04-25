package com.jinjin.jintranet.commuting.repository;

import com.jinjin.jintranet.model.CommutingRequest;
import com.jinjin.jintranet.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommutingRequestRepository extends JpaRepository<CommutingRequest, Integer>{
	
	@Query("select com from CommutingRequest com where com.member= :memberId and com.requestDt >= :strDt and com.requestDt <= :endDt")
	List<CommutingRequest> findAll(@Param("memberId") Member member , @Param("strDt") String strDt , @Param("endDt") String endDt);

	@Query("SELECT DISTINCT year(com.requestDt) FROM CommutingRequest com WHERE com.member = :memberId")
	List<String> yearList(@Param("memberId") Member member);

	@Query("select com from CommutingRequest com left join com.member where com.member= :memberId")
	List<CommutingRequest> findCommute(@Param("memberId") Member member);
}
