package com.jinjin.jintranet.schedule.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jinjin.jintranet.common.VacationDaysUtils;
import com.jinjin.jintranet.member.repository.MemberRepository;
import com.jinjin.jintranet.model.Member;
import com.jinjin.jintranet.model.Schedule;
import com.jinjin.jintranet.schedule.dto.ScheduleInsertDTO;
import com.jinjin.jintranet.schedule.dto.ScheduleSearchDTO;
import com.jinjin.jintranet.schedule.dto.todaySchedulesDTO;
import com.jinjin.jintranet.schedule.repository.ScheduleDslRepository;
import com.jinjin.jintranet.schedule.repository.ScheduleRepository;
import com.jinjin.jintranet.security.auth.PrincipalDetail;

@Service
public class ScheduleService {
	
	private ScheduleRepository scheduleRepository;
	
	private ScheduleDslRepository scheduleDslRepository;
	
	private MemberRepository memberRepository;
	
	private VacationDaysUtils vacationDaysUtils;
	
	
	public ScheduleService(ScheduleRepository scheduleRepository, ScheduleDslRepository scheduleDslRepository,
			MemberRepository memberRepository , VacationDaysUtils vacationDaysUtils) {
		this.scheduleRepository = scheduleRepository;
		this.scheduleDslRepository = scheduleDslRepository;
		this.memberRepository = memberRepository;
		this.vacationDaysUtils = vacationDaysUtils;
	}
	
	@Transactional
	public void write(ScheduleInsertDTO scheduleDTO , Member member , Member approve) {
		
		Schedule schedule = new Schedule(scheduleDTO , approve);
		schedule.setStatus("R");
		schedule.setMember(member);
		schedule.setCreatedBy(member.getName());
		scheduleRepository.save(schedule);
	}
	
	@Transactional
	public void edit(int id,Schedule requestSchedule , PrincipalDetail principal) {
		Schedule schedule = scheduleRepository.findById(id)
				.orElseThrow(() -> {
					return new IllegalArgumentException("일정을 찾을 수 없습니다.");
				}); //영속화
		
		schedule.setTitle(requestSchedule.getTitle());
		schedule.setContent(requestSchedule.getContent());
		schedule.setStrDt(requestSchedule.getStrDt());
		schedule.setEndDt(requestSchedule.getEndDt());
		schedule.setModifiedBy(principal.getMember().getName());
		
	}
	
	@Transactional
	public void cancel(int id,Schedule requestSchedule, PrincipalDetail principal) {
		Schedule schedule = scheduleRepository.findById(id)
				.orElseThrow(() -> {
					return new IllegalArgumentException("일정을 찾을 수 없습니다.");
				}); //영속화
		
		schedule.setCancelReason(requestSchedule.getCancelReason());
		schedule.setStatus("C");
		schedule.setModifiedBy(principal.getMember().getName());
	}
	
	/*@Transactional
	public void delete(Integer id, EntityManager em) {
		Member member = em.find(Member.class , 100L);
		Schedule schedule = em.find(Schedule.class , 100L);
		member.setSchedules(null);
		schedule.setMember(null);
		
		System.out.println("==========================================1");
		//scheduleRepository.findById(id);
		//entityManager.clear();
		System.out.println("==========================================2");
		//scheduleRepository.deleteById(id);
		System.out.println("==========================================3");
	}*/
	@Transactional
	public void delete(Integer id,PrincipalDetail principal) {
		Schedule schedule = scheduleRepository.findById(id)
				.orElseThrow(() -> {
					return new IllegalArgumentException("일정을 찾을 수 없습니다.");
				});
		schedule.setStatus("D");
		schedule.setDeletedBy(principal.getMember().getName());
	}


	
	@Transactional
	public List<ScheduleSearchDTO> read(Schedule schedule) {
		return scheduleDslRepository.findSchedule(schedule);
	}
	
	@Transactional
	public List<todaySchedulesDTO> todaySchedules() {
		List<todaySchedulesDTO> list = scheduleRepository.todaySchedules().stream().map(m -> new todaySchedulesDTO(m)).collect(Collectors.toList());
		return list;
	}
	
	@Transactional
	public Schedule findById(Integer id) {
		return scheduleRepository.findById(id).orElseThrow(() ->{
			return new IllegalArgumentException("해당 일정을 조회하는중 오류가 발생했습니다");
		});
	}
	
	@Transactional
	public List<Schedule> findByMemberId(Member member) {
		return scheduleRepository.findByMemberId(member);
	}
	
	@Transactional
	public Integer cnt(Member member) {
		return scheduleRepository.cnt(member);
	}
	
	@Transactional
	public Page<Schedule> approvesList(Member member, Integer m, List<String> statusList , Pageable pageable) {
		return scheduleDslRepository.approvesList(member, m , statusList , pageable);
	}
	
	@Transactional
	public void approves(int id,Schedule requestSchedule, Member m) {
		Schedule schedule = scheduleRepository.findById(id)
				.orElseThrow(() -> {
					return new IllegalArgumentException("일정을 찾을 수 없습니다.");
				}); //영속화
		
		schedule.setStatus(requestSchedule.getStatus());
		schedule.setApproveDt(LocalDate.now());
		if("D".equals(requestSchedule.getStatus())) {
			schedule.setDeletedBy(m.getName());
		}
	}
	
	@Transactional
	public List<Member> vacationDays() throws Exception {
		List<Member> list = new ArrayList<>();
		
		LocalDate now = LocalDate.now();
		
		int year = now.getYear();
		int month = now.getMonthValue();
		int date = now.getDayOfMonth();
		
		for(Member m : memberRepository.findWorkers()) {
			 list.add(vacationDaysUtils.getMemberVacationDays(m, year, month, date));
		} 
		return list;
	}
	
	@Transactional
	public List<Schedule> mainSchedules(Member m, String year) {
		return scheduleDslRepository.mainSchedules(m, year);
	}
	
	@Transactional
	public List<String> yearList(Member m) {
		return scheduleRepository.yearList(m);
	}
}
