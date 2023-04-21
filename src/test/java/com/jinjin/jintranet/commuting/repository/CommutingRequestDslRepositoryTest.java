package com.jinjin.jintranet.commuting.repository;

import com.jinjin.jintranet.member.repository.MemberRepository;
import com.jinjin.jintranet.model.CommutingRequest;
import com.jinjin.jintranet.model.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
class CommutingRequestDslRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CommutingRequestRepository commutingRequestRepository;

    @Autowired
    CommutingRequestDslRepository commutingRequestDslRepository;

    @Test
    @Transactional
    public void commutingRequestSearching() {
        Member member = memberRepository.findById(9).orElseThrow(() -> {
            return new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        });


        List<CommutingRequest> commutingRequests = commutingRequestDslRepository.commutingRequestSearching(member, "", "2023");
        for(CommutingRequest c : commutingRequests) {
            System.out.println("c.getMember().getClass() = " + c.getMember().getName());
        }
    }

    @Test
    @Transactional
    public void commutingRequestSearching2() {

        List<CommutingRequest> commutingRequests = commutingRequestRepository.findAll();
        for(CommutingRequest c : commutingRequests) {
            System.out.println("c.getMember().getClass() = " + c.getMember());
        }
    }
}