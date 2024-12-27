package com.example;

import com.example.model.*;
import com.example.repository.ScheduleDataRepository;
import com.example.repository.SharedCalenderRepository;
import com.example.repository.SharedUserRepository;
import com.example.repository.UsersRepository;
import com.example.service.ScheduleDataService;
import com.example.service.SharedCalenderService;
import com.example.service.SharedUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class JpaExampleApplicationTests {

	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private SharedCalenderRepository sharedCalenderRepository;
	@Autowired
	private SharedUserRepository sharedUserRepository;
	@Autowired
	private ScheduleDataRepository scheduleDataRepository;
    @Autowired
    private SharedUserService sharedUserService;
    @Autowired
    private ScheduleDataService scheduleDataService;
    @Autowired
    private SharedCalenderService sharedCalenderService;

	@Test
	void contextLoads() {

//		SharedUserDTO sharedUserDTO = new SharedUserDTO();
//		sharedUserDTO.setSharedCalenderId(1L);
//		sharedUserDTO.setSharedUserId(1L);
////		sharedUserService.addUser(sharedUserDTO);
//
//		sharedUserService.removeUser(sharedUserDTO);
		usersRepository.findById(1L);
		SharedCalenderDTO sharedCalenderDTO = new SharedCalenderDTO();
		sharedCalenderDTO.setStartDate(LocalDate.now().minusDays(1));
		sharedCalenderDTO.setEndDate(LocalDate.of(2030,01,01));
		sharedCalenderDTO.setCalenderName("TestCal");
		sharedCalenderDTO.setAdminUserId(1L);
		sharedCalenderService.create(sharedCalenderDTO);



	}

}
