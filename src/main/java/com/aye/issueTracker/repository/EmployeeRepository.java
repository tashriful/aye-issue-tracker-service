package com.aye.issueTracker.repository;

import com.aye.issueTracker.model.Department;
import com.aye.issueTracker.model.Employee;
import com.aye.issueTracker.model.Team;
import com.aye.issueTracker.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, Long> {

//    List<Employee> findByUser(User user);

    Employee findByUser(User user);
    Employee findByTeamAndIsTeamHead(Team team, Boolean status);

    Employee findByDepartmentAndIsDeptHead(Department department, boolean isDeptHead);

    List<Employee> findEmployeesByDepartment(Department department);

    Optional<Employee> findEmployeeByUser(User user);

    Boolean existsByDepartment(Department department);

    Boolean existsByTeam(Team team);
}
