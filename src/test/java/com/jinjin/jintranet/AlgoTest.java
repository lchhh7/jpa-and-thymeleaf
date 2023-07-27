package com.jinjin.jintranet;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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

	@Test
	public void test1() {
		List<Integer> list = new ArrayList<>();
		List<Integer> list2 = new ArrayList<>();
		list.add(3);
		list.add(1);
		list.add(2);
		list.add(2);
		list.add(3);
		list.add(4);
		
		list2.add(3);
		list2.add(1);
		list2.add(2);
		list2.add(2);
		list2.add(3);
		list2.add(4);
		
		System.out.println(list);
		
		list.remove(3);
		
		System.out.println(list);
		
		System.out.println(list2);
		
		list2.remove((Integer) 3);
		
		System.out.println(list2);
		
		int a = Collections.frequency(list, 3);
		System.out.println(a);
		
		Integer[] b = list.toArray(new Integer[0]);
	
	}
}
