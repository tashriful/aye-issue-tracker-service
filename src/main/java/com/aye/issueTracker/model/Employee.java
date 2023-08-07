package com.aye.issueTracker.model;

import com.mongodb.lang.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "EMPLOYEE")
public class Employee {

    @Transient
    public static final String SEQUENCE_NAME = "employee_sequence";

    @Id
    private Long id;
    private String name;
    private String designation;
    private String address;

    @DBRef
    @Indexed(unique = true)
    private User user;

    @DBRef
    private Department department;

    private Boolean isDeptHead;

    @Nullable
    @DBRef
    private Team team;

    @Nullable
    private Boolean isTeamHead;

    private Long createdById;

    private LocalDateTime createdDateTime;

    private Long updatedById;

    private LocalDateTime updatedDateTime;

    public Employee() {
    }

    public Employee(Long id, String name, String designation, String address, User user, Department department, Boolean isDeptHead, @Nullable Team team, @Nullable Boolean isTeamHead, Long createdById, LocalDateTime createdDateTime, Long updatedById, LocalDateTime updatedDateTime) {
        this.id = id;
        this.name = name;
        this.designation = designation;
        this.address = address;
        this.user = user;
        this.department = department;
        this.isDeptHead = isDeptHead;
        this.team = team;
        this.isTeamHead = isTeamHead;
        this.createdById = createdById;
        this.createdDateTime = createdDateTime;
        this.updatedById = updatedById;
        this.updatedDateTime = updatedDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Boolean getDeptHead() {
        return isDeptHead;
    }

    public void setDeptHead(Boolean deptHead) {
        isDeptHead = deptHead;
    }

    @Nullable
    public Team getTeam() {
        return team;
    }

    public void setTeam(@Nullable Team team) {
        this.team = team;
    }

    @Nullable
    public Boolean getTeamHead() {
        return isTeamHead;
    }

    public void setTeamHead(@Nullable Boolean teamHead) {
        isTeamHead = teamHead;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Long getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(Long updatedById) {
        this.updatedById = updatedById;
    }

    public LocalDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(LocalDateTime updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", designation='" + designation + '\'' +
                ", address='" + address + '\'' +
                ", user=" + user +
                ", department=" + department +
                ", isDeptHead=" + isDeptHead +
                ", team=" + team +
                ", isTeamHead=" + isTeamHead +
                ", createdById=" + createdById +
                ", createdDateTime=" + createdDateTime +
                ", updatedById=" + updatedById +
                ", updatedDateTime=" + updatedDateTime +
                '}';
    }
}
