package com.example.repository;

import com.example.model.ScheduleData;
import com.example.model.SharedCalender;
import com.example.model.SharedUser;
import com.example.model.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleDataRepository extends JpaRepository<ScheduleData, Long> {
    List<ScheduleData> findAllBySharedCalender(SharedCalender calender);
    List<ScheduleData> findAllByUsers(SharedUser sharedUser);

    @Transactional
    void deleteByUsers(Users users);

}
