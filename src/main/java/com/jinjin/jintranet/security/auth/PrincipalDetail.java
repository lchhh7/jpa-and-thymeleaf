package com.jinjin.jintranet.security.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.jinjin.jintranet.model.Member;

public class PrincipalDetail implements UserDetails , OAuth2User{
	private static final long serialVersionUID = 1L;
	
	@Autowired
	public BCryptPasswordEncoder encoder;
	
	private Member member;
	
	private Map<String, Object> attributes;
	
	public PrincipalDetail(Member member) {
		this.member = member;
	}
	
	public PrincipalDetail(Member member, Map<String,Object> attributes) {
		this.member = member;
		this.attributes = attributes;
	}
	
	public Member getMember() {
		return member;
	}
	
	public void setMember(Member member) {
		this.member = member;
	}
	
	@Override
	public String getPassword() {
		return member.getPassword();
	}

	@Override
	public String getUsername() {
		return member.getMemberId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override

	public boolean isEnabled() {
		if(member.getDeletedBy() != null) {
			return false;
		}
		return true;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collectors = new ArrayList<>();
		collectors.add(()->{ return "ROLE_"+member.getRole();});
		
		return collectors;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return null;
	}

	
}
