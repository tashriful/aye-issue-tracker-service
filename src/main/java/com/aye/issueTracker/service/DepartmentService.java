package com.aye.issueTracker.service;

import com.aye.issuetrackerdto.entityDto.DepartmentDto;

import java.util.List;

public interface DepartmentService {

    List<DepartmentDto> getAllDepartments();

    DepartmentDto getDepartmentById(Long id);

    DepartmentDto createDepartment(DepartmentDto departmentDto);

    void deleteDepartment(Long id);


    void validDepartmentDto(DepartmentDto departmentDto);

    DepartmentDto updateDepartment(DepartmentDto departmentDto, Long id);
}
