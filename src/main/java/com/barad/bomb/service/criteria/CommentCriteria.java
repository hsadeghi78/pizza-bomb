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
 * Criteria class for the {@link com.barad.bomb.domain.CommentEntity} entity. This class is used
 * in {@link com.barad.bomb.web.rest.CommentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /comments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CommentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter rating;

    private StringFilter description;

    private LongFilter childrenId;

    private LongFilter writerPartyId;

    private LongFilter audiencePartyId;

    private LongFilter parentId;

    public CommentCriteria() {}

    public CommentCriteria(CommentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.rating = other.rating == null ? null : other.rating.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.childrenId = other.childrenId == null ? null : other.childrenId.copy();
        this.writerPartyId = other.writerPartyId == null ? null : other.writerPartyId.copy();
        this.audiencePartyId = other.audiencePartyId == null ? null : other.audiencePartyId.copy();
        this.parentId = other.parentId == null ? null : other.parentId.copy();
    }

    @Override
    public CommentCriteria copy() {
        return new CommentCriteria(this);
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

    public IntegerFilter getRating() {
        return rating;
    }

    public IntegerFilter rating() {
        if (rating == null) {
            rating = new IntegerFilter();
        }
        return rating;
    }

    public void setRating(IntegerFilter rating) {
        this.rating = rating;
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

    public LongFilter getChildrenId() {
        return childrenId;
    }

    public LongFilter childrenId() {
        if (childrenId == null) {
            childrenId = new LongFilter();
        }
        return childrenId;
    }

    public void setChildrenId(LongFilter childrenId) {
        this.childrenId = childrenId;
    }

    public LongFilter getWriterPartyId() {
        return writerPartyId;
    }

    public LongFilter writerPartyId() {
        if (writerPartyId == null) {
            writerPartyId = new LongFilter();
        }
        return writerPartyId;
    }

    public void setWriterPartyId(LongFilter writerPartyId) {
        this.writerPartyId = writerPartyId;
    }

    public LongFilter getAudiencePartyId() {
        return audiencePartyId;
    }

    public LongFilter audiencePartyId() {
        if (audiencePartyId == null) {
            audiencePartyId = new LongFilter();
        }
        return audiencePartyId;
    }

    public void setAudiencePartyId(LongFilter audiencePartyId) {
        this.audiencePartyId = audiencePartyId;
    }

    public LongFilter getParentId() {
        return parentId;
    }

    public LongFilter parentId() {
        if (parentId == null) {
            parentId = new LongFilter();
        }
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CommentCriteria that = (CommentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(rating, that.rating) &&
            Objects.equals(description, that.description) &&
            Objects.equals(childrenId, that.childrenId) &&
            Objects.equals(writerPartyId, that.writerPartyId) &&
            Objects.equals(audiencePartyId, that.audiencePartyId) &&
            Objects.equals(parentId, that.parentId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rating, description, childrenId, writerPartyId, audiencePartyId, parentId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (rating != null ? "rating=" + rating + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (childrenId != null ? "childrenId=" + childrenId + ", " : "") +
            (writerPartyId != null ? "writerPartyId=" + writerPartyId + ", " : "") +
            (audiencePartyId != null ? "audiencePartyId=" + audiencePartyId + ", " : "") +
            (parentId != null ? "parentId=" + parentId + ", " : "") +
            "}";
    }
}
