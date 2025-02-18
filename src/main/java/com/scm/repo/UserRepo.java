package com.scm.repo;

//repository are use to interact with data base.


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scm.entities.User;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
    //extra methods db related operations
    // custom query methods
    // custom finder methods
    
    Optional<User> findByEmail(String Email);
    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findByEmailToken(String id);

}
