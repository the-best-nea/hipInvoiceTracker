package com.mycompany.myapp.domain;

import java.time.LocalDateTime;
import java.util.StringJoiner;

public class RegistrationDetails {

    private Long id;
    private Long studentId;
    private String firstName;
    private String lastName;
    private boolean attended;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RegistrationDetails.class.getSimpleName() + "[", "]")
            .add("id=" + id)
            .add("studentId=" + studentId)
            .add("firstName='" + firstName + "'")
            .add("lastName='" + lastName + "'")
            .add("attended=" + attended)
            .add("createdAt=" + createdAt)
            .toString();
    }
}
