package com.aye.issueTracker.repository;

import com.aye.issueTracker.model.Department;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepository extends MongoRepository<Department, Long> {
}
