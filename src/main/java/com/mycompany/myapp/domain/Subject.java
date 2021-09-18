package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Subject.
 */
@Entity
@Table(name = "subject")
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "subject_name", nullable = false)
    private String subjectName;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "subject")
    @JsonIgnoreProperties(value = { "location", "subject", "students" }, allowSetters = true)
    private Set<LessonTimetable> lessonTimetables = new HashSet<>();

    @OneToMany(mappedBy = "subject")
    @JsonIgnoreProperties(value = { "studentRegisters", "internalUser", "students", "subject" }, allowSetters = true)
    private Set<LessonInstance> lessonInstances = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Subject id(Long id) {
        this.id = id;
        return this;
    }

    public String getSubjectName() {
        return this.subjectName;
    }

    public Subject subjectName(String subjectName) {
        this.subjectName = subjectName;
        return this;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getDescription() {
        return this.description;
    }

    public Subject description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Subject createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Subject active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<LessonTimetable> getLessonTimetables() {
        return this.lessonTimetables;
    }

    public Subject lessonTimetables(Set<LessonTimetable> lessonTimetables) {
        this.setLessonTimetables(lessonTimetables);
        return this;
    }

    public Subject addLessonTimetable(LessonTimetable lessonTimetable) {
        this.lessonTimetables.add(lessonTimetable);
        lessonTimetable.setSubject(this);
        return this;
    }

    public Subject removeLessonTimetable(LessonTimetable lessonTimetable) {
        this.lessonTimetables.remove(lessonTimetable);
        lessonTimetable.setSubject(null);
        return this;
    }

    public void setLessonTimetables(Set<LessonTimetable> lessonTimetables) {
        if (this.lessonTimetables != null) {
            this.lessonTimetables.forEach(i -> i.setSubject(null));
        }
        if (lessonTimetables != null) {
            lessonTimetables.forEach(i -> i.setSubject(this));
        }
        this.lessonTimetables = lessonTimetables;
    }

    public Set<LessonInstance> getLessonInstances() {
        return this.lessonInstances;
    }

    public Subject lessonInstances(Set<LessonInstance> lessonInstances) {
        this.setLessonInstances(lessonInstances);
        return this;
    }

    public Subject addLessonInstance(LessonInstance lessonInstance) {
        this.lessonInstances.add(lessonInstance);
        lessonInstance.setSubject(this);
        return this;
    }

    public Subject removeLessonInstance(LessonInstance lessonInstance) {
        this.lessonInstances.remove(lessonInstance);
        lessonInstance.setSubject(null);
        return this;
    }

    public void setLessonInstances(Set<LessonInstance> lessonInstances) {
        if (this.lessonInstances != null) {
            this.lessonInstances.forEach(i -> i.setSubject(null));
        }
        if (lessonInstances != null) {
            lessonInstances.forEach(i -> i.setSubject(this));
        }
        this.lessonInstances = lessonInstances;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Subject)) {
            return false;
        }
        return id != null && id.equals(((Subject) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Subject{" +
            "id=" + getId() +
            ", subjectName='" + getSubjectName() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
