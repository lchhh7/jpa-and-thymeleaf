package com.jinjin.jintranet.commuting.repository;

import com.jinjin.jintranet.commuting.dto.CommutingsInterface;
import com.jinjin.jintranet.model.Commuting;
import com.jinjin.jintranet.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommutingRepository extends JpaRepository<Commuting, Integer>{
	
	/*@Query(value="SELECT com.commutingTm FROM Commuting com WHERE com.memberId = :memberId AND com.attendYn ='Y' AND left(com.commutingTm,10) = left(now(),10) order by com.commutingTm desc limit 1", nativeQuery = true)
	String goToWorkTime(@Param("memberId") Member member);
	
	@Query(value="SELECT com.commutingTm FROM Commuting com WHERE com.memberId = :memberId AND com.attendYn ='N' AND left(com.commutingTm,10) = left(now(),10) order by com.commutingTm desc limit 1", nativeQuery = true)
	String offToWorkTime(@Param("memberId") Member member);
	
	@Query(value="select if(com.attendYn = 'Y' , '근무중' , '퇴근') from Commuting com where curdate() = date_format(com.commutingTm,'%Y-%m-%d') AND com.memberId = :memberId order by com.commutingTm desc limit 1", nativeQuery = true)
	String workingStatus(@Param("memberId") Member member);
	*/
	@Query(value="SELECT  com.id , com.commutingTm , com.attendYn ,  count(com.commutingTm) over(partition by date_format(com.commutingTm,'%Y-%m-%d') , com.attendYn order by com.commutingTm rows 1 preceding) as cnt FROM Commuting com WHERE com.memberId = :memberId AND date_format(com.commutingTm,'%Y-%m-%d') >= :strDt AND date_format(com.commutingTm,'%Y-%m-%d') <= :endDt and com.attendYn='Y' order by com.commutingTm desc", nativeQuery = true)
	List<CommutingsInterface> findCommuteOn(@Param("memberId") Member member , @Param("strDt") String strDt ,@Param("endDt") String endDt);
	
	@Query(value="SELECT com.id , com.commutingTm , com.attendYn ,  count(com.commutingTm) over(partition by date_format(com.commutingTm,'%Y-%m-%d') , com.attendYn order by com.id desc rows 1 preceding) as cnt FROM Commuting com WHERE com.memberId = :memberId AND date_format(com.commutingTm,'%Y-%m-%d') >= :strDt AND date_format(com.commutingTm,'%Y-%m-%d') <= :endDt and (com.attendYn='N' or com.attendYn='V') order by com.commutingTm desc", nativeQuery = true)
	List<CommutingsInterface> findCommuteOff(@Param("memberId") Member member , @Param("strDt") String strDt ,@Param("endDt") String endDt);

	@Query(value="SELECT com.id , com.commutingTm , com.attendYn ,  count(com.commutingTm) over(partition by date_format(com.commutingTm,'%Y-%m-%d') , com.attendYn order by com.id desc rows 1 preceding) as cnt FROM Commuting com WHERE com.memberId = :memberId AND date_format(com.commutingTm,'%Y-%m-%d') >= :strDt AND date_format(com.commutingTm,'%Y-%m-%d') <= :endDt and com.attendYn='O' order by com.commutingTm desc", nativeQuery = true)
	List<CommutingsInterface> findCommuteOvertime(@Param("memberId") Member member , @Param("strDt") String strDt ,@Param("endDt") String endDt);


}
