package com.aye.issueTracker.service;

import com.aye.issueTracker.exception.InvalidDepartmentDataException;
import com.aye.issueTracker.exception.InvalidRequestDataException;
import com.aye.issueTracker.exception.NotDeletableException;
import com.aye.issueTracker.exception.ResourceNotFoundException;
import com.aye.issueTracker.model.Department;
import com.aye.issueTracker.model.Employee;
import com.aye.issueTracker.model.Team;
import com.aye.issueTracker.model.User;
import com.aye.issueTracker.repository.EmployeeRepository;
import com.aye.issueTracker.repository.TicketRepository;
import com.aye.issueTracker.repository.UserRepository;
import com.aye.issuetrackerdto.entityDto.DepartmentDto;
import com.aye.issuetrackerdto.entityDto.EmployeeDto;
import com.aye.issuetrackerdto.entityDto.TeamDto;
import com.aye.issuetrackerdto.entityDto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public EmployeeDto getEmployeeById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            return convertToDto(employee.get());
        }
        else {
            throw new ResourceNotFoundException("Employee Not found With This id: "+id);
        }
    }

    @Override
    public EmployeeDto getEmployeesByUserName(String username) {
        User user = userService.getUserIdByUserName(username);
        if (user != null){
            Optional<Employee> employee = employeeRepository.findEmployeeByUser(user);
            if (employee.isPresent()){
                return modelMapper.map(employee.get(), EmployeeDto.class);
            }
            else {
                throw new ResourceNotFoundException("Employee not found with this username!");
            }
        }
        else {
            return null;
        }
    }

    @Override
    public List<EmployeeDto> getAllEmployee() {
        return employeeRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) throws InvalidRequestDataException{
        validateEmployee(employeeDto);

        Employee employee = new Employee();

        UserDto userDto = userService.getUserById(employeeDto.getUserId());

        employee.setId(sequenceGeneratorService.generateSequence(Employee.SEQUENCE_NAME));
        employee.setAddress(employeeDto.getAddress());
        employee.setName(employeeDto.getName());
        employee.setDesignation(employeeDto.getDesignation());
        employee.setUser(modelMapper.map(userDto, User.class));

        if(employeeDto.getDepartmentId() != null){
            DepartmentDto departmentDto = departmentService.getDepartmentById(employeeDto.getDepartmentId());
            if(departmentDto != null){
                employee.setDepartment(modelMapper.map(departmentDto, Department.class));
            }
            else {
                employee.setDepartment(null);
            }
        }
        if(employeeDto.getTeamId() != null){
            Optional<TeamDto> teamDto = teamService.getTeamById(employeeDto.getTeamId());
            if(teamDto.isPresent()){
                employee.setTeam(modelMapper.map(teamDto, Team.class));
            }
            else {
                employee.setTeam(null);
            }
        }

        if(employeeDto.getDeptHead() != null){
            employee.setDeptHead(employeeDto.getDeptHead());
        }
        if(employeeDto.getTeamHead() != null){
            employee.setTeamHead(employeeDto.getTeamHead());
        }

        String currentUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId = userRepository.findByUsername(currentUserName).get().getId();
        employee.setCreatedById(currentUserId);
        employee.setCreatedDateTime(LocalDateTime.now());

        Employee createdEmployee = employeeRepository.save(employee);
        return convertToDto(createdEmployee);


    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {

        validateUpdateEmployee(employeeDto);

        Optional<Employee> pEmployee = employeeRepository.findById(id);


        if (pEmployee.isPresent()) {

            Employee existingEmployee = pEmployee.get();

            UserDto userDto = userService.getUserById(employeeDto.getUserId());
            if (employeeDto.getTeamId() != null) {
                Optional<TeamDto> teamDto = teamService.getTeamById(employeeDto.getTeamId());
                existingEmployee.setTeam(modelMapper.map(teamDto, Team.class));
            }
            if (employeeDto.getDepartmentId() != null) {
                DepartmentDto departmentDto = departmentService.getDepartmentById(employeeDto.getDepartmentId());
                existingEmployee.setDepartment(modelMapper.map(departmentDto, Department.class));
            }

            existingEmployee.setName(employeeDto.getName());
            existingEmployee.setDesignation(employeeDto.getDesignation());
            existingEmployee.setAddress(employeeDto.getAddress());
            existingEmployee.setUser(modelMapper.map(userDto, User.class));
            if(employeeDto.getDeptHead() != null){
                existingEmployee.setDeptHead(employeeDto.getDeptHead());
            }
            if(employeeDto.getTeamHead() != null){
                existingEmployee.setTeamHead(employeeDto.getTeamHead());
            }

            String currentUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long currentUserId = userRepository.findByUsername(currentUserName).get().getId();
            existingEmployee.setUpdatedById(currentUserId);
            existingEmployee.setUpdatedDateTime(LocalDateTime.now());

            employeeRepository.save(existingEmployee);
            return convertToDto(existingEmployee);
        }
        else {
            throw new ResourceNotFoundException("Employee Not found With This ID: "+id);
        }
    }

    @Override
    public boolean deleteEmployee(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isPresent()){
            Boolean assignedToExist = ticketRepository.existsByAssignedTo(employee.get());
            Boolean assignedByExist = ticketRepository.existsByAssignedBy(employee.get());
            Boolean createdByExist = ticketRepository.existsByCreatedBy(employee.get());

            if (assignedToExist || assignedByExist || createdByExist) {
                throw new NotDeletableException("Delete operation not possible! Associate Entity Exist!");
            }

            employeeRepository.delete(employee.get());
            return true;
        }
        else {
            return false;
        }

    }

    private EmployeeDto convertToDto(Employee employee){
        return modelMapper.map(employee, EmployeeDto.class);
    }

    private Employee convertToEntity(EmployeeDto employeeDto){
        return modelMapper.map(employeeDto, Employee.class);
    }

    @Override
    public EmployeeDto getTeamHead(Long teamId, Boolean isTeamHead) {
        Optional<TeamDto> teamDto = teamService.getTeamById(teamId);
        Team team = null;
        Employee employee = null;
        if (teamDto.isPresent()) {
            team = modelMapper.map(teamDto.get(), Team.class);
            employee = employeeRepository.findByTeamAndIsTeamHead(team, isTeamHead);
            System.out.println(employee.toString());
            return convertToDto(employee);
        }
        else{
            throw new ResourceNotFoundException("Team Head not found among employees");
        }
    }

    @Override
    public EmployeeDto getDepartmentHead(Long departmentId, boolean isDeptHead) {
        DepartmentDto departmentDto = departmentService.getDepartmentById(departmentId);
        Department department = modelMapper.map(departmentDto, Department.class);
        Employee employee = employeeRepository.findByDepartmentAndIsDeptHead(department, isDeptHead);
        if(employee != null) {
            return convertToDto(employee);
        }
        else{
            System.out.println("Department Head not found among employees");
            throw new ResourceNotFoundException("Department Head not found among employees");
        }
    }

    private void validateEmployee(EmployeeDto employee) throws InvalidRequestDataException {
        if (employee.getName() == null || employee.getName().isEmpty()) {
            throw new InvalidDepartmentDataException("Employee name is required");
        }

        if (employee.getUserId() == null) {
            throw new InvalidRequestDataException("User Id is required");
        }

        UserDto userDto = userService.getUserById(employee.getUserId());
        Employee existingEmployee = employeeRepository.findByUser(modelMapper.map(userDto, User.class));

        if (existingEmployee != null) {
            throw new InvalidRequestDataException("Employee with this user id already exist!");
        }

        if (employee.getDepartmentId() != null) {
            DepartmentDto departmentDto = departmentService.getDepartmentById(employee.getDepartmentId());
            if (departmentDto == null) {
                throw new InvalidRequestDataException("Department not found!");
            }
        }
        if (employee.getTeamId() != null) {
            Optional<TeamDto> teamDto = teamService.getTeamById(employee.getTeamId());
            if (!teamDto.isPresent()) {
                throw new InvalidRequestDataException("Team not found!");
            }
        }
    }

        private void validateUpdateEmployee(EmployeeDto employee) throws InvalidRequestDataException {
            if (employee.getName() == null || employee.getName().isEmpty()) {
                throw new InvalidDepartmentDataException("Employee name is required");
            }

            if (employee.getUserId() == null) {
                throw new InvalidRequestDataException("User Id is required");
            }

            UserDto userDto = userService.getUserById(employee.getUserId());
            Employee employee1 = employeeRepository.findByUser(modelMapper.map(userDto, User.class));


            if(employee1 != null){

                if(!employee1.getId().equals(employee.getId())) {
                    throw new InvalidRequestDataException("Employee with this user id already exist!");
                }
            }

            if(employee.getDepartmentId() != null){
                DepartmentDto departmentDto = departmentService.getDepartmentById(employee.getDepartmentId());
                if(departmentDto == null){
                    throw new InvalidRequestDataException("Department not found!");
                }
            }
            if(employee.getTeamId() != null){
                Optional<TeamDto> teamDto = teamService.getTeamById(employee.getTeamId());
                if(!teamDto.isPresent()){
                    throw new InvalidRequestDataException("Team not found!");
                }
            }



        // ... more validation rules
    }

    @Override
    public List<EmployeeDto> getEmployeeByDept(Long id) {
        DepartmentDto departmentDto = departmentService.getDepartmentById(id);
        List<Employee> employees = employeeRepository.findEmployeesByDepartment(modelMapper.map(departmentDto, Department.class));
        if (employees.size() != 0) {
            return employees.stream().map(this::convertToDto).collect(Collectors.toList());
        }
        else {
            throw new ResourceNotFoundException("Employee Not found With Theis Department ID: "+id);
        }
    }

    @Override
    public EmployeeDto getEmployeeByUser(Long createdByUserId) {
        UserDto userDto;
        try {
            userDto = userService.getUserById(createdByUserId);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("user Not found With This id: " + createdByUserId);
        }
        Optional<Employee> employee = employeeRepository.findEmployeeByUser(modelMapper.map(userDto, User.class));
        if (employee.isPresent()) {
            return convertToDto(employee.get());
        } else {
            System.out.println("Employee Not found With This id: " + createdByUserId);
            throw new ResourceNotFoundException("Employee Not found With This id: " + createdByUserId);
        }
    }
}
