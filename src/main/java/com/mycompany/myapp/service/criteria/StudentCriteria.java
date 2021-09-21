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
 * Criteria class for the {@link com.mycompany.myapp.domain.Student} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.StudentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /students?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StudentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private IntegerFilter yearGroup;

    private StringFilter email;

    private StringFilter phoneNumber;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private BooleanFilter active;

    private InstantFilter createdAt;

    private FloatFilter balance;

    private LongFilter studentRegisterId;

    private LongFilter lessonInstanceId;

    public StudentCriteria() {}

    public StudentCriteria(StudentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.yearGroup = other.yearGroup == null ? null : other.yearGroup.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.balance = other.balance == null ? null : other.balance.copy();
        this.studentRegisterId = other.studentRegisterId == null ? null : other.studentRegisterId.copy();
        this.lessonInstanceId = other.lessonInstanceId == null ? null : other.lessonInstanceId.copy();
    }

    @Override
    public StudentCriteria copy() {
        return new StudentCriteria(this);
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

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public IntegerFilter getYearGroup() {
        return yearGroup;
    }

    public IntegerFilter yearGroup() {
        if (yearGroup == null) {
            yearGroup = new IntegerFilter();
        }
        return yearGroup;
    }

    public void setYearGroup(IntegerFilter yearGroup) {
        this.yearGroup = yearGroup;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public StringFilter phoneNumber() {
        if (phoneNumber == null) {
            phoneNumber = new StringFilter();
        }
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public InstantFilter getStartDate() {
        return startDate;
    }

    public InstantFilter startDate() {
        if (startDate == null) {
            startDate = new InstantFilter();
        }
        return startDate;
    }

    public void setStartDate(InstantFilter startDate) {
        this.startDate = startDate;
    }

    public InstantFilter getEndDate() {
        return endDate;
    }

    public InstantFilter endDate() {
        if (endDate == null) {
            endDate = new InstantFilter();
        }
        return endDate;
    }

    public void setEndDate(InstantFilter endDate) {
        this.endDate = endDate;
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

    public FloatFilter getBalance() {
        return balance;
    }

    public FloatFilter balance() {
        if (balance == null) {
            balance = new FloatFilter();
        }
        return balance;
    }

    public void setBalance(FloatFilter balance) {
        this.balance = balance;
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
        final StudentCriteria that = (StudentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(yearGroup, that.yearGroup) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(active, that.active) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(balance, that.balance) &&
            Objects.equals(studentRegisterId, that.studentRegisterId) &&
            Objects.equals(lessonInstanceId, that.lessonInstanceId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            firstName,
            lastName,
            yearGroup,
            email,
            phoneNumber,
            startDate,
            endDate,
            active,
            createdAt,
            balance,
            studentRegisterId,
            lessonInstanceId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (yearGroup != null ? "yearGroup=" + yearGroup + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (active != null ? "active=" + active + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (balance != null ? "balance=" + balance + ", " : "") +
            (studentRegisterId != null ? "studentRegisterId=" + studentRegisterId + ", " : "") +
            (lessonInstanceId != null ? "lessonInstanceId=" + lessonInstanceId + ", " : "") +
            "}";
    }
}
