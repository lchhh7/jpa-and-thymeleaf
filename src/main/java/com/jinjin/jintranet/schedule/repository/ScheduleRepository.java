package com.jinjin.jintranet.schedule.repository;

import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer>{
	
	@Query(value="select * from Schedule s where s.memberId=:memberId and date_format(s.strDt,'%Y') = :curYear and date_format(s.endDt,'%Y') = :curYear  and s.type in('FV' , 'HV') order by s.strDt desc" , nativeQuery = true)
	List<Schedule> vacations(@Param("memberId") Member member ,@Param("curYear") int curYear);
	
	@Query(value="select distinct left(s.strDt,4) from Schedule s where s.memberId =:memberId", nativeQuery = true)
	List<String> yearList(@Param("memberId") Member member);
	
	@Query(value="select * from Schedule s where left(now(),10) >= left(s.strDt,10) and left(now(),10) <=left(s.endDt,10)", nativeQuery = true)
	List<Schedule> todaySchedules();
	
	@Query(value="select * from Schedule s where s.memberId = :memberId and s.status != 'D' and right(s.type,1) ='V' order by s.strDt desc", nativeQuery = true)
	List<Schedule> findByMemberId(@Param("memberId") Member member);

	@Query(value="select count(*) from Schedule s where s.memberId = :memberId and (s.status = 'R' or s.status ='C') and right(s.type,1) ='V'", nativeQuery = true)
	Integer cnt(@Param("memberId") Member member);
}
