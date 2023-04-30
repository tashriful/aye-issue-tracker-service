package com.aye.issueTracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "TICKET")
public class Ticket {

    public static final String SEQUENCE_NAME = "tickets_sequence";

    @Id
    private Long id;

    private String summary;

    private String description;

    private String fileId;

    @DBRef
    private Employee createdBy;

    @DBRef
    private Employee assignedBy;

    private LocalDateTime createdDateTime;

    @DBRef
    private Department department;

    @DBRef
    private Team team;

    @DBRef
    private Employee departmentHead;

    @DBRef
    private Employee teamHead;

    private String ticketType;
    @DBRef
    private Employee assignedTo;

    private String prirority;

    private String status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate targetResolutionDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate actualResolutionDate;

    private String resolutionSummary;

    public Ticket() {
    }

    public Ticket(Long id, String summary, String description, String fileId, Employee createdBy, Employee assignedBy, LocalDateTime createdDateTime, Department department, Team team, Employee departmentHead, Employee teamHead, String ticketType, Employee assignedTo, String prirority, String status, LocalDate targetResolutionDate, LocalDate actualResolutionDate, String resolutionSummary) {
        this.id = id;
        this.summary = summary;
        this.description = description;
        this.fileId = fileId;
        this.createdBy = createdBy;
        this.assignedBy = assignedBy;
        this.createdDateTime = createdDateTime;
        this.department = department;
        this.team = team;
        this.departmentHead = departmentHead;
        this.teamHead = teamHead;
        this.ticketType = ticketType;
        this.assignedTo = assignedTo;
        this.prirority = prirority;
        this.status = status;
        this.targetResolutionDate = targetResolutionDate;
        this.actualResolutionDate = actualResolutionDate;
        this.resolutionSummary = resolutionSummary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Employee getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Employee createdBy) {
        this.createdBy = createdBy;
    }

    public Employee getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(Employee assignedBy) {
        this.assignedBy = assignedBy;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Employee getDepartmentHead() {
        return departmentHead;
    }

    public void setDepartmentHead(Employee departmentHead) {
        this.departmentHead = departmentHead;
    }

    public Employee getTeamHead() {
        return teamHead;
    }

    public void setTeamHead(Employee teamHead) {
        this.teamHead = teamHead;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public Employee getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Employee assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getPrirority() {
        return prirority;
    }

    public void setPrirority(String prirority) {
        this.prirority = prirority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getTargetResolutionDate() {
        return targetResolutionDate;
    }

    public void setTargetResolutionDate(LocalDate targetResolutionDate) {
        this.targetResolutionDate = targetResolutionDate;
    }

    public LocalDate getActualResolutionDate() {
        return actualResolutionDate;
    }

    public void setActualResolutionDate(LocalDate actualResolutionDate) {
        this.actualResolutionDate = actualResolutionDate;
    }

    public String getResolutionSummary() {
        return resolutionSummary;
    }

    public void setResolutionSummary(String resolutionSummary) {
        this.resolutionSummary = resolutionSummary;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", summary='" + summary + '\'' +
                ", description='" + description + '\'' +
                ", fileId='" + fileId + '\'' +
                ", createdBy=" + createdBy +
                ", assignedBy=" + assignedBy +
                ", createdDateTime=" + createdDateTime +
                ", department=" + department +
                ", team=" + team +
                ", departmentHead=" + departmentHead +
                ", teamHead=" + teamHead +
                ", ticketType='" + ticketType + '\'' +
                ", assignedTo=" + assignedTo +
                ", prirority='" + prirority + '\'' +
                ", status='" + status + '\'' +
                ", targetResolutionDate=" + targetResolutionDate +
                ", actualResolutionDate=" + actualResolutionDate +
                ", resolutionSummary='" + resolutionSummary + '\'' +
                '}';
    }
}
