package com.aye.issueTracker.model;

import com.mongodb.lang.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Document(collection = "TICKET_HISTORY")
public class TicketHistory {

    public static final String SEQUENCE_NAME = "ticket_history_sequence";

    @Id
    private Long id;

    @DBRef
    private Ticket ticket;

    @DBRef
    private Employee assignedBy;

    @DBRef
    private Employee assignedTo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Nullable
    private LocalDate endDate;

    private String prevStatus;

    public TicketHistory() {
    }

    public TicketHistory(Long id, Ticket ticket, Employee assignedBy, Employee assignedTo, LocalDate startDate, LocalDate endDate, String prevStatus) {
        this.id = id;
        this.ticket = ticket;
        this.assignedBy = assignedBy;
        this.assignedTo = assignedTo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.prevStatus = prevStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Employee getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(Employee assignedBy) {
        this.assignedBy = assignedBy;
    }

    public Employee getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Employee assignedTo) {
        this.assignedTo = assignedTo;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getPrevStatus() {
        return prevStatus;
    }

    public void setPrevStatus(String prevStatus) {
        this.prevStatus = prevStatus;
    }

    @Override
    public String toString() {
        return "TicketHistory{" +
                "id=" + id +
                ", ticket=" + ticket +
                ", assignedBy=" + assignedBy +
                ", assignedTo=" + assignedTo +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", prevStatus='" + prevStatus + '\'' +
                '}';
    }
}
