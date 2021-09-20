package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A LessonTimetableStudent.
 */
@Entity
@Table(name = "lesson_timetable_student")
public class LessonTimetableStudent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pay")
    private Float pay;

    @ManyToOne
    @JsonIgnoreProperties(value = { "studentRegisters", "lessonInstances" }, allowSetters = true)
    private Student student;

    @ManyToOne
    @JsonIgnoreProperties(value = { "location", "subject" }, allowSetters = true)
    private LessonTimetable lessonTimetable;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LessonTimetableStudent id(Long id) {
        this.id = id;
        return this;
    }

    public Float getPay() {
        return this.pay;
    }

    public LessonTimetableStudent pay(Float pay) {
        this.pay = pay;
        return this;
    }

    public void setPay(Float pay) {
        this.pay = pay;
    }

    public Student getStudent() {
        return this.student;
    }

    public LessonTimetableStudent student(Student student) {
        this.setStudent(student);
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public LessonTimetable getLessonTimetable() {
        return this.lessonTimetable;
    }

    public LessonTimetableStudent lessonTimetable(LessonTimetable lessonTimetable) {
        this.setLessonTimetable(lessonTimetable);
        return this;
    }

    public void setLessonTimetable(LessonTimetable lessonTimetable) {
        this.lessonTimetable = lessonTimetable;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LessonTimetableStudent)) {
            return false;
        }
        return id != null && id.equals(((LessonTimetableStudent) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LessonTimetableStudent{" +
            "id=" + getId() +
            ", pay=" + getPay() +
            "}";
    }
}
