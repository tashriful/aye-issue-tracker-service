package com.aye.issueTracker.service;

import com.aye.issueTracker.exception.InvalidDepartmentDataException;
import com.aye.issueTracker.exception.InvalidRequestDataException;
import com.aye.issueTracker.exception.ResourceNotFoundException;
import com.aye.issueTracker.model.Department;
import com.aye.issueTracker.model.Employee;
import com.aye.issueTracker.model.Team;
import com.aye.issueTracker.model.User;
import com.aye.issueTracker.repository.EmployeeRepository;
import com.aye.issuetrackerdto.entityDto.DepartmentDto;
import com.aye.issuetrackerdto.entityDto.EmployeeDto;
import com.aye.issuetrackerdto.entityDto.TeamDto;
import com.aye.issuetrackerdto.entityDto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        List<Employee> employees = employeeRepository.findByUser(modelMapper.map(userDto, User.class));

        if (employees.size() != 0) {
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
            List<Employee> employees = employeeRepository.findByUser(modelMapper.map(userDto, User.class));


            if(employees.size() != 0){

                if(!employees.stream().findFirst().get().getId().equals(employee.getId())) {
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
}
