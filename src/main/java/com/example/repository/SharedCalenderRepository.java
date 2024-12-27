package com.example.repository;

import com.example.model.SharedCalender;
import com.example.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SharedCalenderRepository extends JpaRepository<SharedCalender, Long> {


    void deleteByAdminUser(Users adminUser);
}
