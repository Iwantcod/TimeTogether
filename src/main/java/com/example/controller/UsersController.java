package com.example.controller;

import com.example.model.UsersDTO;
import com.example.service.SharedCalenderService;
import com.example.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UsersController {

    private final UserService userService;
    private final SharedCalenderService sharedCalenderService;

    public UsersController(UserService userService, SharedCalenderService sharedCalenderService) {
        this.userService = userService;
        this.sharedCalenderService = sharedCalenderService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsersDTO usersDTO, HttpSession session) {
        // userService의 login 메서드를 통해 인자로 넘긴 usersDTO의 유저닉네임 존재여부와 비밀번호 일치여부 검증
        // 로그인 성공 시 해당 유저의 식별자를 포함한 DTO를 반환. 없으면 null 반환
        if (userService.login(usersDTO) != null) {
            UsersDTO loginedUser = userService.login(usersDTO);
            // 로그인을 했는데 calenderId 라는 세션이 있다는 것 = 초대링크를 클릭하여 들어왔다는 것
            if(session.getAttribute("calenderId") != null) {
                if(userService.processInvite(usersDTO, session)) {
                    return ResponseEntity.ok(loginedUser);
                } else {
                    return ResponseEntity.status(401).body("Invite Failed");
                }
            }

            return ResponseEntity.ok(loginedUser);
        } else {
            return ResponseEntity.status(401).body("Invalid Password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsersDTO usersDTO) {
        // 유저 닉네임 중복여부 검증
        if (userService.register(usersDTO)) {
            return ResponseEntity.ok("Registration Successful");
        } else {
            return ResponseEntity.status(400).body("Nickname already exists");
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if(userService.delete(id)) {
            return ResponseEntity.ok("Delete Successful");
        } else {
            return ResponseEntity.status(400).body("User Not Found");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody UsersDTO usersDTO) {
        if(userService.update(usersDTO)) {
            return ResponseEntity.ok("Update Successful");
        } else {
            return ResponseEntity.status(400).body("User Not Found");
        }
    }

    @GetMapping("/{id}/info")
    public ResponseEntity<?> getInfo(@PathVariable Long id) {
        if(userService.userInfo(id) != null) {
            // return ResponseEntity.status(200).body(usersDTO);
            return ResponseEntity.ok(userService.userInfo(id));
        } else {
            return ResponseEntity.status(400).body("User Not Found");
        }
    }

    // 자기가 참여되어있는 캘린더 목록 조회
    @GetMapping("/{userid}/cal-list")
    public ResponseEntity<?> getCalenderList(@PathVariable Long userid) {
        if(userService.myCalenderList(userid) != null) {
            return ResponseEntity.ok(userService.myCalenderList(userid));
        } else {
            return ResponseEntity.status(400).body("Calender Not Found");
        }
    }


}
