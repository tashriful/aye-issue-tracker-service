package com.aye.issueTracker.repository;

import com.aye.issueTracker.model.Department;
import com.aye.issueTracker.model.DepartmentHead;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DepartmentHeadRepository extends MongoRepository<DepartmentHead, Long> {
    List<DepartmentHead> findByDepartment(Department department);
}
