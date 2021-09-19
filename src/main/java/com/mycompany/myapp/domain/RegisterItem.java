package com.mycompany.myapp.domain;

import java.util.StringJoiner;

public class RegisterItem {

    private Long id;
    private Long studentId;
    private String firstName;
    private String lastName;
    private boolean attended;

    public Long getId() {
        return id;
    }

    public RegisterItem setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getStudentId() {
        return studentId;
    }

    public RegisterItem setStudentId(Long studentId) {
        this.studentId = studentId;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public RegisterItem setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public RegisterItem setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public boolean isAttended() {
        return attended;
    }

    public RegisterItem setAttended(boolean attended) {
        this.attended = attended;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RegisterItem.class.getSimpleName() + "[", "]")
            .add("id=" + id)
            .add("studentId=" + studentId)
            .add("firstName='" + firstName + "'")
            .add("lastName='" + lastName + "'")
            .add("attended=" + attended)
            .toString();
    }
}
