package com.aye.issueTracker.service;

import com.aye.issueTracker.exception.ResourceNotFoundException;
import com.aye.issuetrackerdto.entityDto.DepartmentDto;
import com.aye.issuetrackerdto.entityDto.DepartmentHeadDto;

import java.util.List;

public interface DepartmentHeadService {

    DepartmentHeadDto createDepartmentHead(DepartmentHeadDto departmentHeadDto);

    DepartmentHeadDto getDepartmentHeadById(Long id) throws ResourceNotFoundException;

    List<DepartmentHeadDto> getAllDepartmentHeads();

    List<DepartmentHeadDto> getDepartmentHeadsByDepartment(Long departmentId);

    void deleteDepartmentHead(Long id) throws ResourceNotFoundException;

    boolean validDepartmentDto(DepartmentDto departmentDto);
}
