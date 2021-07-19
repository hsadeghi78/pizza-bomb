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
 * Criteria class for the {@link com.barad.bomb.domain.ClassTypeEntity} entity. This class is used
 * in {@link com.barad.bomb.web.rest.ClassTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /class-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClassTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private IntegerFilter typeCode;

    private StringFilter description;

    private LongFilter classificationsId;

    public ClassTypeCriteria() {}

    public ClassTypeCriteria(ClassTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.typeCode = other.typeCode == null ? null : other.typeCode.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.classificationsId = other.classificationsId == null ? null : other.classificationsId.copy();
    }

    @Override
    public ClassTypeCriteria copy() {
        return new ClassTypeCriteria(this);
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

    public IntegerFilter getTypeCode() {
        return typeCode;
    }

    public IntegerFilter typeCode() {
        if (typeCode == null) {
            typeCode = new IntegerFilter();
        }
        return typeCode;
    }

    public void setTypeCode(IntegerFilter typeCode) {
        this.typeCode = typeCode;
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

    public LongFilter getClassificationsId() {
        return classificationsId;
    }

    public LongFilter classificationsId() {
        if (classificationsId == null) {
            classificationsId = new LongFilter();
        }
        return classificationsId;
    }

    public void setClassificationsId(LongFilter classificationsId) {
        this.classificationsId = classificationsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClassTypeCriteria that = (ClassTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(typeCode, that.typeCode) &&
            Objects.equals(description, that.description) &&
            Objects.equals(classificationsId, that.classificationsId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, typeCode, description, classificationsId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassTypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (typeCode != null ? "typeCode=" + typeCode + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (classificationsId != null ? "classificationsId=" + classificationsId + ", " : "") +
            "}";
    }
}
