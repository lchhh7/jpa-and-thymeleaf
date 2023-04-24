package com.jinjin.jintranet.notice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;

@SpringBootTest
class NoticeDslRepositoryTest {

    @Autowired
    NoticeDslRepository noticeDslRepository;

    @Test
    @Transactional
    void test() {

        Pageable pageable = PageRequest.of(0,10);
        long start = System.currentTimeMillis();
        System.out.println("test1 실행");

        noticeDslRepository.findNotices(pageable, null , null);

        long end = System.currentTimeMillis();

        System.out.println("Test 완료 실행 시간 : " + (end - start)/ 1000.0 );
    }

}