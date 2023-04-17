package com.jinjin.jintranet.commuting.web;

import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.RoleType;
import com.jinjin.jintranet.security.auth.PrincipalDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        PrincipalDetail principal = new PrincipalDetail(Member.builder().memberId(customUser.memberId()).name(customUser.name()).role(RoleType.ADMIN) .build());
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}