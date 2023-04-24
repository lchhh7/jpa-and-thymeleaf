package com.jinjin.jintranet.common;
import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.Schedule;
import com.jinjin.jintranet.schedule.repository.ScheduleDslRepository;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VacationDaysUtils {
    private final ScheduleDslRepository scheduleRepository;

    @Transactional
	public Member getMemberVacationDays(
            Member member, int year, int month, int date) throws Exception {

    	try {
        double total = getTotalVacationDays(member, year, month, date);
        double use = getUseVacationDays(member, year, month, date);
        member.setTotal(total);
        member.setUse(use);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}

        return member;
    }


    private double getTotalVacationDays(
            Member member, int curYear, int curMonth, int curDate) {

        double total = 0;
        
        LocalDate doe = new LocalDate(member.getCrtDt().getYear() , member.getCrtDt().getMonthValue(), member.getCrtDt().getDayOfMonth());
        int yearsOfService = doe.getYear();
        
        if(curYear < yearsOfService) {
        	return 0;
        }
        
        if (curYear - yearsOfService < 2) {
            if (curYear == yearsOfService) {
                int monthsOfService = doe.getMonthOfYear();
                if (curMonth > monthsOfService) {
                    total = curMonth - monthsOfService;
                    if (curDate < doe.getDayOfMonth()) total--;
                }
            } else {
                LocalDate prev = new LocalDate(curYear - 1, 12, 31);
                int prevDays = prev.getDayOfYear();
                int monthsOfService = doe.getMonthOfYear(); //1월 9월 
                double daysOfService = prevDays - doe.getDayOfYear();

                total = Math.floor(daysOfService / 365 * 15);
                
                for(int i = 1 ; i <= monthsOfService ; i++) {
                	if (curMonth > i) {
                    	total++;
                    }
                }
                
            }
        } else {
            total = 15;
            if (total > 25) total = 25;
        }

        return total;
    }

    private Double getUseVacationDays(Member member, int curYear, int curMonth, int curDate) {
        List<Schedule> vacations = scheduleRepository.vacations(member, curYear);
        
        double half = vacations.stream().filter(v -> "HV".equals(v.getType())).count();
        double full = vacations.size() - half;
        

        return full + (half * 0.5);
    }
}
