package com.example.controller;


import com.example.model.SharedCalenderDTO;
import com.example.service.InviteService;
import com.example.service.SharedCalenderService;
import com.example.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/calender")
public class SharedCalenderController {

    private final SharedCalenderService sharedCalenderService;
    private final InviteService inviteService;

    public SharedCalenderController(SharedCalenderService sharedCalenderService, UserService userService, InviteService inviteService) {
        this.sharedCalenderService = sharedCalenderService;
        this.inviteService = inviteService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody SharedCalenderDTO sharedCalenderDTO) {
        if(sharedCalenderDTO.getStartDate().isAfter(sharedCalenderDTO.getEndDate())) {
            return ResponseEntity.badRequest().body("Start date cannot be after end date");
        } else if (sharedCalenderService.create(sharedCalenderDTO)) {
            return ResponseEntity.ok("Create Successful");
        } else {
            return ResponseEntity.status(400).body("User not exist");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody SharedCalenderDTO sharedCalenderDTO) {
        if(sharedCalenderDTO.getStartDate().isAfter(sharedCalenderDTO.getEndDate())) {
            return ResponseEntity.badRequest().body("Start date cannot be after end date");
        } else if (sharedCalenderService.update(sharedCalenderDTO)) {
            return ResponseEntity.ok("Update Successful");
        } else {
            return ResponseEntity.status(400).body("Calender not exist");
        }
    }


    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if(sharedCalenderService.delete(id)) {
            return ResponseEntity.ok("Delete Successful");
        } else {
            return ResponseEntity.status(400).body("Calender not exist");
        }
    }

    // 캘린더 정보 조회
    @GetMapping("/{id}/info")
    public ResponseEntity<?> getInfo(@PathVariable Long id) {
        if(sharedCalenderService.calInfo(id) != null) {
            return ResponseEntity.ok(sharedCalenderService.calInfo(id));
        } else {
            return ResponseEntity.status(400).body("Calender not exist");
        }
    }


    // 캘린더 내부 이벤트들 조회
    @GetMapping("{calenderid}/eventlist")
    public ResponseEntity<?> getEventList(@PathVariable Long calenderid) {
        if(sharedCalenderService.calInfo(calenderid) != null) {
            return ResponseEntity.ok(sharedCalenderService.eventList(calenderid));
        } else {
            return ResponseEntity.status(400).body("Event not exist");
        }

    }

    // 캘린더 참여자들 목록 조회
    @GetMapping("{calenderid}/userlist")
    public ResponseEntity<?> getUserList(@PathVariable Long calenderid) {
        if(sharedCalenderService.userList(calenderid) != null) {
            return ResponseEntity.ok(sharedCalenderService.userList(calenderid));
        } else {
            return ResponseEntity.status(404).body("No Users");
        }
    }

    // 캘린더 초대링크 생성
    @GetMapping("/{calenderid}/getlink")
    public ResponseEntity<?> getLink(@PathVariable Long calenderid) throws JsonProcessingException {
        if(inviteService.generateInviteLink(calenderid) != null) {
            return ResponseEntity.ok(inviteService.generateInviteLink(calenderid));
        } else {
            return ResponseEntity.status(400).body("Invite Link not exist");
        }
    }


}
