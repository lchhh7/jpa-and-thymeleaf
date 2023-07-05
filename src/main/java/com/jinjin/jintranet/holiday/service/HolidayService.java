package com.jinjin.jintranet.holiday.service;

import com.jinjin.jintranet.holiday.repository.HolidayRepository;
import com.jinjin.jintranet.model.Holiday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HolidayService {

	@Autowired
	private HolidayRepository holidayRepository;

	@Transactional
	public List<Holiday> findByMonth(LocalDateTime sdt , LocalDateTime edt) {
		return holidayRepository.findByMonth(sdt , edt);
	}

	@Transactional
	public List<Holiday> countHolidayBy(int year) {
		return holidayRepository.countHolidayBy(year);
	}

	@Transactional
	public int write(Holiday holiday) {
		try {
			holidayRepository.save(holiday);
			return 1;
		} catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}
