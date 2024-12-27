package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class ScheduleData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCHEDULE_ID")
    private Long id;

    // Users의 userName 컬럼을 참조하는 외래키
    @JoinColumn(name = "USER_NICKNAME", referencedColumnName = "userNickname")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    @JoinColumn(name = "CALENDER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private SharedCalender sharedCalender;

    @Column(columnDefinition = "TEXT") // 해당 컬럼의 자료형은 'TEXT'이다.
    private String content; // 일정의 내용

    // 공유 캘린더 내부의 각 일정의 기간
    private LocalDate startDate;
    private LocalDate endDate;

    public Long getId() {
        return id;
    }

    public Users getUsers() {
        return users;
    }

    public SharedCalender getSharedCalender() {
        return sharedCalender;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setUsers(Users users) {
        this.users = users;
        users.getMyScheduleData().add(this);
    }

    public void setSharedCalender(SharedCalender sharedCalender) {
        this.sharedCalender = sharedCalender;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}