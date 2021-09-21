package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.StudentRegister} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.StudentRegisterResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /student-registers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StudentRegisterCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter dateOfLesson;

    private FloatFilter pay;

    private BooleanFilter attended;

    private InstantFilter createdOn;

    private InstantFilter updatedOn;

    private LongFilter studentId;

    private LongFilter lessonTimetableId;

    public StudentRegisterCriteria() {}

    public StudentRegisterCriteria(StudentRegisterCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dateOfLesson = other.dateOfLesson == null ? null : other.dateOfLesson.copy();
        this.pay = other.pay == null ? null : other.pay.copy();
        this.attended = other.attended == null ? null : other.attended.copy();
        this.createdOn = other.createdOn == null ? null : other.createdOn.copy();
        this.updatedOn = other.updatedOn == null ? null : other.updatedOn.copy();
        this.studentId = other.studentId == null ? null : other.studentId.copy();
        this.lessonTimetableId = other.lessonTimetableId == null ? null : other.lessonTimetableId.copy();
    }

    @Override
    public StudentRegisterCriteria copy() {
        return new StudentRegisterCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getDateOfLesson() {
        return dateOfLesson;
    }

    public InstantFilter dateOfLesson() {
        if (dateOfLesson == null) {
            dateOfLesson = new InstantFilter();
        }
        return dateOfLesson;
    }

    public void setDateOfLesson(InstantFilter dateOfLesson) {
        this.dateOfLesson = dateOfLesson;
    }

    public FloatFilter getPay() {
        return pay;
    }

    public FloatFilter pay() {
        if (pay == null) {
            pay = new FloatFilter();
        }
        return pay;
    }

    public void setPay(FloatFilter pay) {
        this.pay = pay;
    }

    public BooleanFilter getAttended() {
        return attended;
    }

    public BooleanFilter attended() {
        if (attended == null) {
            attended = new BooleanFilter();
        }
        return attended;
    }

    public void setAttended(BooleanFilter attended) {
        this.attended = attended;
    }

    public InstantFilter getCreatedOn() {
        return createdOn;
    }

    public InstantFilter createdOn() {
        if (createdOn == null) {
            createdOn = new InstantFilter();
        }
        return createdOn;
    }

    public void setCreatedOn(InstantFilter createdOn) {
        this.createdOn = createdOn;
    }

    public InstantFilter getUpdatedOn() {
        return updatedOn;
    }

    public InstantFilter updatedOn() {
        if (updatedOn == null) {
            updatedOn = new InstantFilter();
        }
        return updatedOn;
    }

    public void setUpdatedOn(InstantFilter updatedOn) {
        this.updatedOn = updatedOn;
    }

    public LongFilter getStudentId() {
        return studentId;
    }

    public LongFilter studentId() {
        if (studentId == null) {
            studentId = new LongFilter();
        }
        return studentId;
    }

    public void setStudentId(LongFilter studentId) {
        this.studentId = studentId;
    }

    public LongFilter getLessonTimetableId() {
        return lessonTimetableId;
    }

    public LongFilter lessonTimetableId() {
        if (lessonTimetableId == null) {
            lessonTimetableId = new LongFilter();
        }
        return lessonTimetableId;
    }

    public void setLessonTimetableId(LongFilter lessonTimetableId) {
        this.lessonTimetableId = lessonTimetableId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StudentRegisterCriteria that = (StudentRegisterCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dateOfLesson, that.dateOfLesson) &&
            Objects.equals(pay, that.pay) &&
            Objects.equals(attended, that.attended) &&
            Objects.equals(createdOn, that.createdOn) &&
            Objects.equals(updatedOn, that.updatedOn) &&
            Objects.equals(studentId, that.studentId) &&
            Objects.equals(lessonTimetableId, that.lessonTimetableId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateOfLesson, pay, attended, createdOn, updatedOn, studentId, lessonTimetableId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentRegisterCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dateOfLesson != null ? "dateOfLesson=" + dateOfLesson + ", " : "") +
            (pay != null ? "pay=" + pay + ", " : "") +
            (attended != null ? "attended=" + attended + ", " : "") +
            (createdOn != null ? "createdOn=" + createdOn + ", " : "") +
            (updatedOn != null ? "updatedOn=" + updatedOn + ", " : "") +
            (studentId != null ? "studentId=" + studentId + ", " : "") +
            (lessonTimetableId != null ? "lessonTimetableId=" + lessonTimetableId + ", " : "") +
            "}";
    }
}
