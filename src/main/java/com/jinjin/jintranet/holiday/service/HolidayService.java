package com.jinjin.jintranet.holiday.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinjin.jintranet.holiday.repository.HolidayRepository;
import com.jinjin.jintranet.model.Holiday;

@Service
public class HolidayService {

	@Autowired
	private HolidayRepository holidayRepository;
	
	@Transactional
	public List<Holiday> findByMonth(LocalDateTime sdt , LocalDateTime edt) {
		return holidayRepository.findByMonth(sdt , edt);
	}
}
