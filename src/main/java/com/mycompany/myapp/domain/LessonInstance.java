package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.DayOfWeek;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A LessonInstance.
 */
@Entity
@Table(name = "lesson_instance")
public class LessonInstance implements Serializable {

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
    @Column(name = "creted_on", nullable = false)
    private Instant cretedOn;

    @OneToMany(mappedBy = "lessonTimetable")
    @JsonIgnoreProperties(value = { "student", "lessonTimetable" }, allowSetters = true)
    private Set<StudentRegister> studentRegisters = new HashSet<>();

    @ManyToOne
    private User internalUser;

    @ManyToMany
    @JoinTable(
        name = "rel_lesson_instance__student",
        joinColumns = @JoinColumn(name = "lesson_instance_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @JsonIgnoreProperties(value = { "studentRegisters", "lessonTimetables", "lessonInstances" }, allowSetters = true)
    private Set<Student> students = new HashSet<>();

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

    public LessonInstance id(Long id) {
        this.id = id;
        return this;
    }

    public String getLessonName() {
        return this.lessonName;
    }

    public LessonInstance lessonName(String lessonName) {
        this.lessonName = lessonName;
        return this;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public Instant getStartAt() {
        return this.startAt;
    }

    public LessonInstance startAt(Instant startAt) {
        this.startAt = startAt;
        return this;
    }

    public void setStartAt(Instant startAt) {
        this.startAt = startAt;
    }

    public Instant getEndAt() {
        return this.endAt;
    }

    public LessonInstance endAt(Instant endAt) {
        this.endAt = endAt;
        return this;
    }

    public void setEndAt(Instant endAt) {
        this.endAt = endAt;
    }

    public DayOfWeek getDayOfWeek() {
        return this.dayOfWeek;
    }

    public LessonInstance dayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        return this;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDescription() {
        return this.description;
    }

    public LessonInstance description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCretedOn() {
        return this.cretedOn;
    }

    public LessonInstance cretedOn(Instant cretedOn) {
        this.cretedOn = cretedOn;
        return this;
    }

    public void setCretedOn(Instant cretedOn) {
        this.cretedOn = cretedOn;
    }

    public Set<StudentRegister> getStudentRegisters() {
        return this.studentRegisters;
    }

    public LessonInstance studentRegisters(Set<StudentRegister> studentRegisters) {
        this.setStudentRegisters(studentRegisters);
        return this;
    }

    public LessonInstance addStudentRegister(StudentRegister studentRegister) {
        this.studentRegisters.add(studentRegister);
        studentRegister.setLessonTimetable(this);
        return this;
    }

    public LessonInstance removeStudentRegister(StudentRegister studentRegister) {
        this.studentRegisters.remove(studentRegister);
        studentRegister.setLessonTimetable(null);
        return this;
    }

    public void setStudentRegisters(Set<StudentRegister> studentRegisters) {
        if (this.studentRegisters != null) {
            this.studentRegisters.forEach(i -> i.setLessonTimetable(null));
        }
        if (studentRegisters != null) {
            studentRegisters.forEach(i -> i.setLessonTimetable(this));
        }
        this.studentRegisters = studentRegisters;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public LessonInstance internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public Set<Student> getStudents() {
        return this.students;
    }

    public LessonInstance students(Set<Student> students) {
        this.setStudents(students);
        return this;
    }

    public LessonInstance addStudent(Student student) {
        this.students.add(student);
        student.getLessonInstances().add(this);
        return this;
    }

    public LessonInstance removeStudent(Student student) {
        this.students.remove(student);
        student.getLessonInstances().remove(this);
        return this;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public LessonInstance subject(Subject subject) {
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
        if (!(o instanceof LessonInstance)) {
            return false;
        }
        return id != null && id.equals(((LessonInstance) o).id);
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
            "}";
    }
}
