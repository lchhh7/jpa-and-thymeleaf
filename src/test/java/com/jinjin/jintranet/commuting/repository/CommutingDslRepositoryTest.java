package com.jinjin.jintranet.commuting.repository;

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
    }
}