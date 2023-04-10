package com.jinjin.jintranet.commuting.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import com.jinjin.jintranet.commuting.dto.AdminCommuteRequestViewDTO;
import com.jinjin.jintranet.commuting.dto.CommuteApproveDTO;
import com.jinjin.jintranet.commuting.dto.CommuteRequestViewDTO;
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
	public AdminCommuteRequestViewDTO findById(Integer id) {
		CommutingRequest commutingRequest = commutingRequestRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("해당 일정을 조회하는중 오류가 발생했습니다");
		});

		AdminCommuteRequestViewDTO dto = new AdminCommuteRequestViewDTO(commutingRequest);
		return dto;
	}
	
	@Transactional
	public Page<AdminCommuteRequestViewDTO> approvesList(Member member, Integer m, String sj , Pageable pageable) {
		return commutingRequestDslRepository.approvesList(member, m , sj , pageable);
	}
	
	@Transactional
	public void approves(int id, CommuteApproveDTO approveDTO,  Member member) {
		CommutingRequest commutingRequest = commutingRequestRepository.findById(id)
				.orElseThrow(() -> {
					return new IllegalArgumentException("해당 근태를 찾을 수 없습니다.");
				});

		commutingRequest.setStatus(approveDTO.getStatus());
		commutingRequest.setApproveDt(LocalDateTime.now());

		//연관관계 편의성 메소드
		//member.getCommutingRequests().stream().filter(m -> m.getId() == id).forEach(m -> m.setStatus(approveDTO.getStatus()));
		member.approve(id , approveDTO);

		if(approveDTO.getStatus().equals("Y") &&!commutingRequest.getType().equals("O") ) {
			Commuting commuting = new Commuting();
			commuting.setAttendYn(commutingRequest.getType());
			commuting.setMember(commutingRequest.getMember());
			commuting.setCommutingTm(DateUtils.toLocalDateTime(commutingRequest.getRequestDt(), commutingRequest.getRequestTm()));

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

	@Transactional
	public void EditRequest(int id , AdminCommuteRequestViewDTO dto) {
		CommutingRequest commutingRequest = commutingRequestRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("해당 일정을 수정하는중 오류가 발생했습니다");
		});

		commutingRequest.setRequestTm(dto.getRequestTm());
		commutingRequest.setContent(dto.getContent());
	}

	@Transactional
	public void DeleteRequest(int id , Member member) {
		CommutingRequest commutingRequest = commutingRequestRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("해당 일정을 삭제하는중 오류가 발생했습니다");
		});

		member.delete(id);

		commutingRequest.setDeletedBy(member.getName());
	}
}
