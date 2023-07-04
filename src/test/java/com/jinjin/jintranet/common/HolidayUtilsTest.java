package com.jinjin.jintranet.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HolidayUtilsTest {

    @InjectMocks
    private HolidayUtils holidayUtils;

    @Test
    @DisplayName("공휴일 정보")
    void get() throws Exception {
        holidayUtils.holidayInfoAPI("2023");
    }
}