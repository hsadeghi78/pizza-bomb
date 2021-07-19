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
 * Criteria class for the {@link com.barad.bomb.domain.PartnerEntity} entity. This class is used
 * in {@link com.barad.bomb.web.rest.PartnerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /partners?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PartnerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter partnerCode;

    private StringFilter tradeTitle;

    private StringFilter economicCode;

    private LocalDateFilter activityDate;

    private LongFilter partiesId;

    public PartnerCriteria() {}

    public PartnerCriteria(PartnerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.partnerCode = other.partnerCode == null ? null : other.partnerCode.copy();
        this.tradeTitle = other.tradeTitle == null ? null : other.tradeTitle.copy();
        this.economicCode = other.economicCode == null ? null : other.economicCode.copy();
        this.activityDate = other.activityDate == null ? null : other.activityDate.copy();
        this.partiesId = other.partiesId == null ? null : other.partiesId.copy();
    }

    @Override
    public PartnerCriteria copy() {
        return new PartnerCriteria(this);
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

    public StringFilter getPartnerCode() {
        return partnerCode;
    }

    public StringFilter partnerCode() {
        if (partnerCode == null) {
            partnerCode = new StringFilter();
        }
        return partnerCode;
    }

    public void setPartnerCode(StringFilter partnerCode) {
        this.partnerCode = partnerCode;
    }

    public StringFilter getTradeTitle() {
        return tradeTitle;
    }

    public StringFilter tradeTitle() {
        if (tradeTitle == null) {
            tradeTitle = new StringFilter();
        }
        return tradeTitle;
    }

    public void setTradeTitle(StringFilter tradeTitle) {
        this.tradeTitle = tradeTitle;
    }

    public StringFilter getEconomicCode() {
        return economicCode;
    }

    public StringFilter economicCode() {
        if (economicCode == null) {
            economicCode = new StringFilter();
        }
        return economicCode;
    }

    public void setEconomicCode(StringFilter economicCode) {
        this.economicCode = economicCode;
    }

    public LocalDateFilter getActivityDate() {
        return activityDate;
    }

    public LocalDateFilter activityDate() {
        if (activityDate == null) {
            activityDate = new LocalDateFilter();
        }
        return activityDate;
    }

    public void setActivityDate(LocalDateFilter activityDate) {
        this.activityDate = activityDate;
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
        final PartnerCriteria that = (PartnerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(partnerCode, that.partnerCode) &&
            Objects.equals(tradeTitle, that.tradeTitle) &&
            Objects.equals(economicCode, that.economicCode) &&
            Objects.equals(activityDate, that.activityDate) &&
            Objects.equals(partiesId, that.partiesId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, partnerCode, tradeTitle, economicCode, activityDate, partiesId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartnerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (partnerCode != null ? "partnerCode=" + partnerCode + ", " : "") +
            (tradeTitle != null ? "tradeTitle=" + tradeTitle + ", " : "") +
            (economicCode != null ? "economicCode=" + economicCode + ", " : "") +
            (activityDate != null ? "activityDate=" + activityDate + ", " : "") +
            (partiesId != null ? "partiesId=" + partiesId + ", " : "") +
            "}";
    }
}
