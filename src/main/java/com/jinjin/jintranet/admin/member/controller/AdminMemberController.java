package com.jinjin.jintranet.admin.member.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.jinjin.jintranet.common.PageUtils;
import com.jinjin.jintranet.member.dto.EditByAdminDTO;
import com.jinjin.jintranet.member.dto.MemberSearchDTO;
import com.jinjin.jintranet.member.dto.MemberShowDTO;
import com.jinjin.jintranet.member.service.MemberService;
import com.jinjin.jintranet.schedule.service.ScheduleService;
import com.jinjin.jintranet.security.auth.PrincipalDetail;

@Controller
public class AdminMemberController {
	
	private MemberService memberService;

	private ScheduleService scheduleService;
	
	public AdminMemberController(MemberService memberService, ScheduleService scheduleService) {
		this.memberService = memberService;
		this.scheduleService = scheduleService;
	}

	/**
     * 사용자관리 > 목록 페이지로 이동
     */
    @GetMapping(value = "/admin/member.do")
    public String main(Model model, HttpServletRequest request,
    		@AuthenticationPrincipal PrincipalDetail principal) throws Exception {
        try {
        	model.addAttribute("todaySchedules" , scheduleService.todaySchedules());
        } catch (Exception e) {
        	e.printStackTrace();
        }

        return "admin-member/admin-member";
    }
    
    /**
     * 사용자관리 > 목록 조회
     */
    @GetMapping(value = "/admin/member/search.do")
    public ResponseEntity<Map<String, Object>> findAll(
    		@AuthenticationPrincipal PrincipalDetail principal ,
            @RequestParam(value = "n", required = false, defaultValue = "") String n,
            @RequestParam(value = "po", required = false, defaultValue = "") String p,
            @RequestParam(value = "d", required = false, defaultValue = "") String d,
            @PageableDefault(size=10, sort="id", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) throws Exception {

    	Map<String, Object> map = new HashMap<>();
        try {
        	Page<MemberSearchDTO> list = memberService.findMembers(principal.getMember() , pageable, n, p, d);
       
            String page = PageUtils.page(list, "", request);
            
            map.put("list", list);
            map.put("page", page);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
        	e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    
    /**
     * 사용자관리 > 사용자 조회
     */
    @GetMapping(value = "/admin/member/{id}.do")
    public ResponseEntity<MemberShowDTO> findById(@PathVariable("id") int id) throws Exception {
        try {
        	MemberShowDTO member = new MemberShowDTO(memberService.findById(id));

            return new ResponseEntity<>(member, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    /**
     * 사용자관리 > 사용자 수정
     */
    @PutMapping(value = "/admin/member/{id}.do")
    public ResponseEntity<String> edit(@PathVariable("id") int id,@Validated @RequestBody EditByAdminDTO memberDTO ,
    		@AuthenticationPrincipal PrincipalDetail principal, BindingResult bindingResult) throws Exception {
        try {
        	if (bindingResult.hasErrors()) {
             	return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
             }
            memberService.adminEdit(id , memberDTO ,principal);
            return new ResponseEntity<>("사용자 정보를 정상적으로 수정했습니다.", HttpStatus.OK);
        } catch (Exception e) {
        	e.printStackTrace();
            return new ResponseEntity<>("사용자 정보 수정 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    /**
     * 사용자관리 > 사용자 삭제
     */
    @DeleteMapping(value = "/admin/member/{id}.do")
    public ResponseEntity<String> delete(@PathVariable("id") int id , @AuthenticationPrincipal PrincipalDetail principal) throws Exception {
        try {
            memberService.delete(id, principal);
            return new ResponseEntity<>("사용자 정보를 정상적으로 삭제했습니다.", HttpStatus.OK);
        } catch (Exception e) {
        	e.printStackTrace();
            return new ResponseEntity<>("사용자 정보를 삭제하는 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }
}
