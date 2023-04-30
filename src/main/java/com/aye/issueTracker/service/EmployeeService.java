package com.aye.issueTracker.service;

import com.aye.issueTracker.exception.InvalidRequestDataException;
import com.aye.issuetrackerdto.entityDto.EmployeeDto;

import java.util.List;

public interface EmployeeService {


    EmployeeDto getEmployeeById(Long id);

    EmployeeDto createEmployee(EmployeeDto employeeDto) throws InvalidRequestDataException;

    EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto);

    boolean deleteEmployee(Long id);

    List<EmployeeDto> getAllEmployee();

    EmployeeDto getTeamHead(Long teamId, Boolean isTeamHead);

    EmployeeDto getDepartmentHead(Long departmentId, boolean isDeptHead);
}
