package com.jinjin.jintranet;

import com.jinjin.jintranet.schedule.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;



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
