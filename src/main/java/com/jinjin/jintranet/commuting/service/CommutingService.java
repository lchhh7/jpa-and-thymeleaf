package com.jinjin.jintranet.commuting.service;

import com.jinjin.jintranet.common.FileUtils;
import com.jinjin.jintranet.commuting.dto.CommuteRequestDTO;
import com.jinjin.jintranet.commuting.dto.CommuteRequestInsertDTO;
import com.jinjin.jintranet.commuting.dto.CommutingsInterface;
import com.jinjin.jintranet.commuting.repository.CommutingDslRepository;
import com.jinjin.jintranet.commuting.repository.CommutingRepository;
import com.jinjin.jintranet.commuting.repository.CommutingRequestRepository;
import com.jinjin.jintranet.member.repository.MemberRepository;
import com.jinjin.jintranet.model.Commuting;
import com.jinjin.jintranet.model.CommutingRequest;
import com.jinjin.jintranet.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommutingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommutingService.class);
	private final MemberRepository memberRepository;
	
	private final CommutingRepository commutingRepository;

	private final CommutingDslRepository commutingDslRepository;

	private final CommutingRequestRepository commutingRequestRepository;

	@Transactional
	public void write(Commuting commuting , Member member) {
			commuting.setMember(member);
			commuting.setCommutingTm(LocalDateTime.now());
			
			if("N".equals(commuting.getAttendYn())) {
				if(commuting.getCommutingTm().getHour() < 9) {
					commuting.setAttendYn("V");
				}
			}
			commutingRepository.save(commuting);
	}

	@Transactional
	public Map<String, Object> getWorkTime(Member member) {
		Map<String, Object> map = new HashMap<>();
		map.put("goToWorkTime", commutingDslRepository.goToWorkTime(member));
		map.put("offToWorkTime", commutingDslRepository.offToWorkTime(member));
		map.put("workingStatus", commutingDslRepository.workingStatus(member));
		return map;
	}

	@Transactional
	public List<CommutingsInterface> findAll(Member member, String strDt , String endDt) {
		//출퇴근은 캘린더에서 수정할수 없어야함
		List<CommutingsInterface> list = commutingRepository.findCommuteOn(member, strDt , endDt);
		list.addAll(commutingRepository.findCommuteOff(member, strDt , endDt));
		list.addAll(commutingRepository.findCommuteOvertime(member,strDt,endDt));
		return list;
	}
	
	@Transactional
	public Commuting findById(Integer id) {
		return commutingRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("일정을 찾을 수 없습니다.");
		});
	}
	
	//승인될때 edit되어야함
	/*@Transactional
	public void edit(int id,CommuteUpdateDTO requestCommuting) {
		Commuting commuting = commutingRepository.findById(id)
				.orElseThrow(() -> {
					return new IllegalArgumentException("근태를 찾을 수 없습니다.");
				}); //영속화
		 int hour = Integer.parseInt(requestCommuting.getTm().substring(0, 2));
		 int minute = Integer.parseInt(requestCommuting.getTm().substring(3, 5));
		LocalDateTime updateTm = commuting.getCommutingTm().withHour(hour).withMinute(minute);
		commuting.setCommutingTm(updateTm);
		commuting.setChangeReason(requestCommuting.getChangeReason());
		commuting.setUdtDt(LocalDateTime.now());
		commuting.setApproveId(requestCommuting.getApproveId());
	}*/
	
	@Transactional
	public void commuteEdit(CommuteRequestInsertDTO dto , Member member) {
		
		CommutingRequest commutingRequest = new CommutingRequest();
		commutingRequest.setApprove(memberRepository.findById(dto.getApproveId()).orElseThrow(() -> {
			return new IllegalArgumentException("관리자를 찾을 수 없습니다.");
		}));
		commutingRequest.setContent(dto.getContent());
		commutingRequest.setRequestDt(dto.getRequestDt());
		commutingRequest.setRequestTm(dto.getRequestTm());
		commutingRequest.setType(dto.getType());
		commutingRequest.setMember(member);
		commutingRequest.setStatus("R");
		commutingRequest.setCreatedBy(member.getName());
		commutingRequestRepository.save(commutingRequest);

		//연관관계 편의성 메소드
		//member.add(commutingRequest);

	}

	@Transactional
	public List<CommuteRequestDTO> overtimes(List<CommutingsInterface> commute , List<CommuteRequestDTO> commuteRequests , int month) {
		int year = Integer.parseInt(commuteRequests.get(0).getRequestDt().substring(0,4));

		List<CommuteRequestDTO> timeFlake = new LinkedList<>();

		List<Integer> overtimeDates = commuteRequests.stream()
				.filter(m -> m.getStatus().equals("Y"))
				.filter(m -> LocalDate.parse(m.getRequestDt()).getMonthValue() == month)
				.map(d -> LocalDate.parse(d.getRequestDt() , DateTimeFormatter.ISO_DATE).getDayOfMonth()).sorted().toList();

		for(Integer i : overtimeDates) {
			List<LocalDateTime> dayOfTime
					= commute.stream().filter(c -> c.getCnt() == 1).filter(c -> (c.getCommutingTm().getDayOfMonth() == i && !c.getAttendYn().equals("V")) ||
					(c.getCommutingTm().minusDays(1).getDayOfMonth() == i && c.getAttendYn().equals("V"))).map(CommutingsInterface::getCommutingTm).collect(Collectors.toList());

			if(dayOfTime.size() < 2) continue;

			Duration diff = Duration.between(dayOfTime.get(0), dayOfTime.get(1));
			if(diff.toHours() > 10)  {
				CommuteRequestDTO ctoFlake = new CommuteRequestDTO();
				ctoFlake.setRequestDt(LocalDate.of(year , month , i).toString());
				ctoFlake.setHours(diff.toHours() -10);
				ctoFlake.setMinutes(diff.toMinutes() - diff.toHours()*60 );
				timeFlake.add(ctoFlake);
			}
		}

		return timeFlake;
	};

}
