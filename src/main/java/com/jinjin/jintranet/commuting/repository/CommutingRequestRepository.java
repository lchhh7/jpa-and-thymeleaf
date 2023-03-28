package com.jinjin.jintranet.commuting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jinjin.jintranet.model.CommutingRequest;
import com.jinjin.jintranet.model.Member;

public interface CommutingRequestRepository extends JpaRepository<CommutingRequest, Integer>{
	
	@Query(value ="select * from CommutingRequest where memberId=?1 and requestDt >= ?2 and requestDt <= ?3" , nativeQuery = true)
	List<CommutingRequest> findAll(Member member , String strDt , String endDt);
}
