package com.example.controller;

import com.example.model.ScheduleDataDTO;
import com.example.model.ScheduleSyncDTO;
import com.example.service.ScheduleDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/event")
public class ScheduleDataController {

    private final ScheduleDataService scheduleDataService;

    public ScheduleDataController(ScheduleDataService scheduleDataService) {
        this.scheduleDataService = scheduleDataService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addEvent(@RequestBody ScheduleDataDTO scheduleDataDTO) {
        if(scheduleDataDTO.getStartDate().isAfter(scheduleDataDTO.getEndDate())) {
            return ResponseEntity.badRequest().body("Start date cannot be after end date");
        } else if(scheduleDataService.addScheduleData(scheduleDataDTO)) {
            return ResponseEntity.ok("Add Event Success");
        } else {
            return ResponseEntity.status(400).body("Add Event Failed");
        }
    }

    // 공유캘린더 초대한 사용자의 일정을 반영하는 API
    @PostMapping("/sync")
    public ResponseEntity<?> syncEvent(@RequestBody ScheduleSyncDTO scheduleSyncDTO) {
        if(scheduleDataService.syncScheduleData(scheduleSyncDTO)) {
            return ResponseEntity.ok("Sync Event Success");
        } else {
            return ResponseEntity.status(400).body("Cannot Found Event");
        }
    }


    @PutMapping("/update")
    public ResponseEntity<?> updateEvent(@RequestBody ScheduleDataDTO scheduleDataDTO) {
        if(scheduleDataDTO.getStartDate().isAfter(scheduleDataDTO.getEndDate())) {
            return ResponseEntity.badRequest().body("Start date cannot be after end date");
        } else if(scheduleDataService.updateScheduleData(scheduleDataDTO)) {
            return ResponseEntity.ok("Update Event Success");
        } else {
            return ResponseEntity.status(400).body("Update Event Failed");
        }
    }


    @DeleteMapping("/{id}/delete")
    // 이벤트 식별자 필요
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        if(scheduleDataService.deleteScheduleData(id)) {
            return ResponseEntity.ok("Delete Event Success");
        } else {
            return ResponseEntity.status(400).body("Delete Event Failed");
        }
    }

}