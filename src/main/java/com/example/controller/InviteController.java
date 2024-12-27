package com.example.controller;

import com.example.service.InviteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invite")
public class InviteController {

    private final InviteService inviteService;

    public InviteController(InviteService inviteService) {
        this.inviteService = inviteService;
    }

    // 초대링크 클릭 시 호출되는 API
    @GetMapping
    public ResponseEntity<String> handleInvite(@RequestParam String token, HttpSession session) {
        if(inviteService.clickInvite(token, session)) {
            System.out.println("Session Value: "+session.getAttribute("calenderId"));
            return ResponseEntity.ok("Calender ID Stored");
        } else {
            return ResponseEntity.status(400).body("Invalid invite link");
        }
    }



}
