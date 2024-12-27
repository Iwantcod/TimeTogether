package com.example.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(nullable = false, length = 20)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false, length = 30)
    private String userNickname;
    private int userAge;
    private int userSex; // 남자: 0, 여자: 1

    @Column(nullable = false, length = 13)
    private String userPhone;

    @OneToMany(mappedBy = "adminUser", cascade = CascadeType.REMOVE) // 유저 제거 시, 생성한 공유 캘린더는 놔둔다. adminUser는 참여자 중 랜덤하게 선택
    private List<SharedCalender> mySharedCalenders = new ArrayList<>(); // 본인이 '생성한' 공유캘린더의 목록

    @OneToMany(mappedBy = "sharedUser", cascade = CascadeType.REMOVE)
    private List<SharedUser> joinedCalenders = new ArrayList<>(); // 본인이 '참여한' 공유캘린더의 목록

    @OneToMany(mappedBy = "users", cascade = CascadeType.REMOVE)
    private List<ScheduleData> myScheduleData = new ArrayList<>(); // 본인이 생성한 '공유캘린더 내부의 일정'

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public int getUserAge() {
        return userAge;
    }

    public int getUserSex() {
        return userSex;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public List<SharedCalender> getMySharedCalenders() {
        return mySharedCalenders;
    }

    public List<SharedUser> getJoinedCalenders() {
        return joinedCalenders;
    }

    public List<ScheduleData> getMyScheduleData() {
        return myScheduleData;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public void setUserSex(int userSex) {
        this.userSex = userSex;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
