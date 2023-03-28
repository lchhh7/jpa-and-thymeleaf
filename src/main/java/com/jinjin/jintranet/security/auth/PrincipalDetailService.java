package com.jinjin.jintranet.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jinjin.jintranet.member.repository.MemberRepository;
import com.jinjin.jintranet.model.Member;

@Service
public class PrincipalDetailService implements UserDetailsService{
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
		Member principal = memberRepository.findByMemberId(memberId)
				.orElseThrow(() -> {
					return new UsernameNotFoundException("해당 사용자를 찾을수 없습니다.");
				});
		return new PrincipalDetail(principal);
	}
}
