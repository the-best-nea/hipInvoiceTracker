package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.LessonTimetableStudent} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.LessonTimetableStudentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lesson-timetable-students?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LessonTimetableStudentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FloatFilter pay;

    private LongFilter studentId;

    private LongFilter lessonTimetableId;

    public LessonTimetableStudentCriteria() {}

    public LessonTimetableStudentCriteria(LessonTimetableStudentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.pay = other.pay == null ? null : other.pay.copy();
        this.studentId = other.studentId == null ? null : other.studentId.copy();
        this.lessonTimetableId = other.lessonTimetableId == null ? null : other.lessonTimetableId.copy();
    }

    @Override
    public LessonTimetableStudentCriteria copy() {
        return new LessonTimetableStudentCriteria(this);
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
        final LessonTimetableStudentCriteria that = (LessonTimetableStudentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(pay, that.pay) &&
            Objects.equals(studentId, that.studentId) &&
            Objects.equals(lessonTimetableId, that.lessonTimetableId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pay, studentId, lessonTimetableId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LessonTimetableStudentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (pay != null ? "pay=" + pay + ", " : "") +
            (studentId != null ? "studentId=" + studentId + ", " : "") +
            (lessonTimetableId != null ? "lessonTimetableId=" + lessonTimetableId + ", " : "") +
            "}";
    }
}
