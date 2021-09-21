package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.DayOfWeek;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A LessonTimetable.
 */
@Entity
@Table(name = "lesson_timetable")
public class LessonTimetable implements Serializable {

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
    private DayOfWeek dayOfWeek;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "register_taken")
    private Boolean registerTaken;

    @ManyToOne
    @JsonIgnoreProperties(value = { "lessonTimetables" }, allowSetters = true)
    private Location location;

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

    public LessonTimetable id(Long id) {
        this.id = id;
        return this;
    }

    public String getLessonName() {
        return this.lessonName;
    }

    public LessonTimetable lessonName(String lessonName) {
        this.lessonName = lessonName;
        return this;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public Instant getStartAt() {
        return this.startAt;
    }

    public LessonTimetable startAt(Instant startAt) {
        this.startAt = startAt;
        return this;
    }

    public void setStartAt(Instant startAt) {
        this.startAt = startAt;
    }

    public Instant getEndAt() {
        return this.endAt;
    }

    public LessonTimetable endAt(Instant endAt) {
        this.endAt = endAt;
        return this;
    }

    public void setEndAt(Instant endAt) {
        this.endAt = endAt;
    }

    public DayOfWeek getDayOfWeek() {
        return this.dayOfWeek;
    }

    public LessonTimetable dayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        return this;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDescription() {
        return this.description;
    }

    public LessonTimetable description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public LessonTimetable createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getActive() {
        return this.active;
    }

    public LessonTimetable active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getRegisterTaken() {
        return this.registerTaken;
    }

    public LessonTimetable registerTaken(Boolean registerTaken) {
        this.registerTaken = registerTaken;
        return this;
    }

    public void setRegisterTaken(Boolean registerTaken) {
        this.registerTaken = registerTaken;
    }

    public Location getLocation() {
        return this.location;
    }

    public LessonTimetable location(Location location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public LessonTimetable subject(Subject subject) {
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
        if (!(o instanceof LessonTimetable)) {
            return false;
        }
        return id != null && id.equals(((LessonTimetable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LessonTimetable{" +
            "id=" + getId() +
            ", lessonName='" + getLessonName() + "'" +
            ", startAt='" + getStartAt() + "'" +
            ", endAt='" + getEndAt() + "'" +
            ", dayOfWeek='" + getDayOfWeek() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", active='" + getActive() + "'" +
            ", registerTaken='" + getRegisterTaken() + "'" +
            "}";
    }
}
