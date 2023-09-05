package com.jinjin.jintranet.schedule.web;

import com.jinjin.jintranet.common.DateUtils;
import com.jinjin.jintranet.common.VacationDaysUtils;
import com.jinjin.jintranet.handler.CustomException;
import com.jinjin.jintranet.handler.ErrorCode;
import com.jinjin.jintranet.holiday.service.HolidayService;
import com.jinjin.jintranet.member.dto.VacationDaysDTO;
import com.jinjin.jintranet.member.service.MemberService;
import com.jinjin.jintranet.model.Holiday;
import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.Schedule;
import com.jinjin.jintranet.schedule.dto.*;
import com.jinjin.jintranet.schedule.service.ScheduleService;
import com.jinjin.jintranet.security.auth.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class ScheduleController {

	private final MemberService memberService;

	private final ScheduleService scheduleService;

	private final HolidayService holidayService;

	private final VacationDaysUtils vacationDaysUtils;

	/**
	 * 일정관리 > 메인화면
	 */
	@GetMapping(value = "/schedule.do")
	public String main(Model model, @AuthenticationPrincipal PrincipalDetail principal) {
			Member member = principal.getMember();
			LocalDate now = LocalDate.now();

			int year = now.getYear();
			int month = now.getMonthOfYear();
			int date = now.getDayOfMonth();

			model.addAttribute("vacationDays",
					new VacationDaysDTO(vacationDaysUtils.getMemberVacationDays(member, year, month, date)));

			model.addAttribute("approves", memberService.findApproves());
			model.addAttribute("todaySchedules", scheduleService.todaySchedules());
			return "schedule/schedule";
	}

	/**
	 * 일정관리 > 일정 목록
	 */
	@GetMapping(value = "/schedule/search.do")
	public ResponseEntity<Map<String, Object>> findScheduleAll(@AuthenticationPrincipal PrincipalDetail principal,
			@RequestParam(value = "m", required = false, defaultValue = "") String m,
			@RequestParam(value = "sc", required = false, defaultValue = "''") String sc,
			@RequestParam(value = "fv", required = false, defaultValue = "''") String fv,
			@RequestParam(value = "hv", required = false, defaultValue = "''") String hv,
			@RequestParam(value = "ow", required = false, defaultValue = "''") String ow,
			@RequestParam(value = "sd") String sd, @RequestParam(value = "ed") String ed) throws Exception {

		Map<String, Object> map = new HashMap<>();

		List<String> typeList = List.of(sc , fv , hv , ow);

		Schedule schedule = Schedule.builder().strDt(DateUtils.toLocalDateTime(sd)).endDt(DateUtils.toLocalDateTime(ed)).build();

		if ("m".equals(m))
			schedule.setMember(principal.getMember());

			List<ScheduleSearchDTO> list = scheduleService.read(schedule , typeList);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
			sdf.parse(sd);
			List<Holiday> holidays = holidayService.findByMonth(DateUtils.toLocalDateTime(sd), DateUtils.toLocalDateTime(ed));
			map.put("list", list);
			map.put("holidays", holidays);

			return ResponseEntity.ok().body(map);
	}

	/**
	 * 일정관리 > 일정 등록
	 */
	@PostMapping(value = "/schedule.do")
	public ResponseEntity<String> write(@Valid @RequestBody ScheduleInsertDTO scheduleDTO, BindingResult bindingResult,
			@AuthenticationPrincipal PrincipalDetail principal) {
			if (bindingResult.hasErrors()) {
				return ResponseEntity.badRequest().body(bindingResult.getFieldErrors().get(0).getDefaultMessage());
			}

			if ("VA".equals(scheduleDTO.getType())) {
				String vacationType = scheduleDTO.getVacationType();
				if (!("1".equals(vacationType) || "2".equals(vacationType) || "3".equals(vacationType))) {
					throw new CustomException(ErrorCode.INVALID_SCHEDULE_PARAMETER);
				}

				if (scheduleDTO.getApproveId() == null) {
					throw new CustomException(ErrorCode.INVALID_APPROVER_PARAMETER);
				}
			}

			if ("OT".equals(scheduleDTO.getType())) {
				if (scheduleDTO.getApproveId() == null) {
					throw new CustomException(ErrorCode.INVALID_APPROVER_PARAMETER);
				}
			}

			if (!isValidDate(new LocalDate(scheduleDTO.getStrDt()), new LocalDate(scheduleDTO.getEndDt()))) {
				throw new CustomException(ErrorCode.INVALID_DATE_PARAMETER);
			}

			String typeCorrection = "VA".equals(scheduleDTO.getType())
					? ("1".equals(scheduleDTO.getVacationType()) ? "FV" : "HV")
					: scheduleDTO.getType();
			scheduleDTO.setType(typeCorrection);

			Member approve = (scheduleDTO.getApproveId() == null) ? null
					: memberService.findById(scheduleDTO.getApproveId());
			scheduleService.write(scheduleDTO, principal.getMember(), approve);

		return ResponseEntity.ok().body("일정을 정상적으로 등록했습니다.");
	}

	// 일정관리 > 일정 선택

	@GetMapping(value = "/schedule/{id}.do")
	public ResponseEntity<ScheduleViewDTO> findById(@PathVariable("id") Integer id) {
			Schedule schedule = scheduleService.findById(id);
		return ResponseEntity.ok().body(new ScheduleViewDTO(schedule));
	}

	// 일정관리 > 일정 수정
	@PutMapping(value = "/schedule/{id}.do")
	public ResponseEntity<String> edit(@PathVariable("id") Integer id,
			@Validated @RequestBody ScheduleUpdateDTO scheduleDTO, @AuthenticationPrincipal PrincipalDetail principal,
			BindingResult bindingResult) {
			if (bindingResult.hasErrors()) {
				return ResponseEntity.badRequest().body(bindingResult.getFieldErrors().get(0).getDefaultMessage());
			}

			if (!isValidDate(new LocalDate(scheduleDTO.getStrDt()), new LocalDate(scheduleDTO.getEndDt()))) {
				throw new CustomException(ErrorCode.INVALID_DATE_PARAMETER);
			}

			scheduleService.edit(id, scheduleDTO.DTOtoEntity(), principal);
		return ResponseEntity.ok().body("일정을 정상적으로 수정했습니다.");
	}
	// 일정관리 > 일정 취소요청

	@PutMapping(value = "/schedule/cancel/{id}.do")
	public ResponseEntity<String> cancel(@PathVariable("id") Integer id, @RequestBody ScheduleCancelDTO scheduleDTO,
			@AuthenticationPrincipal PrincipalDetail principal) {
			Schedule schedule = scheduleService.findById(id);
			if (schedule == null) {
				throw new CustomException(ErrorCode.INVALID_DATE_PARAMETER);
			}
			if (!"Y".equals(schedule.getStatus())) {
				throw new CustomException(ErrorCode.INVALID_CANCEL_PARAMETER);
			}

			LocalDateTime startDt = schedule.getStrDt();
			LocalDateTime now = LocalDateTime.now();
			if (now.isAfter(startDt)) {
				throw new CustomException(ErrorCode.INVALID_DELETE_PARAMETER);
			}

			scheduleService.cancel(id, scheduleDTO.DTOtoEntity(), principal);
			return ResponseEntity.ok().body("일정을 정상적으로 취소 요청했습니다.");
	}

	// 일정관리 > 일정 삭제
	@DeleteMapping(value = "/schedule/{id}.do")
	public ResponseEntity<String> delete(@PathVariable("id") Integer id,
			@AuthenticationPrincipal PrincipalDetail principal) {
			scheduleService.delete(id, principal);
		return ResponseEntity.ok().body("일정을 정상적으로 삭제했습니다.");
	}

	private boolean isValidDate(LocalDate strDt, LocalDate endDt) {
		if (strDt.isAfter(endDt))
			return false;

		return true;
	}

}
