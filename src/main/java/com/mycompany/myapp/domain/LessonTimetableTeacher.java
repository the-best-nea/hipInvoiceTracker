package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A LessonTimetableTeacher.
 */
@Entity
@Table(name = "lesson_timetable_teacher")
public class LessonTimetableTeacher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "pay", nullable = false)
    private Float pay;

    @ManyToOne
    @JsonIgnoreProperties(value = { "location", "subject" }, allowSetters = true)
    private LessonTimetable lessonTimetable;

    @ManyToOne
    private User internalUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LessonTimetableTeacher id(Long id) {
        this.id = id;
        return this;
    }

    public Float getPay() {
        return this.pay;
    }

    public LessonTimetableTeacher pay(Float pay) {
        this.pay = pay;
        return this;
    }

    public void setPay(Float pay) {
        this.pay = pay;
    }

    public LessonTimetable getLessonTimetable() {
        return this.lessonTimetable;
    }

    public LessonTimetableTeacher lessonTimetable(LessonTimetable lessonTimetable) {
        this.setLessonTimetable(lessonTimetable);
        return this;
    }

    public void setLessonTimetable(LessonTimetable lessonTimetable) {
        this.lessonTimetable = lessonTimetable;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public LessonTimetableTeacher internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LessonTimetableTeacher)) {
            return false;
        }
        return id != null && id.equals(((LessonTimetableTeacher) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LessonTimetableTeacher{" +
            "id=" + getId() +
            ", pay=" + getPay() +
            "}";
    }
}
