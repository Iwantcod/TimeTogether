package com.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class ScheduleInfo {
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public ScheduleInfo() {}
    public ScheduleInfo(String content, LocalDate startDate, LocalDate endDate) {
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
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
