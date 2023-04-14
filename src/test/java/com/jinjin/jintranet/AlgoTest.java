package com.jinjin.jintranet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.jinjin.jintranet.commuting.service.CommutingService;
import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.Schedule;
import com.jinjin.jintranet.schedule.dto.ScheduleInsertDTO;
import com.jinjin.jintranet.schedule.repository.ScheduleRepository;
import com.jinjin.jintranet.schedule.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyLong;



@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // 테스트환경에서 충돌을 피하는데 유용
@ExtendWith(MockitoExtension.class)
public class AlgoTest {

	@Value(value="${local.server.port}")
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private ScheduleService scheduleService;


	@Test
	public void test() {
		assertThat(scheduleService).isNotNull();
	}
	@Test
	public void test2() throws Exception{
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/" , String.class))
				.contains("==========================================3");
	}
}
