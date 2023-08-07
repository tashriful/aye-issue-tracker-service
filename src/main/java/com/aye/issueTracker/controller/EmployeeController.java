package com.aye.issueTracker.controller;

import com.aye.issueTracker.common.CustomErrorResponse;
import com.aye.issueTracker.exception.InvalidRequestDataException;
import com.aye.issueTracker.exception.NotDeletableException;
import com.aye.issueTracker.exception.ResourceNotFoundException;
import com.aye.issueTracker.service.EmployeeService;
import com.aye.issuetrackerdto.entityDto.EmployeeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {



    @Autowired
    private EmployeeService employeeService;


    @GetMapping("/")
    public ResponseEntity<?> getAllEmployee() {
       List<EmployeeDto> employeeDtoList = employeeService.getAllEmployee();
        return new ResponseEntity<>(employeeDtoList, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        EmployeeDto employeeDto = employeeService.getEmployeeById(id);
        if (employeeDto != null) {
            return new ResponseEntity<>(employeeDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/submit")
    public ResponseEntity<?> createEmployee( @RequestBody EmployeeDto employeeDto) {

        try {
            EmployeeDto createdEmployeeDto = employeeService.createEmployee(employeeDto);
            return new ResponseEntity<>(createdEmployeeDto, HttpStatus.OK);
        }
        catch (InvalidRequestDataException | ResourceNotFoundException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal Server Error :- "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDto employeeDto) {

        try {
            EmployeeDto updatedEmployee = employeeService.updateEmployee(id, employeeDto);
            return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
        }
        catch (InvalidRequestDataException | ResourceNotFoundException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal Server Error :- "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        try {
            boolean deleted = employeeService.deleteEmployee(id);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(new CustomErrorResponse("Employee not found with this id: "+id, HttpStatus.NOT_FOUND, ZonedDateTime.now()), HttpStatus.NOT_FOUND);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND, ZonedDateTime.now()), HttpStatus.NOT_FOUND);
        }
        catch (NotDeletableException e3) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot delete department with associated entity");
        }
        catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal Server Error :- " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }

    @GetMapping("/department/{id}")
    public ResponseEntity<?> getEmployeesByDepartment(@PathVariable("id") Long id){
        try {
            List<EmployeeDto> employeeDtos = employeeService.getEmployeeByDept(id);
            return new ResponseEntity<>(employeeDtos, HttpStatus.OK);
        }
        catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND, ZonedDateTime.now()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal Server Error :- "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/username/{name}")
    public ResponseEntity<?> getEmployeesByUserName(@PathVariable("name") String username){
        try {
                EmployeeDto employeeDtos= employeeService.getEmployeesByUserName(username);
            return new ResponseEntity<>(employeeDtos, HttpStatus.OK);
        }
        catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new CustomErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND, ZonedDateTime.now()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomErrorResponse("Internal Server Error :- "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
