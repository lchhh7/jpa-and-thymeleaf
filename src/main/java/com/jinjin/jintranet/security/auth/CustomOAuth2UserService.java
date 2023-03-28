package com.jinjin.jintranet.security.auth;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.jinjin.jintranet.member.repository.MemberRepository;
import com.jinjin.jintranet.model.DepartmentType;
import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.PositionType;
import com.jinjin.jintranet.model.RoleType;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService  {
	
	private MemberRepository memberRepository;
	
	 public CustomOAuth2UserService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	@Override
	    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
	        OAuth2User oAuth2User = super.loadUser(userRequest);
	        Map<String, Object> attributes = oAuth2User.getAttributes();
	        Map<String ,Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
			Map<String ,Object> properties = (Map<String, Object>) attributes.get("properties");
			
	        String memberId = kakao_account.get("email").toString();
	        String name = properties.get("nickname").toString();
	        
	        Member memberEntity = memberRepository.findByMemberId(memberId).orElse(null);
	        
	        if(memberEntity == null) {
	        	memberEntity = Member.builder()
	        			.memberId(memberId)
	        			.name(name)
	        			.department(DepartmentType.시스템개발부)
	        			.mobileNo("010-0000-0000")
	        			.phoneNo("000")
	        			.position(PositionType.사원)
	        			.useColor("#A3FFD1")
	        			.role(RoleType.USER)
	        			.build();
	        	
	        	memberRepository.save(memberEntity);
	        }
	        
	        return new PrincipalDetail(memberEntity);
	    }
}