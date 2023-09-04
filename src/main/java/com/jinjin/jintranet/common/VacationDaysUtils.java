package com.jinjin.jintranet.common;

import com.jinjin.jintranet.commuting.dto.CommuteRequestDTO;
import com.jinjin.jintranet.commuting.dto.CommutingsInterface;
import com.jinjin.jintranet.commuting.repository.CommutingRepository;
import com.jinjin.jintranet.commuting.repository.CommutingRequestRepository;
import com.jinjin.jintranet.commuting.service.CommutingService;
import com.jinjin.jintranet.member.dto.MemberCommuteDTO;
import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.Schedule;
import com.jinjin.jintranet.schedule.repository.ScheduleDslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VacationDaysUtils {
    private final ScheduleDslRepository scheduleRepository;

    private final CommutingRepository commutingRepository;

    private final CommutingRequestRepository commutingRequestRepository;

    private final CommutingService commutingService;

    @Transactional
	public MemberCommuteDTO getMemberVacationDays(
            Member member, int year, int month, int date) {

        double total = getTotalVacationDays(member, year, month, date);
        double use = getUseVacationDays(member, year, month, date);
      //  int add = getAddVacationDays(member);
        return new MemberCommuteDTO(member.getName() , total , use , 0 , member.getCrtDt());
    }


    private double getTotalVacationDays(
            Member member, int curYear, int curMonth, int curDate) {

        double total = 0;


        LocalDate doe = LocalDate.of(member.getCrtDt().getYear() , member.getCrtDt().getMonthValue(), member.getCrtDt().getDayOfMonth());
        int yearsOfService = doe.getYear();
        
        if(curYear < yearsOfService) {
        	return 0;
        }
        
        if (curYear - yearsOfService < 2) {
            if (curYear == yearsOfService) {
                int monthsOfService = doe.getMonthValue();
                if (curMonth > monthsOfService) {
                    total = curMonth - monthsOfService;
                    if (curDate < doe.getDayOfMonth()) total--;
                }
            } else {
                LocalDate prev = LocalDate.of(curYear - 1, 12, 31);
                int prevDays = prev.getDayOfYear();
                int monthsOfService = doe.getMonthValue();
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

    public int getAddVacationDays(Member member) {
        int addBreak = 0;
        for(int i=1 ; i <=LocalDate.now().getMonthValue() ; i++) {
            LocalDate firstDayOfMonth = LocalDate.of(LocalDate.now().getYear(), i, 1);
            LocalDate lastDayOfMonth= firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());
            String strDt = firstDayOfMonth.toString();
            String endDt = lastDayOfMonth.toString();
            int hours = 0; int minutes = 0;

            List<CommutingsInterface> list = commutingRepository.findCommuteOn(member, strDt, endDt);
            list.addAll(commutingRepository.findCommuteOff(member, strDt, endDt));
            list.addAll(commutingRepository.findCommuteOvertime(member, strDt, endDt));

            List<CommuteRequestDTO> commuteRequests = commutingRequestRepository.findCommute(member).stream()
                    .filter(m -> java.time.LocalDate.parse(m.getRequestDt()).isAfter(java.time.LocalDate.parse(strDt)))
                    .filter(m -> java.time.LocalDate.parse(m.getRequestDt()).isBefore(java.time.LocalDate.parse(endDt)))
                    .filter(m -> m.getType().equals("O"))
                    .filter(m -> m.getDeletedBy() == null)
                    .map(m -> new CommuteRequestDTO(m)).collect(Collectors.toList());
            if(commuteRequests.size() > 0) {
               List<CommuteRequestDTO> overtimes = commutingService.overtimes(list, commuteRequests, i);

               for(CommuteRequestDTO dto : overtimes) {
                   hours += dto.getHours();
                   minutes += dto.getMinutes();
               }
                hours += (int) minutes / 60;
                addBreak += hours/8;
            }
        }

        return addBreak;
    }

}
