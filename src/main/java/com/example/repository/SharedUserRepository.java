package com.example.repository;

import com.example.model.SharedCalender;
import com.example.model.SharedUser;
import com.example.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SharedUserRepository extends JpaRepository<SharedUser, Long> {
    List<SharedUser> findBySharedUser(Users users);
    List<SharedUser> findBySharedCalender(SharedCalender sharedCalender);
    Optional<SharedUser> findBySharedUserAndSharedCalender(Users users, SharedCalender sharedCalender);

    void deleteBySharedUserAndSharedCalender(Users sharedUser, SharedCalender sharedCalender);
}
