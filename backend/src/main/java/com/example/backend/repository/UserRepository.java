package com.example.backend.repository;

import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(Long userId);
    User findByUserName(String userName);
    Optional<User> findByEmail(String email);
    //User findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.userName = :newUserName, u.phoneNumber = :newPhoneNumber WHERE u.userId = :userId")
    void updateUser(@Param("userId") Long userId,
                    @Param("newUserName") String newUserName,
                    @Param("newPhoneNumber") String newEmail);

    @Modifying
    @Query("DELETE FROM User u WHERE u.email = :email")
    void deleteUser(@Param("email") String email);
}