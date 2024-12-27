package com.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class ScheduleDataDTO {
    private Long id;
    private String userNickname;
    private Long sharedCalenderId;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;


    public ScheduleDataDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public Long getSharedCalenderId() {
        return sharedCalenderId;
    }

    public void setSharedCalenderId(Long sharedCalenderId) {
        this.sharedCalenderId = sharedCalenderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
