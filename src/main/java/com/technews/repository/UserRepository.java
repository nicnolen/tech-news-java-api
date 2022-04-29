package com.technews.repository;

import com.technews.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// specify an integer to make sure the repository can take User and the id of that user
public interface UserRepository extends JpaRepository<User, Integer> {
    // find a user by email
    User.findUserByEmail(String email) throws Exception;
}
