package com.example.controller;

import com.example.model.SharedUserDTO;
import com.example.service.SharedUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shareduser")
public class SharedUserController {

    private final SharedUserService sharedUserService;

    public SharedUserController(SharedUserService sharedUserService) {
        this.sharedUserService = sharedUserService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody SharedUserDTO sharedUserDTO) {
        if(sharedUserService.addUser(sharedUserDTO)) {
            return ResponseEntity.ok("Add User Success");
        } else {
            return ResponseEntity.status(400).body("Add User Failed");
        }
    }

    @DeleteMapping("/{userid}/{valid1}/delete")
    public ResponseEntity<?> delete(@PathVariable Long userid, @PathVariable Long valid1) {
        SharedUserDTO sharedUserDTO = new SharedUserDTO();
        sharedUserDTO.setSharedUserId(userid);
        sharedUserDTO.setSharedCalenderId(valid1);
        if(sharedUserService.removeUser(sharedUserDTO)) {
            return ResponseEntity.ok("Delete User Success");
        } else {
            return ResponseEntity.status(400).body("Delete User Failed");
        }
    }

}
