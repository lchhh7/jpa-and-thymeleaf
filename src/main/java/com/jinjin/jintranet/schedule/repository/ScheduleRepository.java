package com.jinjin.jintranet.schedule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer>{
	
	@Query(value="select * from Schedule s where memberId=?1 and date_format(strDt,'%Y') = '?2' and date_format(endDt,'%Y') = ?2  and type in('FV' , 'HV') order by strDt desc" , nativeQuery = true)
	List<Schedule> vacations(Member member ,int curYear);
	
	@Query(value="select distinct left(s.strDt,4) from Schedule s where memberId =?1", nativeQuery = true)
	List<String> yearList(Member member);
	
	@Query(value="select * from Schedule s where left(now(),10) >= left(strDt,10) and left(now(),10) <=left(endDt,10)", nativeQuery = true)
	List<Schedule> todaySchedules();
	
	@Query(value="select * from Schedule s where memberId = ?1 and status != 'D' and right(type,1) ='V' order by strDt desc", nativeQuery = true)
	List<Schedule> findByMemberId(Member member);
	
	@Query(value="select count(*) from Schedule s where memberId = ?1 and (status = 'R' or status ='C') and right(type,1) ='V'", nativeQuery = true)
	Integer cnt(Member member);
}
