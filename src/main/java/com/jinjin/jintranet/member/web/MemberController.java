package com.jinjin.jintranet.member.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

@Controller
public class MemberController {

    private MemberService memberService;

    private ScheduleService scheduleService;

    private CommutingService commutingService;

    private NoticeService noticeService;

    public MemberController(MemberService memberService, ScheduleService scheduleService,
                            CommutingService commutingService, NoticeService noticeService) {
        this.memberService = memberService;
        this.scheduleService = scheduleService;
        this.commutingService = commutingService;
        this.noticeService = noticeService;
    }

    @GetMapping("/login.do")
    public String loginPage() {
        return "login/index";
    }

    @GetMapping(value = "/main.do")
    public String main(Model model, HttpServletRequest request,
                       @PageableDefault(size=6, sort="id", direction = Sort.Direction.DESC) Pageable pageable,
                       @AuthenticationPrincipal PrincipalDetail principal) throws Exception {

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
            @RequestParam(value ="st", required = false , defaultValue ="") String st,
            @RequestParam(value ="y", required = false , defaultValue ="") String y ,
            @AuthenticationPrincipal PrincipalDetail principal
    ) throws Exception {
        try {
            List<ScheduleViewDTO> dtoList = new ArrayList<>();
            List<Schedule> mainSchedules = scheduleService.mainSchedules(principal.getMember(), y);

            for(Schedule s : mainSchedules) {
                dtoList.add(new ScheduleViewDTO(s));
            }


            return new ResponseEntity<>(dtoList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/join.do")
    public String joinPage() {
        return "joinPage";
    }
    @PostMapping("/auth/joinProc")
    public ResponseEntity<String> save(@Validated @RequestBody MemberSaveDTO memberDTO , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }

        memberService.write(memberDTO);
        return new ResponseEntity<String>("회원가입이 완료되었습니다.",HttpStatus.OK);
    }

    @PostMapping("/main/goToWorkButton.do")
    public ResponseEntity<String> commutingInsert(@RequestBody Commuting commuting , @AuthenticationPrincipal PrincipalDetail principal) {
        commutingService.write(commuting , principal.getMember());
        return new ResponseEntity<String>("",HttpStatus.OK);
    }

    @GetMapping("/member/edit.do")
    public String edit(Model model, HttpServletRequest request , @AuthenticationPrincipal PrincipalDetail principal) throws Exception {
        try {
            model.addAttribute("memberInfo" , new MemberSaveDTO(memberService.findById(principal.getMember().getId())));
            model.addAttribute("todaySchedules" , scheduleService.todaySchedules());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "member/member-edit";
    }

    @PutMapping("/member/edit.do")
    public ResponseEntity<String> edit(@Validated @RequestBody MemberEditDTO memberDTO,BindingResult bindingResult ,
                                       @AuthenticationPrincipal PrincipalDetail principal) throws Exception {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }

        memberService.edit(principal, memberDTO);

        return new ResponseEntity<String>("정상적으로 정보가 수정되었습니다.",HttpStatus.OK);
    }

    @GetMapping("/member/p/edit.do")
    public String pEdit(Model model, HttpServletRequest request,
                        @AuthenticationPrincipal PrincipalDetail principal) throws Exception {
        try {
            model.addAttribute("todaySchedules" , scheduleService.todaySchedules());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "member/password-edit";
    }

    @PutMapping("/member/p/edit.do")
    public ResponseEntity<String> pEdit(@Validated @RequestBody PasswordEditDTO passwordDTO,BindingResult bindingResult ,
                                        @AuthenticationPrincipal PrincipalDetail principal) throws Exception {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }

        String msg = memberService.passwordEdit(principal.getMember().getId(), passwordDTO);

        return new ResponseEntity<String>(msg,HttpStatus.OK);
    }
}