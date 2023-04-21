package com.jinjin.jintranet.schedule.repository;

import com.jinjin.jintranet.model.Schedule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
class ScheduleRepositoryTest {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Test
    @Transactional
    void test1() {
        List<Schedule> schedules = scheduleRepository.findAll();
        for(Schedule s : schedules) {
            System.out.println("s.getMember().getClass() = " + s.getMember().getClass());
            s.getMember().getName();
        }
    }
}