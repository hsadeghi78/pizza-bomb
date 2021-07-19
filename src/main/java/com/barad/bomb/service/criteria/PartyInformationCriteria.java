package com.barad.bomb.service.criteria;

import com.barad.bomb.domain.enumeration.PartyInfoType;
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
 * Criteria class for the {@link com.barad.bomb.domain.PartyInformationEntity} entity. This class is used
 * in {@link com.barad.bomb.web.rest.PartyInformationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /party-informations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PartyInformationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PartyInfoType
     */
    public static class PartyInfoTypeFilter extends Filter<PartyInfoType> {

        public PartyInfoTypeFilter() {}

        public PartyInfoTypeFilter(PartyInfoTypeFilter filter) {
            super(filter);
        }

        @Override
        public PartyInfoTypeFilter copy() {
            return new PartyInfoTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private PartyInfoTypeFilter infoType;

    private StringFilter infoTitle;

    private StringFilter infoDesc;

    private LongFilter partyId;

    public PartyInformationCriteria() {}

    public PartyInformationCriteria(PartyInformationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.infoType = other.infoType == null ? null : other.infoType.copy();
        this.infoTitle = other.infoTitle == null ? null : other.infoTitle.copy();
        this.infoDesc = other.infoDesc == null ? null : other.infoDesc.copy();
        this.partyId = other.partyId == null ? null : other.partyId.copy();
    }

    @Override
    public PartyInformationCriteria copy() {
        return new PartyInformationCriteria(this);
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

    public PartyInfoTypeFilter getInfoType() {
        return infoType;
    }

    public PartyInfoTypeFilter infoType() {
        if (infoType == null) {
            infoType = new PartyInfoTypeFilter();
        }
        return infoType;
    }

    public void setInfoType(PartyInfoTypeFilter infoType) {
        this.infoType = infoType;
    }

    public StringFilter getInfoTitle() {
        return infoTitle;
    }

    public StringFilter infoTitle() {
        if (infoTitle == null) {
            infoTitle = new StringFilter();
        }
        return infoTitle;
    }

    public void setInfoTitle(StringFilter infoTitle) {
        this.infoTitle = infoTitle;
    }

    public StringFilter getInfoDesc() {
        return infoDesc;
    }

    public StringFilter infoDesc() {
        if (infoDesc == null) {
            infoDesc = new StringFilter();
        }
        return infoDesc;
    }

    public void setInfoDesc(StringFilter infoDesc) {
        this.infoDesc = infoDesc;
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
        final PartyInformationCriteria that = (PartyInformationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(infoType, that.infoType) &&
            Objects.equals(infoTitle, that.infoTitle) &&
            Objects.equals(infoDesc, that.infoDesc) &&
            Objects.equals(partyId, that.partyId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, infoType, infoTitle, infoDesc, partyId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartyInformationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (infoType != null ? "infoType=" + infoType + ", " : "") +
            (infoTitle != null ? "infoTitle=" + infoTitle + ", " : "") +
            (infoDesc != null ? "infoDesc=" + infoDesc + ", " : "") +
            (partyId != null ? "partyId=" + partyId + ", " : "") +
            "}";
    }
}
