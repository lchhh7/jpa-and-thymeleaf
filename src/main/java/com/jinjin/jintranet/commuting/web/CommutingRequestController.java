package com.jinjin.jintranet.commuting.web;


import com.jinjin.jintranet.commuting.service.CommutingRequestService;
import com.jinjin.jintranet.commuting.service.CommutingService;
import com.jinjin.jintranet.holiday.service.HolidayService;
import com.jinjin.jintranet.member.service.MemberService;
import com.jinjin.jintranet.model.CommutingRequest;

import com.jinjin.jintranet.schedule.service.ScheduleService;
import com.jinjin.jintranet.security.auth.PrincipalDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;


@Controller
public class CommutingRequestController {

	private CommutingService commutingService;

	private ScheduleService scheduleService;

	private MemberService memberService;

	private HolidayService holidayService;

	private CommutingRequestService commutingRequestService;


	public CommutingRequestController(CommutingService commutingService, ScheduleService scheduleService,
                                      MemberService memberService, HolidayService holidayService, CommutingRequestService commutingRequestService) {
		this.commutingService = commutingService;
		this.scheduleService = scheduleService;
		this.memberService = memberService;
		this.holidayService = holidayService;
		this.commutingRequestService = commutingRequestService;
	}

	@GetMapping("/commuting/searching.do")
	public ResponseEntity<List<CommutingRequest>> searching(
			@RequestParam(value ="st", required = false , defaultValue = "") String st,
			@RequestParam(value ="y", required = false , defaultValue ="") String y,
			@AuthenticationPrincipal PrincipalDetail principal) {
		List<CommutingRequest> list = commutingRequestService.commutingRequestSearching(principal.getMember() , st , y);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	

}
