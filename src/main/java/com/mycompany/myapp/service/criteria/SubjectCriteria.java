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
 * Criteria class for the {@link com.mycompany.myapp.domain.Subject} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.SubjectResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /subjects?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SubjectCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter subjectName;

    private StringFilter description;

    private InstantFilter createdAt;

    private BooleanFilter active;

    private LongFilter lessonTimetableId;

    private LongFilter lessonInstanceId;

    public SubjectCriteria() {}

    public SubjectCriteria(SubjectCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.subjectName = other.subjectName == null ? null : other.subjectName.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.lessonTimetableId = other.lessonTimetableId == null ? null : other.lessonTimetableId.copy();
        this.lessonInstanceId = other.lessonInstanceId == null ? null : other.lessonInstanceId.copy();
    }

    @Override
    public SubjectCriteria copy() {
        return new SubjectCriteria(this);
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

    public StringFilter getSubjectName() {
        return subjectName;
    }

    public StringFilter subjectName() {
        if (subjectName == null) {
            subjectName = new StringFilter();
        }
        return subjectName;
    }

    public void setSubjectName(StringFilter subjectName) {
        this.subjectName = subjectName;
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

    public LongFilter getLessonInstanceId() {
        return lessonInstanceId;
    }

    public LongFilter lessonInstanceId() {
        if (lessonInstanceId == null) {
            lessonInstanceId = new LongFilter();
        }
        return lessonInstanceId;
    }

    public void setLessonInstanceId(LongFilter lessonInstanceId) {
        this.lessonInstanceId = lessonInstanceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SubjectCriteria that = (SubjectCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(subjectName, that.subjectName) &&
            Objects.equals(description, that.description) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(active, that.active) &&
            Objects.equals(lessonTimetableId, that.lessonTimetableId) &&
            Objects.equals(lessonInstanceId, that.lessonInstanceId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subjectName, description, createdAt, active, lessonTimetableId, lessonInstanceId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubjectCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (subjectName != null ? "subjectName=" + subjectName + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (active != null ? "active=" + active + ", " : "") +
            (lessonTimetableId != null ? "lessonTimetableId=" + lessonTimetableId + ", " : "") +
            (lessonInstanceId != null ? "lessonInstanceId=" + lessonInstanceId + ", " : "") +
            "}";
    }
}
