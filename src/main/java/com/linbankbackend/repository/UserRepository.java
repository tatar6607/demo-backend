package com.linbankbackend.repository;

import com.linbankbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySsn(String ssn);
    Boolean existsByEmail(String email);
    Boolean existsUserBySsn(String ssn);
}
