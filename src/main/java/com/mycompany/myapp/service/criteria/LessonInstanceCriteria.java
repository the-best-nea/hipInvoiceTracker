package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.DayOfWeek;
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
 * Criteria class for the {@link com.mycompany.myapp.domain.LessonInstance} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.LessonInstanceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lesson-instances?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LessonInstanceCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DayOfWeek
     */
    public static class DayOfWeekFilter extends Filter<DayOfWeek> {

        public DayOfWeekFilter() {}

        public DayOfWeekFilter(DayOfWeekFilter filter) {
            super(filter);
        }

        @Override
        public DayOfWeekFilter copy() {
            return new DayOfWeekFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter lessonName;

    private InstantFilter startAt;

    private InstantFilter endAt;

    private DayOfWeekFilter dayOfWeek;

    private StringFilter description;

    private InstantFilter cretedOn;

    private LongFilter studentRegisterId;

    private LongFilter internalUserId;

    private LongFilter studentId;

    private LongFilter subjectId;

    public LessonInstanceCriteria() {}

    public LessonInstanceCriteria(LessonInstanceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.lessonName = other.lessonName == null ? null : other.lessonName.copy();
        this.startAt = other.startAt == null ? null : other.startAt.copy();
        this.endAt = other.endAt == null ? null : other.endAt.copy();
        this.dayOfWeek = other.dayOfWeek == null ? null : other.dayOfWeek.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.cretedOn = other.cretedOn == null ? null : other.cretedOn.copy();
        this.studentRegisterId = other.studentRegisterId == null ? null : other.studentRegisterId.copy();
        this.internalUserId = other.internalUserId == null ? null : other.internalUserId.copy();
        this.studentId = other.studentId == null ? null : other.studentId.copy();
        this.subjectId = other.subjectId == null ? null : other.subjectId.copy();
    }

    @Override
    public LessonInstanceCriteria copy() {
        return new LessonInstanceCriteria(this);
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

    public StringFilter getLessonName() {
        return lessonName;
    }

    public StringFilter lessonName() {
        if (lessonName == null) {
            lessonName = new StringFilter();
        }
        return lessonName;
    }

    public void setLessonName(StringFilter lessonName) {
        this.lessonName = lessonName;
    }

    public InstantFilter getStartAt() {
        return startAt;
    }

    public InstantFilter startAt() {
        if (startAt == null) {
            startAt = new InstantFilter();
        }
        return startAt;
    }

    public void setStartAt(InstantFilter startAt) {
        this.startAt = startAt;
    }

    public InstantFilter getEndAt() {
        return endAt;
    }

    public InstantFilter endAt() {
        if (endAt == null) {
            endAt = new InstantFilter();
        }
        return endAt;
    }

    public void setEndAt(InstantFilter endAt) {
        this.endAt = endAt;
    }

    public DayOfWeekFilter getDayOfWeek() {
        return dayOfWeek;
    }

    public DayOfWeekFilter dayOfWeek() {
        if (dayOfWeek == null) {
            dayOfWeek = new DayOfWeekFilter();
        }
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeekFilter dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public InstantFilter getCretedOn() {
        return cretedOn;
    }

    public InstantFilter cretedOn() {
        if (cretedOn == null) {
            cretedOn = new InstantFilter();
        }
        return cretedOn;
    }

    public void setCretedOn(InstantFilter cretedOn) {
        this.cretedOn = cretedOn;
    }

    public LongFilter getStudentRegisterId() {
        return studentRegisterId;
    }

    public LongFilter studentRegisterId() {
        if (studentRegisterId == null) {
            studentRegisterId = new LongFilter();
        }
        return studentRegisterId;
    }

    public void setStudentRegisterId(LongFilter studentRegisterId) {
        this.studentRegisterId = studentRegisterId;
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

    public LongFilter getSubjectId() {
        return subjectId;
    }

    public LongFilter subjectId() {
        if (subjectId == null) {
            subjectId = new LongFilter();
        }
        return subjectId;
    }

    public void setSubjectId(LongFilter subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LessonInstanceCriteria that = (LessonInstanceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(lessonName, that.lessonName) &&
            Objects.equals(startAt, that.startAt) &&
            Objects.equals(endAt, that.endAt) &&
            Objects.equals(dayOfWeek, that.dayOfWeek) &&
            Objects.equals(description, that.description) &&
            Objects.equals(cretedOn, that.cretedOn) &&
            Objects.equals(studentRegisterId, that.studentRegisterId) &&
            Objects.equals(internalUserId, that.internalUserId) &&
            Objects.equals(studentId, that.studentId) &&
            Objects.equals(subjectId, that.subjectId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            lessonName,
            startAt,
            endAt,
            dayOfWeek,
            description,
            cretedOn,
            studentRegisterId,
            internalUserId,
            studentId,
            subjectId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LessonInstanceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (lessonName != null ? "lessonName=" + lessonName + ", " : "") +
            (startAt != null ? "startAt=" + startAt + ", " : "") +
            (endAt != null ? "endAt=" + endAt + ", " : "") +
            (dayOfWeek != null ? "dayOfWeek=" + dayOfWeek + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (cretedOn != null ? "cretedOn=" + cretedOn + ", " : "") +
            (studentRegisterId != null ? "studentRegisterId=" + studentRegisterId + ", " : "") +
            (internalUserId != null ? "internalUserId=" + internalUserId + ", " : "") +
            (studentId != null ? "studentId=" + studentId + ", " : "") +
            (subjectId != null ? "subjectId=" + subjectId + ", " : "") +
            "}";
    }
}
