package com.example.service;

import com.example.model.*;
import com.example.repository.ScheduleDataRepository;
import com.example.repository.SharedCalenderRepository;
import com.example.repository.SharedUserRepository;
import com.example.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ScheduleDataService {

    private final ScheduleDataRepository scheduleDataRepository;
    private final UsersRepository usersRepository;
    private final SharedCalenderRepository sharedCalenderRepository;
    private final SharedUserRepository sharedUserRepository;

    @Autowired
    public ScheduleDataService(ScheduleDataRepository scheduleDataRepository, UsersRepository usersRepository, SharedCalenderRepository sharedCalenderRepository, SharedUserRepository sharedUserRepository) {
        this.scheduleDataRepository = scheduleDataRepository;
        this.usersRepository = usersRepository;
        this.sharedCalenderRepository = sharedCalenderRepository;
        this.sharedUserRepository = sharedUserRepository;
    }

    // 일정 추가
    public boolean addScheduleData(ScheduleDataDTO scheduleDataDTO) {
        Optional<Users> usersOptional = usersRepository.findByUserNickname(scheduleDataDTO.getUserNickname());
        Optional<SharedCalender> sharedCalenderOptional = sharedCalenderRepository.findById(scheduleDataDTO.getSharedCalenderId());
        if(usersOptional.isEmpty() || sharedCalenderOptional.isEmpty()
                || sharedUserRepository.findBySharedUserAndSharedCalender(
                        usersOptional.get(), sharedCalenderOptional.get()).isEmpty()) {

            System.out.println("Cant Found Shared User");
            return false;
            // 유저가 존재하지 않거나, 캘린더가 존재하지 않거나, 해당 유저가 캘린더의 참여자가 아닌 경우에 false 반환.
        }
        LocalDate startDate = sharedCalenderOptional.get().getStartDate();
        LocalDate endDate = sharedCalenderOptional.get().getEndDate();

        LocalDate eventStartDate = scheduleDataDTO.getStartDate();
        LocalDate eventEndDate = scheduleDataDTO.getEndDate();
        if(startDate.isAfter(eventEndDate) || endDate.isBefore(eventStartDate)) {
            System.out.println("Date Error");
            return false;
        }


        // 일정의 기간이 캘린더 범위에 걸치지만, 시작 혹은 종료 날짜가 캘린더 범위보다 더 밖에 있는 경우 = 캘린더의 시작 혹은 종료날짜로 적용
        if(eventStartDate.isBefore(startDate)) {
            eventStartDate = startDate;
        } else if(eventEndDate.isAfter(endDate)) {
            eventEndDate = endDate;
        }
        ScheduleData scheduleData = new ScheduleData();
        scheduleData.setUsers(usersOptional.get());
        scheduleData.setSharedCalender(sharedCalenderOptional.get());
        scheduleData.setStartDate(eventStartDate);
        scheduleData.setEndDate(eventEndDate);
        scheduleData.setContent(scheduleDataDTO.getContent());
        scheduleDataRepository.save(scheduleData);
        return true;
    }

    @Transactional
    public boolean syncScheduleData(ScheduleSyncDTO scheduleSyncDTO) {
        Optional<Users> usersOptional = usersRepository.findByUserNickname(scheduleSyncDTO.getUserNickname());
        Optional<SharedCalender> sharedCalenderOptional = sharedCalenderRepository.findById(scheduleSyncDTO.getSharedCalenderId());
        // 유저가 존재하지 않거나, 캘린더가 존재하지 않거나, 해당 유저가 캘린더의 참여자가 아니거나, 동기화할 일정이 없는 경우에 false 반환.
        if(usersOptional.isEmpty() || sharedCalenderOptional.isEmpty()
                ||sharedUserRepository.findBySharedUserAndSharedCalender(
                usersOptional.get(), sharedCalenderOptional.get()).isEmpty() || scheduleSyncDTO.getScheduleInfo().isEmpty()) {
            System.out.println("Cannot Sync Schedule");
            return false;
        }
        // 캘린더의 시작, 종료 날짜 정보
        LocalDate startDate = sharedCalenderOptional.get().getStartDate();
        LocalDate endDate = sharedCalenderOptional.get().getEndDate();


        boolean isSync = false;
        for (ScheduleInfo scheduleInfo : scheduleSyncDTO.getScheduleInfo()) {
            LocalDate eventStartDate = scheduleInfo.getStartDate();
            LocalDate eventEndDate = scheduleInfo.getEndDate();

            if( !(startDate.isAfter(eventEndDate) || endDate.isBefore(eventStartDate)) ) {
                // 일정의 기간이 캘린더 범위에 걸치지만, 시작 혹은 종료 날짜가 캘린더 범위보다 더 밖에 있는 경우 = 캘린더의 시작 혹은 종료날짜로 적용
                if(eventStartDate.isBefore(startDate)) {
                    eventStartDate = startDate;
                } else if(eventEndDate.isAfter(endDate)) {
                    eventEndDate = endDate;
                }

                ScheduleData scheduleData = new ScheduleData();
                scheduleData.setUsers(usersOptional.get());
                scheduleData.setSharedCalender(sharedCalenderOptional.get());
                scheduleData.setStartDate(eventStartDate);
                scheduleData.setEndDate(eventEndDate);
                scheduleData.setContent(scheduleInfo.getContent());
                scheduleDataRepository.save(scheduleData);
                isSync = true;
            }
        }
        return isSync;
    }

    public boolean updateScheduleData(ScheduleDataDTO scheduleDataDTO) {
        if(scheduleDataDTO.getStartDate().isAfter(scheduleDataDTO.getEndDate())) {
            return false;
        }
        Optional<ScheduleData> scheduleDataOptional = scheduleDataRepository.findById(scheduleDataDTO.getId());
        Optional<SharedCalender> sharedCalenderOptional = sharedCalenderRepository.findById(scheduleDataDTO.getSharedCalenderId());
        if(scheduleDataOptional.isEmpty() || sharedCalenderOptional.isEmpty()) {
            return false;
        }

        // 공유캘린더 (시작날짜-1) ~ (종료날짜+1) 사이의 범위에 들어와야 한다. 이때, 범위 내에는 기준이 되는 날짜는 포함하지 않는다.
        LocalDate startDate = sharedCalenderOptional.get().getStartDate().minusDays(1);
        LocalDate endDate = sharedCalenderOptional.get().getEndDate().plusDays(1);

        if(startDate.isBefore(scheduleDataDTO.getStartDate()) && endDate.isAfter(scheduleDataDTO.getEndDate())) {
            // 일정 내용, 시작일자, 종료일자만을 수정할 수 있다.
            scheduleDataOptional.get().setContent(scheduleDataDTO.getContent());
            scheduleDataOptional.get().setStartDate(scheduleDataDTO.getStartDate());
            scheduleDataOptional.get().setEndDate(scheduleDataDTO.getEndDate());
            scheduleDataRepository.save(scheduleDataOptional.get());
            return true;
        }
        return false;
    }

    public boolean deleteScheduleData(Long id) {
        if(scheduleDataRepository.existsById(id)){
            scheduleDataRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
