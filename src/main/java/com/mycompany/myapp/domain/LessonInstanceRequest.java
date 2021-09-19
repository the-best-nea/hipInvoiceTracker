package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.enumeration.DayOfWeek;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;

/**
 * A LessonInstance.
 */
public class LessonInstanceRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String lessonName;

    @NotNull
    private Instant startAt;

    @NotNull
    private Instant endAt;

    @NotNull
    private DayOfWeek dayOfWeek;

    private String description;

    @NotNull
    private Instant cretedOn;

    private Set<StudentRegister> studentRegisters = new HashSet<>();

    private User internalUser;

    private Set<Student> students = new HashSet<>();

    private Subject subject;

    private List<RegistrationDetails> registrationDetails = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public Instant getStartAt() {
        return startAt;
    }

    public void setStartAt(Instant startAt) {
        this.startAt = startAt;
    }

    public Instant getEndAt() {
        return endAt;
    }

    public void setEndAt(Instant endAt) {
        this.endAt = endAt;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCretedOn() {
        return cretedOn;
    }

    public void setCretedOn(Instant cretedOn) {
        this.cretedOn = cretedOn;
    }

    public Set<StudentRegister> getStudentRegisters() {
        return studentRegisters;
    }

    public void setStudentRegisters(Set<StudentRegister> studentRegisters) {
        this.studentRegisters = studentRegisters;
    }

    public User getInternalUser() {
        return internalUser;
    }

    public void setInternalUser(User internalUser) {
        this.internalUser = internalUser;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<RegistrationDetails> getRegistrationDetails() {
        return registrationDetails;
    }

    public void setRegistrationDetails(List<RegistrationDetails> registrationDetails) {
        this.registrationDetails = registrationDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LessonInstanceRequest)) {
            return false;
        }
        return id != null && id.equals(((LessonInstanceRequest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LessonInstance{" +
            "id=" + getId() +
            ", lessonName='" + getLessonName() + "'" +
            ", startAt='" + getStartAt() + "'" +
            ", endAt='" + getEndAt() + "'" +
            ", dayOfWeek='" + getDayOfWeek() + "'" +
            ", description='" + getDescription() + "'" +
            ", cretedOn='" + getCretedOn() + "'" +
            ", registrationDetails='" + getRegistrationDetails() + "'" +
            "}";
    }
}
