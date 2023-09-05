package com.jinjin.jintranet.holiday.service;

import com.jinjin.jintranet.commuting.service.CommutingService;
import com.jinjin.jintranet.holiday.repository.HolidayRepository;
import com.jinjin.jintranet.model.Holiday;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayService {
	private static final Logger LOGGER = LoggerFactory.getLogger(HolidayService.class);

	private final HolidayRepository holidayRepository;

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
			LOGGER.info("holiday write error : "+e);
			return -1;
		}
	}
}
