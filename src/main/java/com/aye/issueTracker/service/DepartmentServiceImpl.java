package com.aye.issueTracker.service;

import com.aye.issueTracker.exception.InvalidDepartmentDataException;
import com.aye.issueTracker.exception.NotDeletableException;
import com.aye.issueTracker.exception.ResourceNotFoundException;
import com.aye.issueTracker.model.Department;
import com.aye.issueTracker.model.User;
import com.aye.issueTracker.repository.DepartmentRepository;
import com.aye.issueTracker.repository.EmployeeRepository;
import com.aye.issueTracker.repository.TicketRepository;
import com.aye.issueTracker.repository.UserRepository;
import com.aye.issuetrackerdto.entityDto.DepartmentDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService{

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<DepartmentDto> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();

        return departments.stream()
                .map(this::converToDto)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDto getDepartmentById(Long id) {
        Optional<Department> department = departmentRepository.findById(id);
        if(department.isPresent()) {
            DepartmentDto departmentDto = converToDto(department.get());
            return departmentDto;
        }
        else {
            throw new ResourceNotFoundException("Department Not found With this id: "+id);
        }
    }

    @Override
    public DepartmentDto createDepartment(DepartmentDto departmentDto){

        validDepartmentDto(departmentDto);



        departmentDto.setId(sequenceGeneratorService.generateSequence(Department.SEQUENCE_NAME));
        Department department = convertToEntity(departmentDto);

        String createdByUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long createdByUserId = userRepository.findByUsername(createdByUserName).get().getId();
        department.setCreatedById(createdByUserId);
        department.setCreatedDateTime(LocalDateTime.now());
        department = departmentRepository.save(department);
        return converToDto(department);
    }

    @Override
    public void deleteDepartment(Long id) throws ResourceNotFoundException {
        Optional<Department> department = departmentRepository.findById(id);

        if(department.isPresent()){
            Boolean employeeExist = employeeRepository.existsByDepartment(department.get());
            Boolean ticketExist = ticketRepository.existsByDepartment(department.get());

            if (employeeExist || ticketExist) {
                throw new NotDeletableException("Delete operation not possible! Associate Entity Exist!");
            }

            departmentRepository.deleteById(id);
        }
        else {
            throw new ResourceNotFoundException("Department not found with this id");
        }

    }

    @Override
    public DepartmentDto updateDepartment(DepartmentDto departmentDto, Long id) throws ResourceNotFoundException{

//        validDepartmentDto(departmentDto);
        Optional<Department> department = departmentRepository.findById(id);

        if(department.isPresent()){

            if(departmentDto.getName() != null){

                Optional<Department> createdDepartment = departmentRepository.findByName(departmentDto.getName());
                if (createdDepartment.isPresent()) {
                    if (createdDepartment.get().getId() != departmentDto.getId()) {
                        throw new InvalidDepartmentDataException("Department Name Already Exist!");
                    }
                }

                if(departmentDto.getName() == null || departmentDto.getName().isEmpty()){
                    throw new InvalidDepartmentDataException("Department Name Can't be empty");
                }

                department.get().setName(departmentDto.getName());
                department.get().setDescription(departmentDto.getDescription());
                String currentUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Long currentUserId = userRepository.findByUsername(currentUserName).get().getId();
                department.get().setUpdatedById(currentUserId);
                department.get().setUpdatedDateTime(LocalDateTime.now());

                Department updatedDepartment = departmentRepository.save(department.get());
                return modelMapper.map(updatedDepartment, DepartmentDto.class);
            }
            else {
                department.get().setDescription(departmentDto.getDescription());
                Department updatedDepartment = departmentRepository.save(department.get());
                return modelMapper.map(updatedDepartment, DepartmentDto.class);
            }
        }
        else {
            throw new ResourceNotFoundException("No department found with this id: "+id);
        }

    }

    private DepartmentDto converToDto(Department department){
        return modelMapper.map(department, DepartmentDto.class);
    }

    private Department convertToEntity(DepartmentDto departmentDto){
        return modelMapper.map(departmentDto, Department.class);
    }

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void validDepartmentDto(DepartmentDto departmentDto) throws InvalidDepartmentDataException
    {
        if(departmentDto == null){
            throw new InvalidDepartmentDataException("Invalid Request Object");
        }

        if(departmentDto.getName() == null || departmentDto.getName().isEmpty()){
            throw new InvalidDepartmentDataException("Department Name Can't be empty");
        }

        if(departmentDto.getDescription() == null || departmentDto.getDescription().isEmpty() ){
            throw new InvalidDepartmentDataException("Department Description Can't Be empty");
        }

        Optional<Department> createdDepartment = departmentRepository.findByName(departmentDto.getName());
        if (createdDepartment.isPresent()){
            throw new InvalidDepartmentDataException("Department Name Already Exist!");
        }

    }


}
