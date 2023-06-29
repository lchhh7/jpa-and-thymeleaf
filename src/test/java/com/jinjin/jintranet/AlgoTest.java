package com.jinjin.jintranet;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/*@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // 테스트환경에서 충돌을 피하는데 유용
@ExtendWith(MockitoExtension.class)*/
class tempVO {
	public String name;
	public String info;
	public String teamNm;

	public tempVO(String name, String info, String teamNm) {
		this.name = name;
		this.info = info;
		this.teamNm = teamNm;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getTeamNm() {
		return teamNm;
	}

	public void setTeamNm(String teamNm) {
		this.teamNm = teamNm;
	}

	@Override
	public String toString() {
		return "tempVO{" +
				"name='" + name + '\'' +
				", info='" + info + '\'' +
				", teamNm='" + teamNm + '\'' +
				'}';
	}
}
public class AlgoTest {

	/*@Value(value="${local.server.port}")
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private ScheduleService scheduleService;
*/

	@Test
	public void test() {

		List<tempVO> voList = new ArrayList<>();
		voList.add(new tempVO("이창훈","이창정보","정보화팀1"));
		voList.add(new tempVO("홍길동","길동정보","정보화팀1"));
		voList.add(new tempVO("이승언","승언정보","정보화팀2"));
		voList.add(new tempVO("한대현","대현정보","정보화팀2"));
		voList.add(new tempVO("한동윤","동윤정보","정보화팀3"));

		List<String> answer = voList.stream().map(m -> m.getTeamNm()).distinct().collect(Collectors.toList());
		System.out.println("answer = " + answer);

		List<List<tempVO>> test = voList.stream().map(m -> m.getTeamNm()).distinct().
				map(teamNm -> {
					return voList.stream()
						.filter(it -> it.getTeamNm().equals(teamNm))
							.collect(Collectors.toList());
				}).collect(Collectors.toList());

		System.out.println("test.toString() = " + test.toString());
	}

}
