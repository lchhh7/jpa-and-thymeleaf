package com.jinjin.jintranet.holiday.service;

import com.jinjin.jintranet.common.HolidayUtils;
import com.jinjin.jintranet.model.Holiday;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Controller
public class HolidayAPIController {

    private final HolidayService holidayService;

    @PostMapping(value ="/admin/holidayInfoAPI.do")
    public ResponseEntity holidayInfoAPI() {
        try {
            Map<String, Object> holidayMap = HolidayUtils.holidayInfoAPI(String.valueOf(LocalDate.now().getYear()));
            Map<String, Object> response = (Map<String, Object>) holidayMap.get("response");
            Map<String, Object> body = (Map<String, Object>) response.get("body");

            int totalCount = (int) body.get("totalCount");

            if(totalCount == 0 || holidayService.countHolidayBy(LocalDate.now().getYear()) != 0) return ResponseEntity.ok().body("추가할 공휴일이 없습니다.");


            HashMap<String, Object> items = (HashMap<String, Object>) body.get("items");
            ArrayList<HashMap<String, Object>> item = (ArrayList<HashMap<String, Object>>) items.get("item");
            for (HashMap<String, Object> itemMap : item) {
                String locdate = String.valueOf((Integer) itemMap.get("locdate"));
                LocalDate dateTime =LocalDate.of(Integer.parseInt(locdate.substring(0,4)) , Integer.parseInt(locdate.substring(4,6)) , Integer.parseInt(locdate.substring(6,8)));
                holidayService.write(new Holiday((String) itemMap.get("dateName") , dateTime));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body("공휴일 추가가 완료되었습니다.");
    }
}
