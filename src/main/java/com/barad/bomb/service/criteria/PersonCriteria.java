package com.barad.bomb.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.barad.bomb.domain.PersonEntity} entity. This class is used
 * in {@link com.barad.bomb.web.rest.PersonResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /people?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PersonCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fisrtName;

    private StringFilter lastName;

    private LocalDateFilter birthDate;

    private StringFilter nationalCode;

    private LongFilter partiesId;

    public PersonCriteria() {}

    public PersonCriteria(PersonCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fisrtName = other.fisrtName == null ? null : other.fisrtName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.birthDate = other.birthDate == null ? null : other.birthDate.copy();
        this.nationalCode = other.nationalCode == null ? null : other.nationalCode.copy();
        this.partiesId = other.partiesId == null ? null : other.partiesId.copy();
    }

    @Override
    public PersonCriteria copy() {
        return new PersonCriteria(this);
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

    public StringFilter getFisrtName() {
        return fisrtName;
    }

    public StringFilter fisrtName() {
        if (fisrtName == null) {
            fisrtName = new StringFilter();
        }
        return fisrtName;
    }

    public void setFisrtName(StringFilter fisrtName) {
        this.fisrtName = fisrtName;
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

    public LocalDateFilter getBirthDate() {
        return birthDate;
    }

    public LocalDateFilter birthDate() {
        if (birthDate == null) {
            birthDate = new LocalDateFilter();
        }
        return birthDate;
    }

    public void setBirthDate(LocalDateFilter birthDate) {
        this.birthDate = birthDate;
    }

    public StringFilter getNationalCode() {
        return nationalCode;
    }

    public StringFilter nationalCode() {
        if (nationalCode == null) {
            nationalCode = new StringFilter();
        }
        return nationalCode;
    }

    public void setNationalCode(StringFilter nationalCode) {
        this.nationalCode = nationalCode;
    }

    public LongFilter getPartiesId() {
        return partiesId;
    }

    public LongFilter partiesId() {
        if (partiesId == null) {
            partiesId = new LongFilter();
        }
        return partiesId;
    }

    public void setPartiesId(LongFilter partiesId) {
        this.partiesId = partiesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PersonCriteria that = (PersonCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fisrtName, that.fisrtName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(nationalCode, that.nationalCode) &&
            Objects.equals(partiesId, that.partiesId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fisrtName, lastName, birthDate, nationalCode, partiesId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fisrtName != null ? "fisrtName=" + fisrtName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (birthDate != null ? "birthDate=" + birthDate + ", " : "") +
            (nationalCode != null ? "nationalCode=" + nationalCode + ", " : "") +
            (partiesId != null ? "partiesId=" + partiesId + ", " : "") +
            "}";
    }
}
