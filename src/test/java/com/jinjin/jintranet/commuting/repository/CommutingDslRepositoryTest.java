package com.jinjin.jintranet.commuting.repository;

import com.jinjin.jintranet.model.Commuting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
class CommutingDslRepositoryTest {

    @Autowired
    CommutingRepository commutingRepository;
    @Autowired
    CommutingDslRepository commutingDslRepository;

    @Test
    @Transactional
    public void commutingRequestSearching() {
        Commuting commuting1 = commutingDslRepository.goToWorkTime("admin");
        System.out.println("onCheck = " + commuting1.getCommutingTm());

        Commuting commuting2 = commutingDslRepository.offToWorkTime("admin");
        System.out.println("offCheck = " + commuting2.getCommutingTm());

        Commuting commuting3 = commutingDslRepository.workingStatus("admin");
        System.out.println("workingStatus = " + commuting3.getAttendYn());
    }
}