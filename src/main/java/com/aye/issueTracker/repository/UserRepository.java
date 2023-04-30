package com.aye.issueTracker.repository;

import com.aye.issueTracker.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, Long> {
    List<User> findAllByUsername(String username);

    Optional<User> findByUsername(String username);
}
