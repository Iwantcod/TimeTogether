package com.example.service;


import com.example.model.*;
import com.example.repository.ScheduleDataRepository;
import com.example.repository.SharedCalenderRepository;
import com.example.repository.SharedUserRepository;
import com.example.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class SharedCalenderService {

    private final SharedCalenderRepository sharedCalenderRepository;
    private final UsersRepository usersRepository;
    private final SharedUserRepository sharedUserRepository;
    private final ScheduleDataRepository scheduleDataRepository;

    @Autowired
    public SharedCalenderService(SharedCalenderRepository sharedCalenderRepository, UsersRepository usersRepository, UserService userService, SharedUserRepository sharedUserRepository, ScheduleDataRepository scheduleDataRepository) {
        this.sharedCalenderRepository = sharedCalenderRepository;
        this.usersRepository = usersRepository;
        this.sharedUserRepository = sharedUserRepository;
        this.scheduleDataRepository = scheduleDataRepository;
    }

    public boolean create(SharedCalenderDTO sharedCalenderDTO) {

        // 1. 유저 존재여부 검사
        // 2. 유저를 adminUser로 설정하여 새 캘린더 생성
        Optional<Users> usersOptional = usersRepository.findById(sharedCalenderDTO.getAdminUserId());
        if(usersOptional.isPresent()) {
            Users adminUser = usersOptional.get();

            SharedCalender sharedCalender = new SharedCalender();

            sharedCalender.setAdminUser(adminUser);
            sharedCalender.setCalenderName(sharedCalenderDTO.getCalenderName());
            sharedCalender.setStartDate(sharedCalenderDTO.getStartDate());
            sharedCalender.setEndDate(sharedCalenderDTO.getEndDate());
            if(sharedCalenderDTO.getStartDate().isAfter(sharedCalenderDTO.getEndDate())) {
                return false;
            }
            sharedCalenderRepository.save(sharedCalender);


            // 캘린더 생성자도 캘린더 참여자 테이블에 삽입
            SharedUser sharedUser = new SharedUser();
            sharedUser.setSharedUser(adminUser);
            sharedUser.setSharedCalender(sharedCalender);
            sharedUserRepository.save(sharedUser);


            return true;
        } else {
            return false;
        }
    }


    public boolean update(SharedCalenderDTO sharedCalenderDTO) {
        Optional<SharedCalender> sharedCalenderOptional = sharedCalenderRepository.findById(sharedCalenderDTO.getId());
        if(sharedCalenderOptional.isPresent()) {
            SharedCalender sharedCalender = sharedCalenderOptional.get();

            sharedCalender.setCalenderName(sharedCalenderDTO.getCalenderName());
            // 일단은 종료 기간만 늘릴 수 있도록 만든다.
            sharedCalender.setEndDate(sharedCalenderDTO.getEndDate());
            sharedCalenderRepository.save(sharedCalender);
            return true;
        } else {
            return false;
        }
    }

    public boolean delete(Long id) {
        if(sharedCalenderRepository.existsById(id)) {
            sharedCalenderRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public SharedCalenderDTO calInfo(Long id) {
        Optional<SharedCalender> sharedCalenderOptional = sharedCalenderRepository.findById(id);
        if(sharedCalenderOptional.isPresent()) {
            SharedCalenderDTO sharedCalenderDTO = new SharedCalenderDTO();
            sharedCalenderDTO.setId(sharedCalenderOptional.get().getId());
            sharedCalenderDTO.setAdminUserId(sharedCalenderOptional.get().getAdminUserId().getId());
            sharedCalenderDTO.setCalenderName(sharedCalenderOptional.get().getCalenderName());
            sharedCalenderDTO.setStartDate(sharedCalenderOptional.get().getStartDate());
            sharedCalenderDTO.setEndDate(sharedCalenderOptional.get().getEndDate());
            return sharedCalenderDTO;
        } else {
            return null;
        }
    }

    public List<SharedUserDTO> userList(Long calenderid) {
        Optional<SharedCalender> sharedCalenderOptional = sharedCalenderRepository.findById(calenderid);
        if(sharedCalenderOptional.isEmpty()) {
            return null;
        }
        List<SharedUser> sharedUserList = sharedUserRepository.findBySharedCalender(sharedCalenderOptional.get());
        if(sharedUserList.isEmpty()) {
            return null;
        }

        List<SharedUserDTO> sharedUserDTOList = new ArrayList<>();
        for (SharedUser sharedUser : sharedUserList) {
            SharedUserDTO sharedUserDTO = new SharedUserDTO();
            sharedUserDTO.setId(sharedUser.getId());
            sharedUserDTO.setSharedUserId(sharedUser.getSharedUser().getId());
            sharedUserDTO.setSharedCalenderId(sharedUser.getSharedCalender().getId());
            sharedUserDTOList.add(sharedUserDTO);
        }
        return sharedUserDTOList;
    }


    public List<ScheduleDataDTO> eventList(Long calenderid) {
        Optional<SharedCalender> sharedCalenderOptional = sharedCalenderRepository.findById(calenderid);
        if(sharedCalenderOptional.isEmpty()) {
            return null;
        }
        List<ScheduleData> scheduleDataDTOList = scheduleDataRepository.findAllBySharedCalender(sharedCalenderOptional.get());
        if(scheduleDataDTOList.isEmpty()) {
            return null;
        }

        List<ScheduleDataDTO> eventList = new ArrayList<>();
        for (ScheduleData scheduleData : scheduleDataDTOList) {
            ScheduleDataDTO scheduleDataDTO = new ScheduleDataDTO();
            scheduleDataDTO.setId(scheduleData.getId());
            scheduleDataDTO.setStartDate(scheduleData.getStartDate());
            scheduleDataDTO.setEndDate(scheduleData.getEndDate());
            scheduleDataDTO.setUserNickname(scheduleData.getUsers().getUserNickname());
            scheduleDataDTO.setContent(scheduleData.getContent());
            scheduleDataDTO.setSharedCalenderId(scheduleData.getSharedCalender().getId());
            eventList.add(scheduleDataDTO);
        }
        return eventList;
    }
}