package com.aye.issueTracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "TEAM_HEAD")
public class TeamHead {

    public static final String SEQUENCE_NAME = "teamHead_sequence";

    @Id
    private Long id;
    @DBRef
    private User user;

    @DBRef
    private  Team team;

    public TeamHead() {
    }

    public TeamHead(Long id, User user, Team team) {
        this.id = id;
        this.user = user;
        this.team = team;
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

    public void setUser(User user) {
        this.user = user;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "TeamHead{" +
                "id=" + id +
                ", user=" + user +
                ", team=" + team +
                '}';
    }
}
