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
 * Criteria class for the {@link com.barad.bomb.domain.BranchEntity} entity. This class is used
 * in {@link com.barad.bomb.web.rest.BranchResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /branches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BranchCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter branchCode;

    private StringFilter tradeTitle;

    private LocalDateFilter activationDate;

    private LocalDateFilter expirationDate;

    private BooleanFilter activationStatus;

    private DoubleFilter lat;

    private StringFilter address;

    private StringFilter postalCode;

    private StringFilter description;

    private LongFilter partyId;

    public BranchCriteria() {}

    public BranchCriteria(BranchCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.branchCode = other.branchCode == null ? null : other.branchCode.copy();
        this.tradeTitle = other.tradeTitle == null ? null : other.tradeTitle.copy();
        this.activationDate = other.activationDate == null ? null : other.activationDate.copy();
        this.expirationDate = other.expirationDate == null ? null : other.expirationDate.copy();
        this.activationStatus = other.activationStatus == null ? null : other.activationStatus.copy();
        this.lat = other.lat == null ? null : other.lat.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.postalCode = other.postalCode == null ? null : other.postalCode.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.partyId = other.partyId == null ? null : other.partyId.copy();
    }

    @Override
    public BranchCriteria copy() {
        return new BranchCriteria(this);
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

    public StringFilter getBranchCode() {
        return branchCode;
    }

    public StringFilter branchCode() {
        if (branchCode == null) {
            branchCode = new StringFilter();
        }
        return branchCode;
    }

    public void setBranchCode(StringFilter branchCode) {
        this.branchCode = branchCode;
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

    public DoubleFilter getLat() {
        return lat;
    }

    public DoubleFilter lat() {
        if (lat == null) {
            lat = new DoubleFilter();
        }
        return lat;
    }

    public void setLat(DoubleFilter lat) {
        this.lat = lat;
    }

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getPostalCode() {
        return postalCode;
    }

    public StringFilter postalCode() {
        if (postalCode == null) {
            postalCode = new StringFilter();
        }
        return postalCode;
    }

    public void setPostalCode(StringFilter postalCode) {
        this.postalCode = postalCode;
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
        final BranchCriteria that = (BranchCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(branchCode, that.branchCode) &&
            Objects.equals(tradeTitle, that.tradeTitle) &&
            Objects.equals(activationDate, that.activationDate) &&
            Objects.equals(expirationDate, that.expirationDate) &&
            Objects.equals(activationStatus, that.activationStatus) &&
            Objects.equals(lat, that.lat) &&
            Objects.equals(address, that.address) &&
            Objects.equals(postalCode, that.postalCode) &&
            Objects.equals(description, that.description) &&
            Objects.equals(partyId, that.partyId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            branchCode,
            tradeTitle,
            activationDate,
            expirationDate,
            activationStatus,
            lat,
            address,
            postalCode,
            description,
            partyId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BranchCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (branchCode != null ? "branchCode=" + branchCode + ", " : "") +
            (tradeTitle != null ? "tradeTitle=" + tradeTitle + ", " : "") +
            (activationDate != null ? "activationDate=" + activationDate + ", " : "") +
            (expirationDate != null ? "expirationDate=" + expirationDate + ", " : "") +
            (activationStatus != null ? "activationStatus=" + activationStatus + ", " : "") +
            (lat != null ? "lat=" + lat + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (postalCode != null ? "postalCode=" + postalCode + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (partyId != null ? "partyId=" + partyId + ", " : "") +
            "}";
    }
}
