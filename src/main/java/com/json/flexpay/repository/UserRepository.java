package com.json.flexpay.repository;


import com.json.flexpay.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {


    User findByUsernameIgnoreCase(String username);
    Optional<User> findByUsername(String username);


}
