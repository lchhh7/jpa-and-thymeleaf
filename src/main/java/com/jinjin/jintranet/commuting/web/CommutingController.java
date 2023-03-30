package com.jinjin.jintranet.commuting.web;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.jinjin.jintranet.common.DateUtils;
import com.jinjin.jintranet.commuting.dto.CommuteRequestDTO;
import com.jinjin.jintranet.commuting.dto.CommuteRequestInsertDTO;
import com.jinjin.jintranet.commuting.dto.CommutingSelectDTO;
import com.jinjin.jintranet.commuting.dto.CommutingsInterface;
import com.jinjin.jintranet.commuting.service.CommutingRequestService;
import com.jinjin.jintranet.commuting.service.CommutingService;
import com.jinjin.jintranet.holiday.service.HolidayService;
import com.jinjin.jintranet.member.service.MemberService;
import com.jinjin.jintranet.model.Holiday;
import com.jinjin.jintranet.model.Schedule;
import com.jinjin.jintranet.schedule.dto.ScheduleSearchDTO;
import com.jinjin.jintranet.schedule.service.ScheduleService;
import com.jinjin.jintranet.security.auth.PrincipalDetail;

@Controller
public class CommutingController {
	
	private CommutingService commutingService;
	
	private ScheduleService scheduleService;
	
	private MemberService memberService;
	
	private HolidayService holidayService;
	
	private CommutingRequestService commutingRequestService;
	
	
	public CommutingController(CommutingService commutingService, ScheduleService scheduleService,
			MemberService memberService, HolidayService holidayService, CommutingRequestService commutingRequestService) {
		this.commutingService = commutingService;
		this.scheduleService = scheduleService;
		this.memberService = memberService;
		this.holidayService = holidayService;
		this.commutingRequestService = commutingRequestService;
	}

	@GetMapping("/commuting.do")
	public String commuting(Model model , HttpServletRequest request, @AuthenticationPrincipal PrincipalDetail principal) {
		model.addAttribute("todaySchedules" , scheduleService.todaySchedules());
		model.addAttribute("approves", memberService.findApproves());
		model.addAttribute("principal", principal);
		return "commuting/commuting";
	}
	
	/**
     * 근태관리 > 목록 조회
     */
    @GetMapping(value = "/commuting/search.do")
    public ResponseEntity<Map<String, Object>> findScheduleAll(
            @RequestParam(value = "sd") String sd ,
            @RequestParam(value = "ed") String ed, @AuthenticationPrincipal PrincipalDetail principal) throws Exception {
        Map<String, Object> map = new HashMap<>();
        try {
            
            
            Schedule schedule = Schedule.builder()
            		.member(principal.getMember()).type("FV,HV").status("Y")
            		.strDt(DateUtils.toLocalDateTime(sd)).endDt(DateUtils.toLocalDateTime(ed)).build();
        	
            List<Holiday> holidays = holidayService.findByMonth(DateUtils.toLocalDateTime(sd), DateUtils.toLocalDateTime(ed));
            List<ScheduleSearchDTO> schedules = scheduleService.read(schedule);
            List<CommutingsInterface> commute = commutingService.findAll(principal.getMember() ,sd ,ed);
            List<CommuteRequestDTO> commuteRequests = commutingRequestService.findAll(principal.getMember() , sd, ed)
            		.stream().map(m -> new CommuteRequestDTO(m)).collect(Collectors.toList());
            
            map.put("holidays" , holidays);
            map.put("schedules", schedules);
            map.put("commute" , commute.stream().filter(c -> c.getCnt() ==1).collect(Collectors.toList()));
            map.put("commuteRequests", commuteRequests);
			map.put("overtimes", overtimes(commute));
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

	public String overtimes(List<CommutingsInterface> commute) {
		int hour = 0; int minute =0;
		List<Integer> overtimeDates = commute.stream().filter(m -> m.getAttendYn().equals("O"))
				.map(d -> d.getCommutingTm().getDayOfMonth()).sorted().toList();

		for(Integer i : overtimeDates) {
			List<LocalDateTime> dayOfTime
					= commute.stream()
					.filter(c -> c.getCnt() ==1)
					.filter(c -> !c.getAttendYn().equals("O"))
					.filter(d -> d.getCommutingTm().getDayOfMonth() == i).map(m -> m.getCommutingTm()).toList();

			if(dayOfTime.size() < 2) break;

			Duration diff = Duration.between(dayOfTime.get(0).toLocalTime(), dayOfTime.get(1).toLocalTime());
			if(diff.toHours() > 10)  {
				minute += diff.toMinutes() - 600;
			}
		}
		hour = (minute/60);
		minute = minute%60;

		return hour+ "시간" + minute +"분";
	};

    /**
     * 일정관리 > 일정 선택
     */
    @GetMapping(value = "/commuting/{id}.do")
    public ResponseEntity<CommutingSelectDTO> findById(@PathVariable("id") Integer id) throws Exception {

        try {
        	CommutingSelectDTO commutingDTO = new CommutingSelectDTO(commutingService.findById(id));
            return new ResponseEntity<>(commutingDTO, HttpStatus.OK);
        } catch (Exception e) {
        	e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    
  //근태 내용 수정
   /* @PutMapping(value = "/commuting/{id}.do")
    public ResponseEntity<String> editCommute(@PathVariable("id") Integer id,@Validated @RequestBody CommuteUpdateDTO commuteDTO, BindingResult bindingResult,
    		@AuthenticationPrincipal PrincipalDetail principal) throws Exception {
    	try {
    		if (bindingResult.hasErrors()) {
             	return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
             }
    		
    		//commutingService.edit(id, commuteDTO);
    		return new ResponseEntity<>("근태를 정상적으로 수정했습니다.", HttpStatus.OK);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>("근태수정 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
    	}
    }*/
    
    @PostMapping(value = "/commuting/writeCommute.do")
    public ResponseEntity<String> edit(@Validated @RequestBody CommuteRequestInsertDTO dto, BindingResult bindingResult,
    		@AuthenticationPrincipal PrincipalDetail principal) throws Exception {
    	try {
    		if (bindingResult.hasErrors()) {
             	return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
             }
    		
    		commutingService.commuteEdit(dto , principal.getMember());
    		return new ResponseEntity<>("근태를 정상적으로 수정했습니다.", HttpStatus.OK);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>("근태수정 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
    	}
    }
}
