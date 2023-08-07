package com.aye.issueTracker.repository;

import com.aye.issueTracker.model.Department;
import com.aye.issueTracker.model.Employee;
import com.aye.issueTracker.model.Team;
import com.aye.issueTracker.model.Ticket;
import com.aye.issuetrackerdto.entityDto.TicketDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, Long> {
    List<Ticket> findByDepartment(Long id);

    List<Ticket> findByAssignedTo(Employee assignedTo);

    List<Ticket> findByTeam(Long id);

    List<Ticket> findTicketByCreatedBy(Employee employee);

    Boolean existsByDepartment(Department department);

    Boolean existsByTeam(Team team);

    Boolean existsByAssignedTo(Employee employee);

    Boolean existsByAssignedBy(Employee employee);

    Boolean existsByCreatedBy(Employee employee);
}
