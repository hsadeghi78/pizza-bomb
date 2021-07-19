package com.barad.bomb.service.criteria;

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
 * Criteria class for the {@link com.barad.bomb.domain.ClassificationEntity} entity. This class is used
 * in {@link com.barad.bomb.web.rest.ClassificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /classifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClassificationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private IntegerFilter classCode;

    private StringFilter description;

    private LongFilter classTypeId;

    public ClassificationCriteria() {}

    public ClassificationCriteria(ClassificationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.classCode = other.classCode == null ? null : other.classCode.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.classTypeId = other.classTypeId == null ? null : other.classTypeId.copy();
    }

    @Override
    public ClassificationCriteria copy() {
        return new ClassificationCriteria(this);
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

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public IntegerFilter getClassCode() {
        return classCode;
    }

    public IntegerFilter classCode() {
        if (classCode == null) {
            classCode = new IntegerFilter();
        }
        return classCode;
    }

    public void setClassCode(IntegerFilter classCode) {
        this.classCode = classCode;
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

    public LongFilter getClassTypeId() {
        return classTypeId;
    }

    public LongFilter classTypeId() {
        if (classTypeId == null) {
            classTypeId = new LongFilter();
        }
        return classTypeId;
    }

    public void setClassTypeId(LongFilter classTypeId) {
        this.classTypeId = classTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClassificationCriteria that = (ClassificationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(classCode, that.classCode) &&
            Objects.equals(description, that.description) &&
            Objects.equals(classTypeId, that.classTypeId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, classCode, description, classTypeId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassificationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (classCode != null ? "classCode=" + classCode + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (classTypeId != null ? "classTypeId=" + classTypeId + ", " : "") +
            "}";
    }
}
