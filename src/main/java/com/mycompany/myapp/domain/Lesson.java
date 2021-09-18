package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.DaysOfWeek;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Lesson.
 */
@Entity
@Table(name = "lesson")
public class Lesson implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "lesson_name", nullable = false)
    private String lessonName;

    @NotNull
    @Column(name = "start_at", nullable = false)
    private Instant startAt;

    @NotNull
    @Column(name = "end_at", nullable = false)
    private Instant endAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DaysOfWeek dayOfWeek;

    @Column(name = "desciption")
    private String desciption;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne
    @JsonIgnoreProperties(value = { "lessonTimetables", "lessonInstances" }, allowSetters = true)
    private Subject subject;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lesson id(Long id) {
        this.id = id;
        return this;
    }

    public String getLessonName() {
        return this.lessonName;
    }

    public Lesson lessonName(String lessonName) {
        this.lessonName = lessonName;
        return this;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public Instant getStartAt() {
        return this.startAt;
    }

    public Lesson startAt(Instant startAt) {
        this.startAt = startAt;
        return this;
    }

    public void setStartAt(Instant startAt) {
        this.startAt = startAt;
    }

    public Instant getEndAt() {
        return this.endAt;
    }

    public Lesson endAt(Instant endAt) {
        this.endAt = endAt;
        return this;
    }

    public void setEndAt(Instant endAt) {
        this.endAt = endAt;
    }

    public DaysOfWeek getDayOfWeek() {
        return this.dayOfWeek;
    }

    public Lesson dayOfWeek(DaysOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        return this;
    }

    public void setDayOfWeek(DaysOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDesciption() {
        return this.desciption;
    }

    public Lesson desciption(String desciption) {
        this.desciption = desciption;
        return this;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Lesson createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Lesson active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public Lesson subject(Subject subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lesson)) {
            return false;
        }
        return id != null && id.equals(((Lesson) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lesson{" +
            "id=" + getId() +
            ", lessonName='" + getLessonName() + "'" +
            ", startAt='" + getStartAt() + "'" +
            ", endAt='" + getEndAt() + "'" +
            ", dayOfWeek='" + getDayOfWeek() + "'" +
            ", desciption='" + getDesciption() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
