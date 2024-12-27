package com.example.model;

public class UsersDTO {
    private Long id;
    private String userName;
    private String password;
    private String userNickname;
    private int userAge;
    private int userSex;
    private String userPhone;



    public UsersDTO() {}

    public UsersDTO(Long id, String userName, String password, String userNickname, int userAge, int userSex, String userPhone){
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.userNickname = userNickname;
        this.userAge = userAge;
        this.userSex = userSex;
        this.userPhone = userPhone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public int getUserSex() {
        return userSex;
    }

    public void setUserSex(int userSex) {
        this.userSex = userSex;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
