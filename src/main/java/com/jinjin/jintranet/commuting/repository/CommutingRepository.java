package com.jinjin.jintranet.commuting.repository;

import com.jinjin.jintranet.commuting.dto.CommutingsInterface;
import com.jinjin.jintranet.model.Commuting;
import com.jinjin.jintranet.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommutingRepository extends JpaRepository<Commuting, Integer>{

	@Query(value="SELECT  com.id , com.commutingTm , com.attendYn ,  count(com.commutingTm) over(partition by date_format(com.commutingTm,'%Y-%m-%d') , com.attendYn order by com.commutingTm rows 1 preceding) as cnt FROM Commuting com WHERE com.memberId = :memberId AND date_format(com.commutingTm,'%Y-%m-%d') >= :strDt AND date_format(com.commutingTm,'%Y-%m-%d') <= :endDt and com.attendYn='Y' order by com.commutingTm desc", nativeQuery = true)
	List<CommutingsInterface> findCommuteOn(@Param("memberId") Member member , @Param("strDt") String strDt ,@Param("endDt") String endDt);
	
	@Query(value="SELECT com.id , com.commutingTm , com.attendYn ,  count(com.commutingTm) over(partition by date_format(com.commutingTm,'%Y-%m-%d') , com.attendYn order by com.id desc rows 1 preceding) as cnt FROM Commuting com WHERE com.memberId = :memberId AND date_format(com.commutingTm,'%Y-%m-%d') >= :strDt AND date_format(com.commutingTm,'%Y-%m-%d') <= :endDt and (com.attendYn='N' or com.attendYn='V') order by com.commutingTm desc", nativeQuery = true)
	List<CommutingsInterface> findCommuteOff(@Param("memberId") Member member , @Param("strDt") String strDt ,@Param("endDt") String endDt);

	@Query(value="SELECT com.id , com.commutingTm , com.attendYn ,  count(com.commutingTm) over(partition by date_format(com.commutingTm,'%Y-%m-%d') , com.attendYn order by com.id desc rows 1 preceding) as cnt FROM Commuting com WHERE com.memberId = :memberId AND date_format(com.commutingTm,'%Y-%m-%d') >= :strDt AND date_format(com.commutingTm,'%Y-%m-%d') <= :endDt and com.attendYn='O' order by com.commutingTm desc", nativeQuery = true)
	List<CommutingsInterface> findCommuteOvertime(@Param("memberId") Member member , @Param("strDt") String strDt ,@Param("endDt") String endDt);


}
