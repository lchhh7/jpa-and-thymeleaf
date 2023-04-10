package com.jinjin.jintranet.member.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jinjin.jintranet.member.dto.EditByAdminDTO;
import com.jinjin.jintranet.member.dto.MemberEditDTO;
import com.jinjin.jintranet.member.dto.MemberInterface;
import com.jinjin.jintranet.member.dto.MemberSaveDTO;
import com.jinjin.jintranet.member.dto.MemberSearchDTO;
import com.jinjin.jintranet.member.dto.MemberViewDTO;
import com.jinjin.jintranet.member.dto.PasswordEditDTO;
import com.jinjin.jintranet.member.repository.MemberDslRepository;
import com.jinjin.jintranet.member.repository.MemberRepository;
import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.RoleType;
import com.jinjin.jintranet.security.auth.PrincipalDetail;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final MemberDslRepository memberDslRepository;
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Transactional
	public int write(MemberSaveDTO memberDTO) {
		Member member = memberDTO.DtoToEntity();
		String rawPassword = member.getPassword();
		String encPassword = encoder.encode(rawPassword);
		member.setPassword(encPassword);
		member.setRole(RoleType.USER);
		member.setCreatedBy(member.getName());
		try {
			memberRepository.save(member);
			return 1;
		} catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	@Transactional
	public void edit(PrincipalDetail principal, MemberEditDTO requestMember) {
		Member member = memberRepository.findById(principal.getMember().getId())
				.orElseThrow(() -> {
					return new IllegalArgumentException("사용자를 찾을 수 없습니다.");
				});
		
		member.setName(requestMember.getName());
		member.setPosition(requestMember.getPosition());
		member.setDepartment(requestMember.getDepartment());
		member.setPhoneNo(requestMember.getPhoneNo());
		member.setMobileNo(requestMember.getMobileNo());
		member.setUseColor(requestMember.getUseColor());
		member.setModifiedBy(requestMember.getName());
		
		principal.setMember(member);
		
	}
	
	@Transactional
	public void adminEdit(int id, EditByAdminDTO requestMember,PrincipalDetail principal) {
		Member member = memberRepository.findById(id)
				.orElseThrow(() -> {
					return new IllegalArgumentException("사용자를 찾을 수 없습니다.");
				});
		
		member.setName(requestMember.getName());
		member.setPosition(requestMember.getPosition());
		member.setDepartment(requestMember.getDepartment());
		member.setPhoneNo(requestMember.getPhoneNo());
		member.setMobileNo(requestMember.getMobileNo());
		member.setUseColor(requestMember.getUseColor());
		member.setRole(requestMember.getRole());
		member.setModifiedBy(principal.getMember().getName());
	}
	
	@Transactional
	public String passwordEdit(int id, PasswordEditDTO requestPassword) {
		Member member = memberRepository.findById(id)
				.orElseThrow(() -> {
					return new IllegalArgumentException("사용자를 찾을 수 없습니다.");
				});
		
		String rawPassword = requestPassword.getPassword(); //원래 비밀번호 
		String rawNewPassword = requestPassword.getNewPassword();
		String encPassword = encoder.encode(rawNewPassword); //수정된 비밀번호
		
		if(encoder.matches(rawPassword, member.getPassword())) {
			member.setPassword(encPassword);
			return "정상적으로 비밀번호를 수정했습니다";
		}else if(!requestPassword.getNewPassword().equals(requestPassword.getNewPassword2())) {
			return "새로운 비밀번호가 일치하지 않습니다.";
		}else { 
			return "현재 비밀번호가 맞지 않습니다.";
		}
	}
	
	@Transactional
	public void delete(int id,PrincipalDetail principal) {
		Member member = memberRepository.findById(id)
				.orElseThrow(() -> {
					return new IllegalArgumentException("사용자를 찾을 수 없습니다.");
				});
		
		member.setDeletedBy(principal.getMember().getName());
		
		if(member.getId() == principal.getMember().getId()) {
			SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
		}
	}
	
	
	@Transactional
	public List<MemberInterface> findApproves() {
		return memberRepository.approves();
	}
	
	@Transactional
	public Member findById(Integer id) {
		return memberRepository.findById(id).orElseThrow(() ->{
			return new IllegalArgumentException("승인자를 조회하는중 오류가 발생했습니다"); 
		});
	}
	
	@Transactional	
	public Member findOAuthById(String memberId) {	
		return memberRepository.findOAuthById(memberId);	
	}
	
	
	@Transactional
	public List<MemberViewDTO> findAll() {
		List<MemberViewDTO> list = memberRepository.findAll().stream().filter(m-> m.getDeletedBy() == null).map(m -> new MemberViewDTO(m)).collect(Collectors.toList());
		return list;
	};
		
	@Transactional
	public Page<MemberSearchDTO> findMembers(Member member, Pageable pageable, String n, String p, String d) {
		return memberDslRepository.findMembers(member, pageable, n, p, d);
	};
}
