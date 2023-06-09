package com.jinjin.jintranet.commuting.service;

import com.jinjin.jintranet.common.DateUtils;
import com.jinjin.jintranet.commuting.dto.AdminCommuteRequestViewDTO;
import com.jinjin.jintranet.commuting.dto.CommuteApproveDTO;
import com.jinjin.jintranet.commuting.dto.CommuteRequestDTO;
import com.jinjin.jintranet.commuting.dto.CommuteRequestViewDTO;
import com.jinjin.jintranet.commuting.repository.CommutingRepository;
import com.jinjin.jintranet.commuting.repository.CommutingRequestDslRepository;
import com.jinjin.jintranet.commuting.repository.CommutingRequestRepository;
import com.jinjin.jintranet.model.Commuting;
import com.jinjin.jintranet.model.CommutingRequest;
import com.jinjin.jintranet.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommutingRequestService {
	
	private final CommutingRepository commutingRepository;

	private final CommutingRequestRepository commutingRequestRepository;
	
	private final CommutingRequestDslRepository commutingRequestDslRepository;

	@Transactional
	public AdminCommuteRequestViewDTO findById(Integer id) {
		CommutingRequest commutingRequest = commutingRequestRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("해당 일정을 조회하는중 오류가 발생했습니다");
		});

		AdminCommuteRequestViewDTO dto = new AdminCommuteRequestViewDTO(commutingRequest);
		return dto;
	}
	
	@Transactional
	public Page<AdminCommuteRequestViewDTO> approvesList(Member member, Integer m, List<String> statusList , Pageable pageable) {
		return commutingRequestDslRepository.approvesList(member, m , statusList , pageable);
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
		//member.approve(id , approveDTO);

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
	public List<CommuteRequestViewDTO> commutingRequestSearching(Member member, String st , String y) {
		return commutingRequestDslRepository.commutingRequestSearching(member , st ,y).stream().map(m -> new CommuteRequestViewDTO(m)).toList();
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

		//member.delete(id);

		commutingRequest.setDeletedBy(member.getName());
	}

	@Transactional
	public List<CommuteRequestDTO> findCommute(Member member , String sd , String ed) {

		List<CommuteRequestDTO> commuteRequests = commutingRequestRepository.findCommute(member).stream()
				.filter(m -> LocalDate.parse(m.getRequestDt()).isAfter(LocalDate.parse(sd)))
				.filter(m -> LocalDate.parse(m.getRequestDt()).isBefore(LocalDate.parse(ed)))
				.filter(m -> m.getType().equals("O"))
				.filter(m -> m.getDeletedBy() == null)
				.map(m -> new CommuteRequestDTO(m)).collect(Collectors.toList());

		return commuteRequests;
	}
}
