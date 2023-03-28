package com.jinjin.jintranet.holiday.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jinjin.jintranet.model.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Integer>{

	@Query(value="SELECT * FROM Holiday h WHERE holidayDt between ?1 and ?2", nativeQuery = true)
	List<Holiday> findByMonth(LocalDateTime sdt , LocalDateTime edt);
}
