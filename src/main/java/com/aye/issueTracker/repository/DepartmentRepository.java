package com.aye.issueTracker.repository;

import com.aye.issueTracker.model.Department;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends MongoRepository<Department, Long> {

    Optional<List<Department>> findByName (String name);
}
