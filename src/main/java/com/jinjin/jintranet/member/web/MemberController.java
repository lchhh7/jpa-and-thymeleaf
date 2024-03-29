package com.jinjin.jintranet.member.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.jinjin.jintranet.commuting.service.CommutingService;
import com.jinjin.jintranet.member.dto.MemberEditDTO;
import com.jinjin.jintranet.member.dto.MemberSaveDTO;
import com.jinjin.jintranet.member.dto.PasswordEditDTO;
import com.jinjin.jintranet.member.service.MemberService;
import com.jinjin.jintranet.model.Commuting;
import com.jinjin.jintranet.model.Schedule;
import com.jinjin.jintranet.notice.dto.NoticeSearchDTO;
import com.jinjin.jintranet.notice.service.NoticeService;
import com.jinjin.jintranet.schedule.dto.ScheduleViewDTO;
import com.jinjin.jintranet.schedule.service.ScheduleService;
import com.jinjin.jintranet.security.auth.PrincipalDetail;
import org.springframework.web.servlet.NoHandlerFoundException;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    private final ScheduleService scheduleService;

    private final CommutingService commutingService;

    private final NoticeService noticeService;

    @GetMapping("/login.do")
    public String loginPage() {
        return "login/index";
    }

    @GetMapping(value = "/main.do")
    public String main(Model model, HttpServletRequest request,
                       @PageableDefault(size=6, sort="id", direction = Sort.Direction.DESC) Pageable pageable,
                       @AuthenticationPrincipal PrincipalDetail principal) {

        List<String> yearList = scheduleService.yearList(principal.getMember());
        List<Schedule> vacation = scheduleService.findByMemberId(principal.getMember());
        List<NoticeSearchDTO> notice = noticeService.findNotices(pageable , null, null).getContent();
        Integer cnt = scheduleService.cnt(principal.getMember());

        model.addAttribute("noticeList" , notice);
        model.addAttribute("vacation" , vacation);
        model.addAttribute("cnt" , cnt);
        model.addAttribute("yearList" , yearList);
        model.addAttribute("todaySchedules" , scheduleService.todaySchedules());
        model.addAllAttributes(commutingService.getWorkTime(principal.getMember()));
        return "main/index";
    }

    @GetMapping(value = "/main/searching.do")
    public ResponseEntity<List<ScheduleViewDTO>> searching(
            @RequestParam(value ="y", required = false , defaultValue ="") String y ,
            @AuthenticationPrincipal PrincipalDetail principal) {
            List<ScheduleViewDTO> dtoList = new ArrayList<>();
            List<Schedule> mainSchedules = scheduleService.mainSchedules(principal.getMember(), y);

            for(Schedule s : mainSchedules) {
                dtoList.add(new ScheduleViewDTO(s));
            }
            return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/join.do")
    public String joinPage() {
        return "joinPage";
    }
    @PostMapping("/auth/joinProc")
    public ResponseEntity<String> save(@Validated @RequestBody MemberSaveDTO memberDTO , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors().get(0).getDefaultMessage());
        }

        memberService.write(memberDTO);
        return ResponseEntity.ok().body("회원가입이 완료되었습니다.");
    }

    @PostMapping("/main/goToWorkButton.do")
    public ResponseEntity<String> commutingInsert(@RequestBody Commuting commuting , @AuthenticationPrincipal PrincipalDetail principal) {
        commutingService.write(commuting , principal.getMember());
        return ResponseEntity.ok().body("");
    }

    @GetMapping("/member/edit.do")
    public String edit(Model model , @AuthenticationPrincipal PrincipalDetail principal) {

        model.addAttribute("memberInfo" , new MemberSaveDTO(memberService.findById(principal.getMember().getId())));
        model.addAttribute("todaySchedules" , scheduleService.todaySchedules());
        return "member/member-edit";
    }

    @PutMapping("/member/edit.do")
    public ResponseEntity<String> edit(@Validated @RequestBody MemberEditDTO memberDTO,BindingResult bindingResult ,
                                       @AuthenticationPrincipal PrincipalDetail principal) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors().get(0).getDefaultMessage());
        }
        memberService.edit(principal, memberDTO);
        return ResponseEntity.ok().body("정상적으로 정보가 수정되었습니다.");
    }

    @GetMapping("/member/p/edit.do")
    public String pEdit(Model model) {
        model.addAttribute("todaySchedules" , scheduleService.todaySchedules());
        return "member/password-edit";
    }

    @PutMapping("/member/p/edit.do")
    public ResponseEntity<String> pEdit(@Validated @RequestBody PasswordEditDTO passwordDTO,BindingResult bindingResult ,
                                        @AuthenticationPrincipal PrincipalDetail principal) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors().get(0).getDefaultMessage());
        }

        return ResponseEntity.ok().body(memberService.passwordEdit(principal.getMember().getId(), passwordDTO));
    }
}
