package com.example.service;

import com.example.model.*;
import com.example.repository.ScheduleDataRepository;
import com.example.repository.SharedCalenderRepository;
import com.example.repository.SharedUserRepository;
import com.example.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class SharedUserService {
    private final SharedUserRepository sharedUserRepository;
    private final UsersRepository usersRepository;
    private final SharedCalenderRepository sharedCalenderRepository;
    private final ScheduleDataRepository scheduleDataRepository;

    @Autowired
    public SharedUserService(SharedUserRepository sharedUserRepository, UsersRepository usersRepository, SharedCalenderRepository sharedCalenderRepository, ScheduleDataRepository scheduleDataRepository) {
        this.sharedUserRepository = sharedUserRepository;
        this.usersRepository = usersRepository;
        this.sharedCalenderRepository = sharedCalenderRepository;
        this.scheduleDataRepository = scheduleDataRepository;
    }

    // 캘린더 유저 참여 정보를 추가
    public boolean addUser(SharedUserDTO sharedUserDTO) {
        Optional<Users> usersOptional = usersRepository.findById(sharedUserDTO.getSharedUserId());
        Optional<SharedCalender> sharedCalenderOptional = sharedCalenderRepository.findById(sharedUserDTO.getSharedCalenderId());
        if(usersOptional.isEmpty() || sharedCalenderOptional.isEmpty()) {
            return false;
        }

        List<SharedUser> sharedUserList = sharedUserRepository.findBySharedUser(usersOptional.get());
        for(SharedUser sharedUser : sharedUserList) {
            // 중복 참여 방지
            if(sharedUser.getSharedCalender().getId().equals(sharedUserDTO.getSharedCalenderId())) {
                return false;
            }
        }

        SharedUser sharedUser = new SharedUser();
        sharedUser.setSharedUser(usersOptional.get());
        sharedUser.setSharedCalender(sharedCalenderOptional.get());
        sharedUserRepository.save(sharedUser);
        return true;
    }

    // 캘린더에서 유저 삭제
    public boolean removeUser(SharedUserDTO sharedUserDTO) {
        // 제거할 SharedUser 객체를 찾기 위해 Users, SharedCalender를 조회
        Optional<Users> userOptional = usersRepository.findById(sharedUserDTO.getSharedUserId());
        Optional<SharedCalender> sharedCalenderOptional = sharedCalenderRepository.findById(sharedUserDTO.getSharedCalenderId());

        // Users, SharedCalender가 존재하면 그 정보로 SharedUser 객체 검색 -> 존재하면 삭제 후 true 리턴
        if(userOptional.isPresent() && sharedCalenderOptional.isPresent()){
            Optional<SharedUser> deleteTarget = sharedUserRepository.findBySharedUserAndSharedCalender(userOptional.get(), sharedCalenderOptional.get());
            scheduleDataRepository.deleteByUsers(userOptional.get());
            if(deleteTarget.isPresent()) {
                // 만약 캘린더에서 삭제되는 유저가 관리자라면, 캘린더 자체를 제거
                if(sharedUserDTO.getSharedUserId().equals(sharedCalenderOptional.get().getAdminUserId().getId())) {
                    sharedCalenderRepository.deleteById(deleteTarget.get().getSharedCalender().getId());
                    return true;
                }
                sharedUserRepository.delete(deleteTarget.get());
                return true;
            }
            return false;
        }

        // 제거하려는 참여 정보(유저 식별자, 캘린더 식별자의 한 쌍)를 찾아서 제거

        return false;
    }

}
