package com.jinjin.jintranet.holiday.repository;

import com.jinjin.jintranet.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface HolidayRepository extends JpaRepository<Holiday, Integer>{

	@Query(value="SELECT * FROM Holiday h WHERE holidayDt between ?1 and ?2", nativeQuery = true)
	List<Holiday> findByMonth(LocalDateTime sdt , LocalDateTime edt);

	@Query("SELECT h FROM Holiday h WHERE year(h.holidayDt) =:year ORDER BY h.holidayDt")
	List<Holiday> countHolidayBy(@Param("year") int year);

}
