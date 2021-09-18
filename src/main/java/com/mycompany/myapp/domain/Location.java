package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @OneToMany(mappedBy = "location")
    @JsonIgnoreProperties(value = { "location", "subject", "students" }, allowSetters = true)
    private Set<LessonTimetable> lessonTimetables = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Location id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Location name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public Location address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<LessonTimetable> getLessonTimetables() {
        return this.lessonTimetables;
    }

    public Location lessonTimetables(Set<LessonTimetable> lessonTimetables) {
        this.setLessonTimetables(lessonTimetables);
        return this;
    }

    public Location addLessonTimetable(LessonTimetable lessonTimetable) {
        this.lessonTimetables.add(lessonTimetable);
        lessonTimetable.setLocation(this);
        return this;
    }

    public Location removeLessonTimetable(LessonTimetable lessonTimetable) {
        this.lessonTimetables.remove(lessonTimetable);
        lessonTimetable.setLocation(null);
        return this;
    }

    public void setLessonTimetables(Set<LessonTimetable> lessonTimetables) {
        if (this.lessonTimetables != null) {
            this.lessonTimetables.forEach(i -> i.setLocation(null));
        }
        if (lessonTimetables != null) {
            lessonTimetables.forEach(i -> i.setLocation(this));
        }
        this.lessonTimetables = lessonTimetables;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return id != null && id.equals(((Location) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
