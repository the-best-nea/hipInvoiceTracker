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
 * Criteria class for the {@link com.mycompany.myapp.domain.LessonTimetable} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.LessonTimetableResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lesson-timetables?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LessonTimetableCriteria implements Serializable, Criteria {

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

    private InstantFilter createdAt;

    private BooleanFilter active;

    private BooleanFilter registerTaken;

    private LongFilter locationId;

    private LongFilter subjectId;

    public LessonTimetableCriteria() {}

    public LessonTimetableCriteria(LessonTimetableCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.lessonName = other.lessonName == null ? null : other.lessonName.copy();
        this.startAt = other.startAt == null ? null : other.startAt.copy();
        this.endAt = other.endAt == null ? null : other.endAt.copy();
        this.dayOfWeek = other.dayOfWeek == null ? null : other.dayOfWeek.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.registerTaken = other.registerTaken == null ? null : other.registerTaken.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
        this.subjectId = other.subjectId == null ? null : other.subjectId.copy();
    }

    @Override
    public LessonTimetableCriteria copy() {
        return new LessonTimetableCriteria(this);
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

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            createdAt = new InstantFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public BooleanFilter active() {
        if (active == null) {
            active = new BooleanFilter();
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public BooleanFilter getRegisterTaken() {
        return registerTaken;
    }

    public BooleanFilter registerTaken() {
        if (registerTaken == null) {
            registerTaken = new BooleanFilter();
        }
        return registerTaken;
    }

    public void setRegisterTaken(BooleanFilter registerTaken) {
        this.registerTaken = registerTaken;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public LongFilter locationId() {
        if (locationId == null) {
            locationId = new LongFilter();
        }
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
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
        final LessonTimetableCriteria that = (LessonTimetableCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(lessonName, that.lessonName) &&
            Objects.equals(startAt, that.startAt) &&
            Objects.equals(endAt, that.endAt) &&
            Objects.equals(dayOfWeek, that.dayOfWeek) &&
            Objects.equals(description, that.description) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(active, that.active) &&
            Objects.equals(registerTaken, that.registerTaken) &&
            Objects.equals(locationId, that.locationId) &&
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
            createdAt,
            active,
            registerTaken,
            locationId,
            subjectId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LessonTimetableCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (lessonName != null ? "lessonName=" + lessonName + ", " : "") +
            (startAt != null ? "startAt=" + startAt + ", " : "") +
            (endAt != null ? "endAt=" + endAt + ", " : "") +
            (dayOfWeek != null ? "dayOfWeek=" + dayOfWeek + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (active != null ? "active=" + active + ", " : "") +
            (registerTaken != null ? "registerTaken=" + registerTaken + ", " : "") +
            (locationId != null ? "locationId=" + locationId + ", " : "") +
            (subjectId != null ? "subjectId=" + subjectId + ", " : "") +
            "}";
    }
}
