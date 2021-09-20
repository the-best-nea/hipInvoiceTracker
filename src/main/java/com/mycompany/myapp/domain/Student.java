package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Student.
 */
@Entity
@Table(name = "student")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "year_group", nullable = false)
    private Integer yearGroup;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "active")
    private Boolean active;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "balance")
    private Float balance;

    @OneToMany(mappedBy = "student")
    @JsonIgnoreProperties(value = { "student", "lessonTimetable" }, allowSetters = true)
    private Set<StudentRegister> studentRegisters = new HashSet<>();

    @ManyToMany(mappedBy = "students")
    @JsonIgnoreProperties(value = { "studentRegisters", "internalUser", "students", "subject" }, allowSetters = true)
    private Set<LessonInstance> lessonInstances = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student id(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Student firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Student lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getYearGroup() {
        return this.yearGroup;
    }

    public Student yearGroup(Integer yearGroup) {
        this.yearGroup = yearGroup;
        return this;
    }

    public void setYearGroup(Integer yearGroup) {
        this.yearGroup = yearGroup;
    }

    public String getEmail() {
        return this.email;
    }

    public Student email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Student phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public Student startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public Student endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Student active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Student createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Float getBalance() {
        return this.balance;
    }

    public Student balance(Float balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public Set<StudentRegister> getStudentRegisters() {
        return this.studentRegisters;
    }

    public Student studentRegisters(Set<StudentRegister> studentRegisters) {
        this.setStudentRegisters(studentRegisters);
        return this;
    }

    public Student addStudentRegister(StudentRegister studentRegister) {
        this.studentRegisters.add(studentRegister);
        studentRegister.setStudent(this);
        return this;
    }

    public Student removeStudentRegister(StudentRegister studentRegister) {
        this.studentRegisters.remove(studentRegister);
        studentRegister.setStudent(null);
        return this;
    }

    public void setStudentRegisters(Set<StudentRegister> studentRegisters) {
        if (this.studentRegisters != null) {
            this.studentRegisters.forEach(i -> i.setStudent(null));
        }
        if (studentRegisters != null) {
            studentRegisters.forEach(i -> i.setStudent(this));
        }
        this.studentRegisters = studentRegisters;
    }

    public Set<LessonInstance> getLessonInstances() {
        return this.lessonInstances;
    }

    public Student lessonInstances(Set<LessonInstance> lessonInstances) {
        this.setLessonInstances(lessonInstances);
        return this;
    }

    public Student addLessonInstance(LessonInstance lessonInstance) {
        this.lessonInstances.add(lessonInstance);
        lessonInstance.getStudents().add(this);
        return this;
    }

    public Student removeLessonInstance(LessonInstance lessonInstance) {
        this.lessonInstances.remove(lessonInstance);
        lessonInstance.getStudents().remove(this);
        return this;
    }

    public void setLessonInstances(Set<LessonInstance> lessonInstances) {
        if (this.lessonInstances != null) {
            this.lessonInstances.forEach(i -> i.removeStudent(this));
        }
        if (lessonInstances != null) {
            lessonInstances.forEach(i -> i.addStudent(this));
        }
        this.lessonInstances = lessonInstances;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        return id != null && id.equals(((Student) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Student{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", yearGroup=" + getYearGroup() +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", active='" + getActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", balance=" + getBalance() +
            "}";
    }
}
