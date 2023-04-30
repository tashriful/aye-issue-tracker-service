package com.aye.issueTracker.repository;

import com.aye.issueTracker.model.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends MongoRepository<Team, Long> {
    Optional<List<Team>> findByName(String name);
}
