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
 * Criteria class for the {@link com.mycompany.myapp.domain.LessonTimetableTeacher} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.LessonTimetableTeacherResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lesson-timetable-teachers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LessonTimetableTeacherCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FloatFilter pay;

    private LongFilter lessonTimetableId;

    private LongFilter internalUserId;

    public LessonTimetableTeacherCriteria() {}

    public LessonTimetableTeacherCriteria(LessonTimetableTeacherCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.pay = other.pay == null ? null : other.pay.copy();
        this.lessonTimetableId = other.lessonTimetableId == null ? null : other.lessonTimetableId.copy();
        this.internalUserId = other.internalUserId == null ? null : other.internalUserId.copy();
    }

    @Override
    public LessonTimetableTeacherCriteria copy() {
        return new LessonTimetableTeacherCriteria(this);
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

    public LongFilter getInternalUserId() {
        return internalUserId;
    }

    public LongFilter internalUserId() {
        if (internalUserId == null) {
            internalUserId = new LongFilter();
        }
        return internalUserId;
    }

    public void setInternalUserId(LongFilter internalUserId) {
        this.internalUserId = internalUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LessonTimetableTeacherCriteria that = (LessonTimetableTeacherCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(pay, that.pay) &&
            Objects.equals(lessonTimetableId, that.lessonTimetableId) &&
            Objects.equals(internalUserId, that.internalUserId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pay, lessonTimetableId, internalUserId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LessonTimetableTeacherCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (pay != null ? "pay=" + pay + ", " : "") +
            (lessonTimetableId != null ? "lessonTimetableId=" + lessonTimetableId + ", " : "") +
            (internalUserId != null ? "internalUserId=" + internalUserId + ", " : "") +
            "}";
    }
}
