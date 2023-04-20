package com.jinjin.jintranet.commuting.web;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommutingController {

	private final CommutingService commutingService;
	
	private final ScheduleService scheduleService;
	
	private final MemberService memberService;
	
	private final HolidayService holidayService;
	
	private final CommutingRequestService commutingRequestService;

	@GetMapping("/commuting.do")
	public String commuting(Model model , HttpServletRequest request, @AuthenticationPrincipal PrincipalDetail principal) throws Exception{
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

			int month = currentMonth(sd);
            
            Schedule schedule = Schedule.builder()
            		.member(principal.getMember()).type("FV,HV").status("Y")
            		.strDt(DateUtils.toLocalDateTime(sd)).endDt(DateUtils.toLocalDateTime(ed)).build();
        	
            List<Holiday> holidays = holidayService.findByMonth(DateUtils.toLocalDateTime(sd), DateUtils.toLocalDateTime(ed));
            List<ScheduleSearchDTO> schedules = scheduleService.read(schedule);
            List<CommutingsInterface> commute = commutingService.findAll(principal.getMember() ,sd ,ed);


		List<CommuteRequestDTO> commuteRequests = commutingRequestService.findCommute(principal.getMember()).stream()
					.filter(m -> LocalDate.parse(m.getRequestDt()).isAfter(LocalDate.parse(sd)))
					.filter(m -> LocalDate.parse(m.getRequestDt()).isBefore(LocalDate.parse(ed)))
					.filter(m -> m.getType().equals("O"))
					.filter(m -> m.getDeletedBy() == null)
					.map(m -> new CommuteRequestDTO(m)).collect(Collectors.toList());
			CommuteRequestDTO nearList = null;
			List<CommuteRequestDTO> overtimes = null;
			if(commuteRequests.size() !=0) {
				 nearList = commuteRequests.stream().sorted(Comparator.comparing(CommuteRequestDTO::getRequestDt).reversed()).toList().get(0);
				 overtimes = commutingService.overtimes(commute , commuteRequests , month);
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
			map.put("overtimes", overtimes);
			map.put("nearList" , nearList);

            return new ResponseEntity<>(map, HttpStatus.OK);
    }



    /**
     * 일정관리 > 일정 선택
     */
    @GetMapping(value = "/commuting/{id}.do")
    public ResponseEntity<CommutingSelectDTO> findById(@PathVariable("id") Integer id) throws Exception {
		CommutingSelectDTO commutingDTO = new CommutingSelectDTO(commutingService.findById(id));
		return new ResponseEntity<>(commutingDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/commuting/writeCommute.do")
    public ResponseEntity<String> edit(@Validated @RequestBody CommuteRequestInsertDTO dto, BindingResult bindingResult,
    		@AuthenticationPrincipal PrincipalDetail principal) throws Exception {
    		if (bindingResult.hasErrors()) {
             	return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
             }
    		
    		commutingService.commuteEdit(dto , principal.getMember());
    		return new ResponseEntity<>("근태를 정상적으로 수정했습니다.", HttpStatus.OK);
    }
}
