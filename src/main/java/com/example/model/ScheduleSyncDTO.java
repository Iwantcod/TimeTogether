package com.example.model;

import java.util.List;

public class ScheduleSyncDTO {
    private String userNickname;
    private Long sharedCalenderId;

    // 여러 개의 일정을 받는데, 그 중 중복되지 않는 부분은 '시작날짜', '종료날짜', '일정 내용'이므로 이 부분만 따로 클래스화하여 리스트로 만든다.
    private List<ScheduleInfo> scheduleInfo;

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

    public List<ScheduleInfo> getScheduleInfo() {
        return scheduleInfo;
    }

    public void setScheduleInfo(List<ScheduleInfo> scheduleInfo) {
        this.scheduleInfo = scheduleInfo;
    }
}
