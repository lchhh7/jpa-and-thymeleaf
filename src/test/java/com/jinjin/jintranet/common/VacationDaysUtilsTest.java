package com.jinjin.jintranet.common;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


class VacationDaysUtilsTest {

    @Test
    void test() {
        LocalDateTime yes = LocalDateTime.of(2023 , 4 ,6 ,9 ,36 ,42);
        LocalDateTime now = LocalDateTime.of(2023 , 4 ,7 ,3 ,32 ,43);



        Duration duration = Duration.between(yes , now);
        System.out.println("duration = " + duration);

        long between = ChronoUnit.HOURS.between(yes, now);
        long between1 = ChronoUnit.MINUTES.between(yes, now);
        System.out.println("between = " + between);
        System.out.println("between1 = " + between1);
    }

}