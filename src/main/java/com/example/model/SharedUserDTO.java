package com.example.model;

public class SharedUserDTO {
    private Long id;
    private Long sharedUserId;
    private Long sharedCalenderId;


    public SharedUserDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public Long getSharedUserId() {
        return sharedUserId;
    }

    public void setSharedUserId(Long sharedUserId) {
        this.sharedUserId = sharedUserId;
    }

    public Long getSharedCalenderId() {
        return sharedCalenderId;
    }

    public void setSharedCalenderId(Long sharedCalenderId) {
        this.sharedCalenderId = sharedCalenderId;
    }
}
