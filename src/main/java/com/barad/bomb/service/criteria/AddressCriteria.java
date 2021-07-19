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
 * Criteria class for the {@link com.barad.bomb.domain.AddressEntity} entity. This class is used
 * in {@link com.barad.bomb.web.rest.AddressResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /addresses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AddressCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private DoubleFilter lat;

    private DoubleFilter lon;

    private StringFilter street1;

    private StringFilter street2;

    private StringFilter address;

    private StringFilter postalCode;

    private LongFilter factorsId;

    private LongFilter partyId;

    public AddressCriteria() {}

    public AddressCriteria(AddressCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.lat = other.lat == null ? null : other.lat.copy();
        this.lon = other.lon == null ? null : other.lon.copy();
        this.street1 = other.street1 == null ? null : other.street1.copy();
        this.street2 = other.street2 == null ? null : other.street2.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.postalCode = other.postalCode == null ? null : other.postalCode.copy();
        this.factorsId = other.factorsId == null ? null : other.factorsId.copy();
        this.partyId = other.partyId == null ? null : other.partyId.copy();
    }

    @Override
    public AddressCriteria copy() {
        return new AddressCriteria(this);
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

    public DoubleFilter getLon() {
        return lon;
    }

    public DoubleFilter lon() {
        if (lon == null) {
            lon = new DoubleFilter();
        }
        return lon;
    }

    public void setLon(DoubleFilter lon) {
        this.lon = lon;
    }

    public StringFilter getStreet1() {
        return street1;
    }

    public StringFilter street1() {
        if (street1 == null) {
            street1 = new StringFilter();
        }
        return street1;
    }

    public void setStreet1(StringFilter street1) {
        this.street1 = street1;
    }

    public StringFilter getStreet2() {
        return street2;
    }

    public StringFilter street2() {
        if (street2 == null) {
            street2 = new StringFilter();
        }
        return street2;
    }

    public void setStreet2(StringFilter street2) {
        this.street2 = street2;
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

    public LongFilter getFactorsId() {
        return factorsId;
    }

    public LongFilter factorsId() {
        if (factorsId == null) {
            factorsId = new LongFilter();
        }
        return factorsId;
    }

    public void setFactorsId(LongFilter factorsId) {
        this.factorsId = factorsId;
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
        final AddressCriteria that = (AddressCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(lat, that.lat) &&
            Objects.equals(lon, that.lon) &&
            Objects.equals(street1, that.street1) &&
            Objects.equals(street2, that.street2) &&
            Objects.equals(address, that.address) &&
            Objects.equals(postalCode, that.postalCode) &&
            Objects.equals(factorsId, that.factorsId) &&
            Objects.equals(partyId, that.partyId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, lat, lon, street1, street2, address, postalCode, factorsId, partyId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AddressCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (lat != null ? "lat=" + lat + ", " : "") +
            (lon != null ? "lon=" + lon + ", " : "") +
            (street1 != null ? "street1=" + street1 + ", " : "") +
            (street2 != null ? "street2=" + street2 + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (postalCode != null ? "postalCode=" + postalCode + ", " : "") +
            (factorsId != null ? "factorsId=" + factorsId + ", " : "") +
            (partyId != null ? "partyId=" + partyId + ", " : "") +
            "}";
    }
}
