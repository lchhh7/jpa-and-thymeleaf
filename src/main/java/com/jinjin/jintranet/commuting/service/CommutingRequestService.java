package com.jinjin.jintranet.commuting.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jinjin.jintranet.common.DateUtils;
import com.jinjin.jintranet.commuting.repository.CommutingRepository;
import com.jinjin.jintranet.commuting.repository.CommutingRequestDslRepository;
import com.jinjin.jintranet.commuting.repository.CommutingRequestRepository;
import com.jinjin.jintranet.model.Commuting;
import com.jinjin.jintranet.model.CommutingRequest;
import com.jinjin.jintranet.model.Member;

@Service
public class CommutingRequestService {
	
	private CommutingRepository commutingRepository; 

	private CommutingRequestRepository commutingRequestRepository; 
	
	private CommutingRequestDslRepository commutingRequestDslRepository; 

	public CommutingRequestService(CommutingRepository commutingRepository,
			CommutingRequestRepository commutingRequestRepository,
			CommutingRequestDslRepository commutingRequestDslRepository) {
		this.commutingRepository = commutingRepository;
		this.commutingRequestRepository = commutingRequestRepository;
		this.commutingRequestDslRepository = commutingRequestDslRepository;
	}

	@Transactional
	public List<CommutingRequest> findAll(Member member, String strDt , String endDt) {
		return commutingRequestRepository.findAll(member, strDt , endDt);
	}

	@Transactional
	public CommutingRequest findById(Integer id) {
		return commutingRequestRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("해당 일정을 조회하는중 오류가 발생했습니다");
		});
	}
	
	@Transactional
	public Page<CommutingRequest> approvesList(Member member, Integer m, String sj , Pageable pageable) {
		return commutingRequestDslRepository.approvesList(member, m , sj , pageable);
	}
	
	@Transactional
	public void approves(int id, CommutingRequest requestCommutingRequest) {
		CommutingRequest commutingRequest = commutingRequestRepository.findById(id)
				.orElseThrow(() -> {
					return new IllegalArgumentException("해당 근태를 찾을 수 없습니다.");
				});
		
		commutingRequest.setStatus(requestCommutingRequest.getStatus());
		commutingRequest.setApproveDt(LocalDateTime.now());
		
		
		if(requestCommutingRequest.getStatus().equals("Y")) {
			Commuting commuting = new Commuting();
			commuting.setAttendYn(commutingRequest.getType());
			commuting.setMember(commutingRequest.getMember());
			if(!commutingRequest.getType().equals("O")) commuting.setCommutingTm(DateUtils.toLocalDateTime(commutingRequest.getRequestDt(), commutingRequest.getRequestTm()));

			if("N".equals(commuting.getAttendYn())) {
				if(commuting.getCommutingTm().getHour() < 9) {
					commuting.setAttendYn("V");
				}
			}
			
			commutingRepository.save(commuting);
		}
	}

	@Transactional
	public List<CommutingRequest> commutingRequestSearching(Member member, String st , String y) {
		return commutingRequestDslRepository.commutingRequestSearching(member , st ,y);
	}

	@Transactional
	public List<String> yearList(Member member) {
		return commutingRequestRepository.yearList(member);
	}
}
