package com.barad.bomb.service.criteria;

import com.barad.bomb.domain.enumeration.ContactType;
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
 * Criteria class for the {@link com.barad.bomb.domain.ContactEntity} entity. This class is used
 * in {@link com.barad.bomb.web.rest.ContactResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contacts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContactCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ContactType
     */
    public static class ContactTypeFilter extends Filter<ContactType> {

        public ContactTypeFilter() {}

        public ContactTypeFilter(ContactTypeFilter filter) {
            super(filter);
        }

        @Override
        public ContactTypeFilter copy() {
            return new ContactTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private ContactTypeFilter contactType;

    private StringFilter contactValue;

    private LongFilter partyId;

    public ContactCriteria() {}

    public ContactCriteria(ContactCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.contactType = other.contactType == null ? null : other.contactType.copy();
        this.contactValue = other.contactValue == null ? null : other.contactValue.copy();
        this.partyId = other.partyId == null ? null : other.partyId.copy();
    }

    @Override
    public ContactCriteria copy() {
        return new ContactCriteria(this);
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

    public ContactTypeFilter getContactType() {
        return contactType;
    }

    public ContactTypeFilter contactType() {
        if (contactType == null) {
            contactType = new ContactTypeFilter();
        }
        return contactType;
    }

    public void setContactType(ContactTypeFilter contactType) {
        this.contactType = contactType;
    }

    public StringFilter getContactValue() {
        return contactValue;
    }

    public StringFilter contactValue() {
        if (contactValue == null) {
            contactValue = new StringFilter();
        }
        return contactValue;
    }

    public void setContactValue(StringFilter contactValue) {
        this.contactValue = contactValue;
    }

    public LongFilter getPartyId() {
        return partyId;
    }

    public LongFilter partyId() {
        if (partyId == null) {
            partyId = new LongFilter();
        }
        return partyId;
    }

    public void setPartyId(LongFilter partyId) {
        this.partyId = partyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ContactCriteria that = (ContactCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(contactType, that.contactType) &&
            Objects.equals(contactValue, that.contactValue) &&
            Objects.equals(partyId, that.partyId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, contactType, contactValue, partyId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (contactType != null ? "contactType=" + contactType + ", " : "") +
            (contactValue != null ? "contactValue=" + contactValue + ", " : "") +
            (partyId != null ? "partyId=" + partyId + ", " : "") +
            "}";
    }
}
