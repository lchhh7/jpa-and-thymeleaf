package com.jinjin.jintranet.admin.schedule.controller;

import com.jinjin.jintranet.common.PageUtils;
import com.jinjin.jintranet.member.dto.MemberCommuteDTO;
import com.jinjin.jintranet.member.service.MemberService;
import com.jinjin.jintranet.model.Schedule;
import com.jinjin.jintranet.schedule.service.ScheduleService;
import com.jinjin.jintranet.security.auth.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AdminScheduleController {
	
	private final MemberService memberService;
	
	private final ScheduleService scheduleService;
    

	/**
     * 일정신청관리(관) > 목록 페이지 이동
     */
    @GetMapping(value = "/admin/schedule.do")
    public String main(Model model) {
        model.addAttribute("members", memberService.findAll());
        model.addAttribute("todaySchedules" , scheduleService.todaySchedules());
        return "admin-schedule/admin-schedule";
    }

    /**
     * 일정신청관리(관) > 목록 조회
     */
    @GetMapping(value = "/admin/schedule/search.do")
    public ResponseEntity<Map<String, Object>> findSchedule(
    		@AuthenticationPrincipal PrincipalDetail principal ,
            @RequestParam(value = "m", required = false) Integer m,
            @RequestParam(value = "r", required = false, defaultValue = "''") String r,
            @RequestParam(value = "y", required = false, defaultValue = "''") String y,
            @RequestParam(value = "n", required = false, defaultValue = "''") String n,
            @PageableDefault(size=10, sort="id", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {

            List<String> statusList = new ArrayList<>();
            if(!"".equals(r)) {
                statusList.add(r);
                statusList.add("C");
            }
    	
            statusList.add(y); statusList.add(n);
            Map<String, Object> map = new HashMap<>();
            Page<Schedule> approvesList = scheduleService.approvesList(principal.getMember(), m , statusList , pageable);
            String page = PageUtils.page(approvesList, "schedules" , request);
            
            map.put("list" , approvesList);
            map.put("page" , page);
            return ResponseEntity.ok().body(map);
    }
    

    /**
     * 일정신청관리(관) > 신청내역 조회
     */
    @GetMapping(value = "/admin/schedule/{id}.do")
    public ResponseEntity<Schedule> findById(@PathVariable("id") int id) {
        Schedule vo = scheduleService.findById(id);
        return ResponseEntity.ok().body(vo);
    }
    

    /**
     * 일정신청관리(관) > 신청내역 처리
     * status
     * R : 대기
     * Y : 승인
     * N : 비승인
     * C : 취소요청
     * D : 취소요청 승인
     * 취소요청 비승인 = Y
     */
    @PutMapping(value = "/admin/schedule/{id}.do")
    public ResponseEntity<String> approve(
            @PathVariable("id") int id,
            @RequestBody Schedule schedule,
            @AuthenticationPrincipal PrincipalDetail principal) {

            scheduleService.approves(id, schedule , principal.getMember());
            return ResponseEntity.ok().body("정상적으로 처리되었습니다.");
    }
    

    /**
     * 일정신청관리(관) > 휴가일수조회
     */
    @GetMapping(value = "/admin/schedule/vacationDays.do")
    public ResponseEntity<List<MemberCommuteDTO>> vacationDays() {
        return ResponseEntity.ok().body(scheduleService.vacationDays());
    }
}
