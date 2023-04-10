package com.jinjin.jintranet.commuting.web;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.jinjin.jintranet.model.CommutingRequest;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
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
@RequiredArgsConstructor
public class CommutingController {
	
	private final CommutingService commutingService;
	
	private final ScheduleService scheduleService;
	
	private final MemberService memberService;
	
	private final HolidayService holidayService;
	
	private final CommutingRequestService commutingRequestService;

	@GetMapping("/commuting.do")
	public String commuting(Model model , HttpServletRequest request, @AuthenticationPrincipal PrincipalDetail principal) {
		model.addAttribute("todaySchedules" , scheduleService.todaySchedules());
		model.addAttribute("approves", memberService.findApproves());
		model.addAttribute("principal", principal);
		model.addAttribute("yearList" , commutingRequestService.yearList(principal.getMember()));
		model.addAllAttributes(commutingService.getWorkTime(principal.getMember()));
		return "commuting/commuting";
	}

	public Integer currentMonth(String sd) {
		LocalDate ld = LocalDate.parse(sd);
		int month = ld.getDayOfMonth() > 1 ? ld.getMonthValue() +1 : ld.getMonthValue();
		return month;
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
			int month = currentMonth(sd);
            
            Schedule schedule = Schedule.builder()
            		.member(principal.getMember()).type("FV,HV").status("Y")
            		.strDt(DateUtils.toLocalDateTime(sd)).endDt(DateUtils.toLocalDateTime(ed)).build();
        	
            List<Holiday> holidays = holidayService.findByMonth(DateUtils.toLocalDateTime(sd), DateUtils.toLocalDateTime(ed));
            List<ScheduleSearchDTO> schedules = scheduleService.read(schedule);
            List<CommutingsInterface> commute = commutingService.findAll(principal.getMember() ,sd ,ed);

			List<CommuteRequestDTO> commuteRequests = principal.getMember().getCommutingRequests().stream()
					.filter(m -> LocalDate.parse(m.getRequestDt()).isAfter(LocalDate.parse(sd)))
					.filter(m -> LocalDate.parse(m.getRequestDt()).isBefore(LocalDate.parse(ed)))
					.filter(m -> m.getDeletedBy() == null)
					.map(m -> new CommuteRequestDTO(m)).collect(Collectors.toList());
			CommuteRequestDTO nearList = null;
			if(commuteRequests.size() !=0) {
				 nearList = commuteRequests.stream().sorted(Comparator.comparing(CommuteRequestDTO::getRequestDt).reversed()).toList().get(0);
			}
           /* List<CommuteRequestDTO> commuteRequests = commutingRequestService.findAll(principal.getMember() , sd, ed)
            		.stream().map(m -> new CommuteRequestDTO(m)).collect(Collectors.toList());

			List<CommutingRequest> list = commutingRequestService.commutingRequestSearching(principal.getMember() , null , null);
			CommutingRequest nearList = null;
			if(list.size() !=0) {
				nearList = list.stream().sorted(Comparator.comparing(CommutingRequest::getRequestDt).reversed()).toList().get(0);
			}*/

            map.put("holidays" , holidays);
            map.put("schedules", schedules);
            map.put("commute" , commute.stream().filter(c -> c.getCnt() ==1).collect(Collectors.toList()));
            map.put("commuteRequests", commuteRequests);
			map.put("overtimes", overtimes(commute , commuteRequests , month));
			map.put("nearList" , nearList);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

	public String overtimes(List<CommutingsInterface> commute , List<CommuteRequestDTO> commuteRequests , int month) {
		int hour = 0; int minute =0;
		List<Integer> overtimeDates = commuteRequests.stream()
				.filter(m -> m.getType().equals("O"))
				.filter(m -> m.getStatus().equals("Y"))
				.filter(m -> LocalDate.parse(m.getRequestDt()).getMonthValue() == month)
				.map(d -> LocalDate.parse(d.getRequestDt() , DateTimeFormatter.ISO_DATE).getDayOfMonth()).sorted().toList();

		for(Integer i : overtimeDates) {
			List<LocalDateTime> dayOfTime
					= commute.stream()
					.filter(c -> c.getCnt() ==1)
					.filter(d -> d.getCommutingTm().getDayOfMonth() == i ).map(m -> m.getCommutingTm()).toList();

			if(dayOfTime.size() < 2) continue;

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
