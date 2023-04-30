package com.aye.issueTracker.repository;

import com.aye.issueTracker.model.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectCorrdinatorRepository extends MongoRepository<Team, Long> {
}
