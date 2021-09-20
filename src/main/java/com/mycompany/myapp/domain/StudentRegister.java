package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A StudentRegister.
 */
@Entity
@Table(name = "student_register")
public class StudentRegister implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date_of_lesson", nullable = false)
    private Instant dateOfLesson;

    @Column(name = "pay")
    private Float pay;

    @Column(name = "attended")
    private Boolean attended;

    @NotNull
    @Column(name = "created_on", nullable = false)
    private Instant createdOn;

    @NotNull
    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;

    @ManyToOne
    @JsonIgnoreProperties(value = { "studentRegisters", "lessonInstances" }, allowSetters = true)
    private Student student;

    @ManyToOne
    @JsonIgnoreProperties(value = { "studentRegisters", "internalUser", "students", "subject" }, allowSetters = true)
    private LessonInstance lessonTimetable;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentRegister id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getDateOfLesson() {
        return this.dateOfLesson;
    }

    public StudentRegister dateOfLesson(Instant dateOfLesson) {
        this.dateOfLesson = dateOfLesson;
        return this;
    }

    public void setDateOfLesson(Instant dateOfLesson) {
        this.dateOfLesson = dateOfLesson;
    }

    public Float getPay() {
        return this.pay;
    }

    public StudentRegister pay(Float pay) {
        this.pay = pay;
        return this;
    }

    public void setPay(Float pay) {
        this.pay = pay;
    }

    public Boolean getAttended() {
        return this.attended;
    }

    public StudentRegister attended(Boolean attended) {
        this.attended = attended;
        return this;
    }

    public void setAttended(Boolean attended) {
        this.attended = attended;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public StudentRegister createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getUpdatedOn() {
        return this.updatedOn;
    }

    public StudentRegister updatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Student getStudent() {
        return this.student;
    }

    public StudentRegister student(Student student) {
        this.setStudent(student);
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public LessonInstance getLessonTimetable() {
        return this.lessonTimetable;
    }

    public StudentRegister lessonTimetable(LessonInstance lessonInstance) {
        this.setLessonTimetable(lessonInstance);
        return this;
    }

    public void setLessonTimetable(LessonInstance lessonInstance) {
        this.lessonTimetable = lessonInstance;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentRegister)) {
            return false;
        }
        return id != null && id.equals(((StudentRegister) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentRegister{" +
            "id=" + getId() +
            ", dateOfLesson='" + getDateOfLesson() + "'" +
            ", pay=" + getPay() +
            ", attended='" + getAttended() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            "}";
    }
}
