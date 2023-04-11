package com.jinjin.jintranet.commuting.web;


import com.jinjin.jintranet.commuting.dto.AdminCommuteRequestViewDTO;
import com.jinjin.jintranet.commuting.dto.CommuteRequestViewDTO;
import com.jinjin.jintranet.commuting.service.CommutingRequestService;
import com.jinjin.jintranet.commuting.service.CommutingService;
import com.jinjin.jintranet.holiday.service.HolidayService;
import com.jinjin.jintranet.member.service.MemberService;
import com.jinjin.jintranet.model.CommutingRequest;

import com.jinjin.jintranet.schedule.service.ScheduleService;
import com.jinjin.jintranet.security.auth.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import javax.servlet.jsp.tagext.TryCatchFinally;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class CommutingRequestController {

	private final CommutingRequestService commutingRequestService;

	@GetMapping("/commuting/searching.do")
	public ResponseEntity<List<CommuteRequestViewDTO>> searching (
			@RequestParam(value ="st", required = false , defaultValue = "") String st,
			@RequestParam(value ="y", required = false , defaultValue ="") String y,
			@AuthenticationPrincipal PrincipalDetail principal) throws Exception {
		List<CommuteRequestViewDTO> list = commutingRequestService.commutingRequestSearching(principal.getMember() , st , y)
				.stream().map(m -> new CommuteRequestViewDTO(m)).toList();
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@GetMapping("/commuting/request/{id}.do")
	public ResponseEntity<AdminCommuteRequestViewDTO> requesting(
			@PathVariable("id") int id) throws Exception{
		AdminCommuteRequestViewDTO dto = commutingRequestService.findById(id);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@PutMapping(value="/commuting/editRequest/{id}.do")
	public ResponseEntity<String> editRequest(
			@PathVariable("id") int id ,
			@RequestBody AdminCommuteRequestViewDTO dto) throws Exception{
			commutingRequestService.EditRequest(id,dto);
			return new ResponseEntity<>("정상적으로 처리되었습니다.", HttpStatus.OK);
	}

	@DeleteMapping(value="/commuting/deleteRequest/{id}.do")
	public ResponseEntity<String> deleteRequest(
			@PathVariable("id") int id ,
			@AuthenticationPrincipal PrincipalDetail principal) throws Exception{
			commutingRequestService.DeleteRequest(id , principal.getMember());
			return new ResponseEntity<>("정상적으로 처리되었습니다.", HttpStatus.OK);
	}
}
