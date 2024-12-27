package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SharedCalender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CALENDER_ID")
    private Long id;


    @JoinColumn(name = "ADMIN_USER_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Users adminUser;

    @Column(nullable = false, length = 20)
    private String calenderName;

    // 공유 캘린더의 기간 범위
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;


    // 공유 캘린더의 참여자 정보, 공유 캘린더 내의 일정 정보는 공유 캘린더가 사라지면 같이 지운다.
    @OneToMany(mappedBy = "sharedCalender", cascade = CascadeType.REMOVE)
    private List<SharedUser> sharedUsers = new ArrayList<>();
    @OneToMany(mappedBy = "sharedCalender", cascade = CascadeType.REMOVE)
    private List<ScheduleData> scheduleData = new ArrayList<>(); // 공유 캘린더 내부의 모든 스케줄 데이터 리스트


    public Long getId() {
        return id;
    }

    public Users getAdminUserId() {
        return adminUser;
    }

    public String getCalenderName() {
        return calenderName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setAdminUser(Users adminUser) { // 관리자(생성자) 유저 정보 외래키 입력, 동시에 양방향 매핑
        this.adminUser = adminUser;
        adminUser.getMySharedCalenders().add(this);
    }

    public List<SharedUser> getSharedUsers() {
        return sharedUsers;
    }

    public List<ScheduleData> getScheduleData() {
        return scheduleData;
    }

    public void setCalenderName(String calenderName) {
        this.calenderName = calenderName;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
