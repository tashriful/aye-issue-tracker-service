package com.aye.issueTracker.service;

import com.aye.issueTracker.exception.ResourceNotFoundException;
import com.aye.issueTracker.model.Department;
import com.aye.issueTracker.model.DepartmentHead;
import com.aye.issueTracker.model.User;
import com.aye.issueTracker.repository.DepartmentHeadRepository;
import com.aye.issuetrackerdto.entityDto.DepartmentDto;
import com.aye.issuetrackerdto.entityDto.DepartmentHeadDto;
import com.aye.issuetrackerdto.entityDto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentHeadServiceImpl implements DepartmentHeadService{

    @Autowired
    private DepartmentHeadRepository departmentHeadRepository;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @Override
    public DepartmentHeadDto createDepartmentHead(DepartmentHeadDto departmentHeadDto) {
        DepartmentDto departmentDto = departmentService.getDepartmentById(departmentHeadDto.getDepartmentId());

        UserDto userDto = userService.getUserById(departmentHeadDto.getUserId());

        DepartmentHead departmentHead = new DepartmentHead();
        departmentHead.setId(departmentHeadDto.getId());
        departmentHead.setUser(modelMapper.map(userDto, User.class));
        departmentHead.setDepartment(modelMapper.map(departmentDto, Department.class));

        DepartmentHead departmentHeadData = departmentHeadRepository.save(departmentHead);
        return this.converToDto(departmentHeadData);

    }

    @Override
    public DepartmentHeadDto getDepartmentHeadById(Long id) throws ResourceNotFoundException {
        Optional<DepartmentHead> departmentHead = departmentHeadRepository.findById(id);
//        System.out.println(departmentHead.get().getName());
        if (!departmentHead.isPresent()) {
            throw new ResourceNotFoundException("Department Head not found with id: " + id);
        }
        return this.converToDto(departmentHead.get());
    }

    @Override
    public List<DepartmentHeadDto> getAllDepartmentHeads() {
        return departmentHeadRepository.findAll().stream().map(this::converToDto).collect(Collectors.toList());
    }

    @Override
    public List<DepartmentHeadDto> getDepartmentHeadsByDepartment(Long departmentId) {
        DepartmentDto departmentDto = departmentService.getDepartmentById(departmentId);

        List<DepartmentHead> departmentHeads =
                departmentHeadRepository.findByDepartment(modelMapper.map(departmentDto, Department.class));

        return departmentHeads.stream().map(this::converToDto).collect(Collectors.toList());
    }

    @Override
    public void deleteDepartmentHead(Long id) throws ResourceNotFoundException {
        DepartmentHeadDto departmentHeadDto = getDepartmentHeadById(id);

        if(departmentHeadDto == null) {
            throw new ResourceNotFoundException("Department Head not found for this id: " + id);
        }

        departmentHeadRepository.delete(this.convertToEntity(departmentHeadDto));

    }

    private DepartmentHeadDto converToDto(DepartmentHead departmentHead){
        return modelMapper.map(departmentHead, DepartmentHeadDto.class);
    }


    private DepartmentHead convertToEntity(DepartmentHeadDto departmentHeadDto){
        return modelMapper.map(departmentHeadDto, DepartmentHead.class);
    }

    @Override
    public boolean validDepartmentDto(DepartmentDto departmentDto) {
        if(departmentDto == null){
            return false;
        }

        if(departmentDto.getName() == null || departmentDto.getName().isEmpty() || !departmentDto.getName().matches("[a-zA-Z]+")){
            return false;
        }

        if(departmentDto.getDescription() == null || departmentDto.getDescription().isEmpty() || !departmentDto.getDescription().matches("[a-zA-Z]+")){
            return false;
        }

        return true;


    }
}
