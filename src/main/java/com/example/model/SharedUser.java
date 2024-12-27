package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "twoColumnsUnique",
                columnNames = {"USER_ID", "CALENDER_ID"}
        )})
public class SharedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SHARED_USER_ID")
    private Long id;

    @JoinColumn(name = "USER_ID", nullable = false) @ManyToOne(fetch = FetchType.LAZY)
    private Users sharedUser;


    @JoinColumn(name = "CALENDER_ID", nullable = false) @ManyToOne(fetch = FetchType.LAZY)
    private SharedCalender sharedCalender;


    public SharedUser(Users sharedUser, SharedCalender sharedCalender) {
        this.sharedUser = sharedUser;
        this.sharedCalender = sharedCalender;
    }

    public SharedUser() {
    }

    public Long getId() {
        return id;
    }

    public Users getSharedUser() {
        return sharedUser;
    }

    public SharedCalender getSharedCalender() {
        return sharedCalender;
    }

    public void setSharedUser(Users sharedUser) {
        this.sharedUser = sharedUser;
    }

    public void setSharedCalender(SharedCalender sharedCalender) {
        this.sharedCalender = sharedCalender;
    }
}
