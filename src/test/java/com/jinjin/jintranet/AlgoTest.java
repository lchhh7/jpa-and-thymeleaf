package com.jinjin.jintranet;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
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

	public void swap(int[] a, int i,int j) {
		int temp = a[i];
		a[i]  = a[j];
		a[j] = temp;
	}

	@Test
	public void selectSort() {
		int[] a = {254,3,213,64,75,56,4,324,65,78,9,5,76,3410,8,342,76};
		int b;

		for(int i=0; i < a.length-1 ; i++) {
			int minIdx = i;
			for(int j = i+1 ; j < a.length ; j++) {
				if(a[j] < a[minIdx]) {
					minIdx = j;
				}
			}
			swap(a,minIdx,i);
		}

		Arrays.stream(a).mapToObj(j -> j + " ").forEach(System.out::print);
	}

	@Test
	public void bubbleSort() {
		int[] a = {254,3,213,64,75,56,4,324,65,78,9,5,76,3410,8,342,76};
		int b;

		for(int i=0; i < a.length; i++) {
			for(int j=0; j < a.length-1-i; j++) {
				if(a[j] > a[j+1]) {
					swap(a , j , j+1);
				}
			}
		}

		Arrays.stream(a).mapToObj(j -> j + " ").forEach(System.out::print);
	}

	@Test
	public void selectTest(){
		int[] a = {254,3,213,64,75,56,4,324,65,78,9,5,76,3410,8,342,76};
		for(int i=0; i < a.length -1; i++) {
			int minIdx = i;
			for(int j=i+1 ; j< a.length ; j++) {
				if(a[minIdx] > a[j]) {
					minIdx = j;
				}
			}
			swap(a , minIdx ,i);
		}
		Arrays.stream(a).mapToObj(j -> j + " ").forEach(System.out::print);
	}

	@Test
	public void bubbleTest() {
		int[] a = {254,3,213,64,75,56,4,324,65,78,9,5,76,3410,8,342,76};
		for(int i=0; i < a.length ; i++) {
			for(int j= 0 ; j < a.length-1 -i; j++) {
				if(a[j+1] < a[j]) {
					swap(a , j+1 , j);
				}
			}
		}
		Arrays.stream(a).mapToObj(j -> j + " ").forEach(System.out::print);
	}

	@Test
	public void quickTest() {
		int[] a = {254,3,213,64,75,56,4,324,65,78,9,5,76,3410,8,342,76};
		quickSort(a ,0, a.length-1);
		Arrays.stream(a).mapToObj(j -> j + " ").forEach(System.out::print);
	}
	public void quickSort(int[] a , int start , int end) {
		int idx1 = start;
		int idx2 = end;
		int pivot = a[idx1];

		while(idx1<=idx2) {
			if(pivot > a[idx1]) idx1++;
			else {
				int temp = a[idx1];
				if(pivot < a[idx2]) {
					idx2--;
				}else {
					a[idx1] = a[idx2];
					a[idx2] = temp;
					idx1++;
					idx2--;
				}
			}
		}
		if(start<idx2) {
			quickSort(a,start,idx2);
		}
		if(end > idx1) {
			quickSort(a,idx1 , end);
		}
	}

	@Test
	public void bt() {
		int[] a = {254,3,213,64,75,56,4,324,65,78,9,5,76,3410,8,342,76};

		for(int i=0; i < a.length -1 ; i++) {
			for(int j=0; j < a.length -1 -i; j++) {
				if(a[j] > a[j+1]) {
					swap(a , j, j+1);
				}
			}
		}

		Arrays.stream(a).mapToObj(j -> j + " ").forEach(System.out::print);
	}

	@Test
	public void st() {
		int[] a = {254,3,213,64,75,56,4,324,65,78,9,5,76,3410,8,342,76};

		for(int i=0; i < a.length-1 ; i++) {
			int minIdx = i;
			for(int j= i+1 ; j < a.length ; j++) {
				if(a[minIdx] > a[j]) {
					minIdx = j;
				}
			}

			swap(a , minIdx , i);
		}


		Arrays.stream(a).mapToObj(j -> j + " ").forEach(System.out::print);
	}

	@Test
	public void qt() {
		int[] a = {254,3,213,64,75,56,4,324,65,78,9,5,76,3410,8,342,76};
		qs(a , 0, a.length-1);
		Arrays.stream(a).mapToObj(j -> j + " ").forEach(System.out::print);
	}

	public void qs(int[] a , int start, int end) {
		int idx1 = start;
		int idx2 = end;
		int pivot = a[idx1];

		while(idx1 <= idx2) {
			if(pivot > a[idx1]) idx1++;
			else{
				int temp = a[idx1];
				if(pivot < a[idx2]) idx2--;
				else {
					a[idx1] = a[idx2];
					a[idx2] = temp;
					idx1++; idx2 --;
				}
			}
		}
		if(start <idx2) qs(a , start, idx2);
		if(idx1 < end) qs(a , idx1 , end);
	}
}
