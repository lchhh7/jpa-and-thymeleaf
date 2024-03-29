package com.jinjin.jintranet.holiday.service;

import com.jinjin.jintranet.common.HolidayUtils;
import com.jinjin.jintranet.handler.CustomException;
import com.jinjin.jintranet.handler.ErrorCode;
import com.jinjin.jintranet.member.service.MemberService;
import com.jinjin.jintranet.model.Holiday;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class HolidayAPIController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HolidayAPIController.class);
    private final HolidayService holidayService;

    @PostMapping(value ="/admin/holidayInfoAPI.do")
    public ResponseEntity holidayInfoAPI() {
        try {
            Map<String, Object> holidayMap = HolidayUtils.holidayInfoAPI(String.valueOf(LocalDate.now().getYear()));
            Map<String, Object> response = (Map<String, Object>) holidayMap.get("response");
            Map<String, Object> body = (Map<String, Object>) response.get("body");

            int totalCount = (int) body.get("totalCount");

            if(totalCount == 0) return ResponseEntity.ok().body("추가할 공휴일이 없습니다.");

            List<Holiday> lh = holidayService.countHolidayBy(LocalDate.now().getYear());

            HashMap<String, Object> items = (HashMap<String, Object>) body.get("items");
            ArrayList<HashMap<String, Object>> item = (ArrayList<HashMap<String, Object>>) items.get("item");
            if(lh.size() == item.size()) return ResponseEntity.ok().body("추가할 공휴일이 없습니다.");

            List<String> dateList = lh.stream().map(h -> h.getHolidayDt().toString().replaceAll("-", "")).collect(Collectors.toList());

            for (HashMap<String, Object> itemMap : item) {
                String locdate = String.valueOf((Integer) itemMap.get("locdate"));
                LocalDate dateTime =LocalDate.of(Integer.parseInt(locdate.substring(0,4)) , Integer.parseInt(locdate.substring(4,6)) , Integer.parseInt(locdate.substring(6,8)));

                if(dateList.contains(locdate)) continue;
                holidayService.write(new Holiday((String) itemMap.get("dateName") , dateTime));
            }
        } catch (Exception e) {
            LOGGER.info("holiday openapi error : "+ e);
        }
        return ResponseEntity.ok().body("공휴일 추가가 완료되었습니다.");
    }
}

