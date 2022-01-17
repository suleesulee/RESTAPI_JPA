package com.example.jpa.user.repository;

import com.example.jpa.notice.model.UserStatus;
import com.example.jpa.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    int countByEmail(String email);
    Optional<User> findByIdAndPassword(Long id, String password);

    Optional<User> findByUserNameAndPhone(String userName, String phone);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndUserName(String email, String userName);

    List<User> findByEmailContainsOrPhoneContainsOrUserNameContains(String email, String phone, String userName);

    long countByStatus(UserStatus userStatus);

    @Query(" select u.* from User u where u.regDate between :startDate and :endDate ")
    List<User> findToday(LocalDateTime startDate, LocalDateTime endDate);
}
