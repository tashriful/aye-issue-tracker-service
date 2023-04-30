package com.aye.issueTracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "DEPARTMENT_HEAD")
public class DepartmentHead {

    public static final String SEQUENCE_NAME = "departmentHead_sequence";

    @Id
    private Long id;
    @DBRef
    private User user;
    @DBRef
    private  Department department;

    public DepartmentHead() {
    }

    public DepartmentHead(Long id, User user, Department department) {
        this.id = id;
        this.user = user;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User name) {
        this.user = name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "DepartmentHead{" +
                "id=" + id +
                ", name=" + user.getName() +
                ", department=" + department +
                '}';
    }
}
