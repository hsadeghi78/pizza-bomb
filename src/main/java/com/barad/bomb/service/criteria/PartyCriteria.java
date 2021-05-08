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
 * Criteria class for the {@link com.barad.bomb.domain.PartyEntity} entity. This class is used
 * in {@link com.barad.bomb.web.rest.PartyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /parties?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PartyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter partyCode;

    private StringFilter tradeTitle;

    private LocalDateFilter activationDate;

    private LocalDateFilter expirationDate;

    private BooleanFilter activationStatus;

    private StringFilter description;

    private LongFilter branchsId;

    private LongFilter criticismsId;

    public PartyCriteria() {}

    public PartyCriteria(PartyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.partyCode = other.partyCode == null ? null : other.partyCode.copy();
        this.tradeTitle = other.tradeTitle == null ? null : other.tradeTitle.copy();
        this.activationDate = other.activationDate == null ? null : other.activationDate.copy();
        this.expirationDate = other.expirationDate == null ? null : other.expirationDate.copy();
        this.activationStatus = other.activationStatus == null ? null : other.activationStatus.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.branchsId = other.branchsId == null ? null : other.branchsId.copy();
        this.criticismsId = other.criticismsId == null ? null : other.criticismsId.copy();
    }

    @Override
    public PartyCriteria copy() {
        return new PartyCriteria(this);
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

    public StringFilter getPartyCode() {
        return partyCode;
    }

    public StringFilter partyCode() {
        if (partyCode == null) {
            partyCode = new StringFilter();
        }
        return partyCode;
    }

    public void setPartyCode(StringFilter partyCode) {
        this.partyCode = partyCode;
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

    public LocalDateFilter getActivationDate() {
        return activationDate;
    }

    public LocalDateFilter activationDate() {
        if (activationDate == null) {
            activationDate = new LocalDateFilter();
        }
        return activationDate;
    }

    public void setActivationDate(LocalDateFilter activationDate) {
        this.activationDate = activationDate;
    }

    public LocalDateFilter getExpirationDate() {
        return expirationDate;
    }

    public LocalDateFilter expirationDate() {
        if (expirationDate == null) {
            expirationDate = new LocalDateFilter();
        }
        return expirationDate;
    }

    public void setExpirationDate(LocalDateFilter expirationDate) {
        this.expirationDate = expirationDate;
    }

    public BooleanFilter getActivationStatus() {
        return activationStatus;
    }

    public BooleanFilter activationStatus() {
        if (activationStatus == null) {
            activationStatus = new BooleanFilter();
        }
        return activationStatus;
    }

    public void setActivationStatus(BooleanFilter activationStatus) {
        this.activationStatus = activationStatus;
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

    public LongFilter getBranchsId() {
        return branchsId;
    }

    public LongFilter branchsId() {
        if (branchsId == null) {
            branchsId = new LongFilter();
        }
        return branchsId;
    }

    public void setBranchsId(LongFilter branchsId) {
        this.branchsId = branchsId;
    }

    public LongFilter getCriticismsId() {
        return criticismsId;
    }

    public LongFilter criticismsId() {
        if (criticismsId == null) {
            criticismsId = new LongFilter();
        }
        return criticismsId;
    }

    public void setCriticismsId(LongFilter criticismsId) {
        this.criticismsId = criticismsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PartyCriteria that = (PartyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(partyCode, that.partyCode) &&
            Objects.equals(tradeTitle, that.tradeTitle) &&
            Objects.equals(activationDate, that.activationDate) &&
            Objects.equals(expirationDate, that.expirationDate) &&
            Objects.equals(activationStatus, that.activationStatus) &&
            Objects.equals(description, that.description) &&
            Objects.equals(branchsId, that.branchsId) &&
            Objects.equals(criticismsId, that.criticismsId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            partyCode,
            tradeTitle,
            activationDate,
            expirationDate,
            activationStatus,
            description,
            branchsId,
            criticismsId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (partyCode != null ? "partyCode=" + partyCode + ", " : "") +
            (tradeTitle != null ? "tradeTitle=" + tradeTitle + ", " : "") +
            (activationDate != null ? "activationDate=" + activationDate + ", " : "") +
            (expirationDate != null ? "expirationDate=" + expirationDate + ", " : "") +
            (activationStatus != null ? "activationStatus=" + activationStatus + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (branchsId != null ? "branchsId=" + branchsId + ", " : "") +
            (criticismsId != null ? "criticismsId=" + criticismsId + ", " : "") +
            "}";
    }
}
