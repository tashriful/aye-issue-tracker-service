package com.aye.issueTracker.repository;

import com.aye.issueTracker.model.TeamHead;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamHeadRepository extends MongoRepository<TeamHead, Long> {
}
