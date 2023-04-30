package com.aye.issueTracker.model;

import org.springframework.data.annotation.Id;

import java.util.Objects;

public class UserDetails {

    @Id
    private Long id;
    private String detailName;
    private String detailName1;

    public UserDetails() {
    }

    public UserDetails(Long id, String detailName) {
        this.id = id;
        this.detailName = detailName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public String getDetailName1() {
        return detailName1;
    }

    public void setDetailName1(String detailName1) {
        this.detailName1 = detailName1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetails that = (UserDetails) o;
        return id.equals(that.id) && detailName.equals(that.detailName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, detailName);
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "id=" + id +
                ", detailName='" + detailName + '\'' +
                '}';
    }
}
