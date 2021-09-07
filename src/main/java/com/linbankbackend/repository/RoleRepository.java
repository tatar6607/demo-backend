package com.linbankbackend.repository;

import com.linbankbackend.model.ERoles;
import com.linbankbackend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByName(ERoles name);
}
