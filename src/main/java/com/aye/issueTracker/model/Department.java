package com.aye.issueTracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "DEPARTMENT")
public class Department {

    public static final String SEQUENCE_NAME = "department_sequence";
    @Id
    private Long id;
    private String name;
    private  String description;

    private Long createdById;

    private LocalDateTime createdDateTime;

    private Long updatedById;

    private LocalDateTime updatedDateTime;

    public Department() {
    }

    public Department(Long id, String name, String description, Long createdById, LocalDateTime createdDateTime, Long updatedById, LocalDateTime updatedDateTime) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdById=" + createdById +
                ", createdDateTime=" + createdDateTime +
                ", updatedById=" + updatedById +
                ", updatedDateTime=" + updatedDateTime +
                '}';
    }
}
