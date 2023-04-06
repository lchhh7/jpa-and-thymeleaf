package com.jinjin.jintranet.commuting.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.jinjin.jintranet.commuting.dto.CommuteRequestInsertDTO;
import com.jinjin.jintranet.commuting.dto.CommutingsInterface;
import com.jinjin.jintranet.commuting.repository.CommutingRepository;
import com.jinjin.jintranet.commuting.repository.CommutingRequestRepository;
import com.jinjin.jintranet.member.repository.MemberRepository;
import com.jinjin.jintranet.model.Commuting;
import com.jinjin.jintranet.model.CommutingRequest;
import com.jinjin.jintranet.model.Member;

@Service
public class CommutingService {
	
	private MemberRepository memberRepository; 
	
	private CommutingRepository commutingRepository; 
	
	private CommutingRequestRepository commutingRequestRepository; 
	
	
	public CommutingService(MemberRepository memberRepository , 
			CommutingRepository commutingRepository, CommutingRequestRepository commutingRequestRepository) {
		this.memberRepository = memberRepository;
		this.commutingRepository = commutingRepository;
		this.commutingRequestRepository = commutingRequestRepository;
	}

	@Transactional
	public void write(Commuting commuting , Member member) {
		try {
			commuting.setMember(member);
			commuting.setCommutingTm(LocalDateTime.now());
			
			if("N".equals(commuting.getAttendYn())) {
				if(commuting.getCommutingTm().getHour() < 9) {
					commuting.setAttendYn("V");
				}
			}
			commutingRepository.save(commuting);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Transactional
	public Map<String, Object> getWorkTime(Member member) {
		Map<String, Object> map = new HashMap<>();
		map.put("goToWorkTime", commutingRepository.goToWorkTime(member));
		map.put("offToWorkTime", commutingRepository.offToWorkTime(member));
		map.put("workingStatus", commutingRepository.workingStatus(member));
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

		//영속성 업데이트
		member.add(commutingRequest);
		//member.getCommutingRequests().add(commutingRequest);
		//member.setCommutingRequests(member.getCommutingRequests());
	}
}
