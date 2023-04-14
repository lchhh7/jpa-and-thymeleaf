package com.jinjin.jintranet.commuting.web;

import com.jinjin.jintranet.commuting.service.CommutingRequestService;
import com.jinjin.jintranet.commuting.service.CommutingService;
import com.jinjin.jintranet.holiday.service.HolidayService;
import com.jinjin.jintranet.member.service.MemberService;
import com.jinjin.jintranet.model.Commuting;
import com.jinjin.jintranet.schedule.service.ScheduleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommutingController.class)
@MockBean(JpaMetamodelMappingContext.class)
class CommutingControllerTest {

    @Autowired
    private MockMvc mockMvc; //controller와 api를 테스트하는 용도

    @MockBean // 테스트할 클래스에서 주입받고 있는 객체에 대해 가짜객체 생성
    CommutingService commutingService;

    @MockBean
    ScheduleService scheduleService;

    @MockBean
    MemberService memberService;

    @MockBean
    HolidayService holidayService;

    @MockBean
    CommutingRequestService commutingRequestService;

    @Test
    @WithMockCustomUser(memberId = "admin", name = "관리자")
    @DisplayName("commutingController main method test")
    void commuting() throws Exception {
        mockMvc.perform(
                        get("/commuting.do"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void currentMonth() {
    }

    @Test
    void findScheduleAll() {
    }

    @Test
    @WithMockCustomUser(memberId = "admin", name = "관리자")
    @DisplayName("commutingController findById method test")
    void findById() throws Exception {
        given(commutingService.findById(54)).willReturn(
                Commuting.builder().id(54).commutingTm(LocalDateTime.now()).attendYn("Y").build());

        int commutingId = 54;

        mockMvc.perform(
                        get("/commuting/" + commutingId + ".do"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void edit() {
    }
}