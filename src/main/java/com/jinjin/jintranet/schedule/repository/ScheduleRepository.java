package com.jinjin.jintranet.schedule.repository;

import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer>{

	@Query("select distinct substring(s.strDt,1,4) from Schedule s where s.member =:memberId")
	List<String> yearList(@Param("memberId") Member member);
	
	@Query("select s from Schedule s where current_date >= substring(s.strDt , 0,10) and current_date <=substring(s.endDt,0,10) ")
	List<Schedule> todaySchedules();
	
	@Query("select s from Schedule s where s.member = :memberId and s.status <> 'D' and substring(s.type,1) ='V' order by s.strDt desc")
	List<Schedule> findByMemberId(@Param("memberId") Member member);

	@Query("select count(s.id) from Schedule s where s.member = :memberId and (s.status = 'R' or s.status ='C') and substring(s.type,1) ='V'")
	Integer cnt(@Param("memberId") Member member);
}
