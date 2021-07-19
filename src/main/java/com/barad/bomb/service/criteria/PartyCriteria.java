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

    private DoubleFilter lat;

    private DoubleFilter lon;

    private StringFilter address;

    private StringFilter postalCode;

    private StringFilter mobile;

    private LongFilter partyTypeClassId;

    private StringFilter description;

    private LongFilter criticismsId;

    private LongFilter filesId;

    private LongFilter moreInfoId;

    private LongFilter writedCommentsId;

    private LongFilter audienceCommentsId;

    private LongFilter foodTypesId;

    private LongFilter childrenId;

    private LongFilter contactsId;

    private LongFilter addressesId;

    private LongFilter menuItemsId;

    private LongFilter produceFoodsId;

    private LongFilter designedFoodsId;

    private LongFilter buyerFactorsId;

    private LongFilter sellerFactorsId;

    private LongFilter parentId;

    private LongFilter partnerId;

    private LongFilter personId;

    public PartyCriteria() {}

    public PartyCriteria(PartyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.partyCode = other.partyCode == null ? null : other.partyCode.copy();
        this.tradeTitle = other.tradeTitle == null ? null : other.tradeTitle.copy();
        this.activationDate = other.activationDate == null ? null : other.activationDate.copy();
        this.expirationDate = other.expirationDate == null ? null : other.expirationDate.copy();
        this.activationStatus = other.activationStatus == null ? null : other.activationStatus.copy();
        this.lat = other.lat == null ? null : other.lat.copy();
        this.lon = other.lon == null ? null : other.lon.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.postalCode = other.postalCode == null ? null : other.postalCode.copy();
        this.mobile = other.mobile == null ? null : other.mobile.copy();
        this.partyTypeClassId = other.partyTypeClassId == null ? null : other.partyTypeClassId.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.criticismsId = other.criticismsId == null ? null : other.criticismsId.copy();
        this.filesId = other.filesId == null ? null : other.filesId.copy();
        this.moreInfoId = other.moreInfoId == null ? null : other.moreInfoId.copy();
        this.writedCommentsId = other.writedCommentsId == null ? null : other.writedCommentsId.copy();
        this.audienceCommentsId = other.audienceCommentsId == null ? null : other.audienceCommentsId.copy();
        this.foodTypesId = other.foodTypesId == null ? null : other.foodTypesId.copy();
        this.childrenId = other.childrenId == null ? null : other.childrenId.copy();
        this.contactsId = other.contactsId == null ? null : other.contactsId.copy();
        this.addressesId = other.addressesId == null ? null : other.addressesId.copy();
        this.menuItemsId = other.menuItemsId == null ? null : other.menuItemsId.copy();
        this.produceFoodsId = other.produceFoodsId == null ? null : other.produceFoodsId.copy();
        this.designedFoodsId = other.designedFoodsId == null ? null : other.designedFoodsId.copy();
        this.buyerFactorsId = other.buyerFactorsId == null ? null : other.buyerFactorsId.copy();
        this.sellerFactorsId = other.sellerFactorsId == null ? null : other.sellerFactorsId.copy();
        this.parentId = other.parentId == null ? null : other.parentId.copy();
        this.partnerId = other.partnerId == null ? null : other.partnerId.copy();
        this.personId = other.personId == null ? null : other.personId.copy();
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

    public StringFilter getMobile() {
        return mobile;
    }

    public StringFilter mobile() {
        if (mobile == null) {
            mobile = new StringFilter();
        }
        return mobile;
    }

    public void setMobile(StringFilter mobile) {
        this.mobile = mobile;
    }

    public LongFilter getPartyTypeClassId() {
        return partyTypeClassId;
    }

    public LongFilter partyTypeClassId() {
        if (partyTypeClassId == null) {
            partyTypeClassId = new LongFilter();
        }
        return partyTypeClassId;
    }

    public void setPartyTypeClassId(LongFilter partyTypeClassId) {
        this.partyTypeClassId = partyTypeClassId;
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

    public LongFilter getFilesId() {
        return filesId;
    }

    public LongFilter filesId() {
        if (filesId == null) {
            filesId = new LongFilter();
        }
        return filesId;
    }

    public void setFilesId(LongFilter filesId) {
        this.filesId = filesId;
    }

    public LongFilter getMoreInfoId() {
        return moreInfoId;
    }

    public LongFilter moreInfoId() {
        if (moreInfoId == null) {
            moreInfoId = new LongFilter();
        }
        return moreInfoId;
    }

    public void setMoreInfoId(LongFilter moreInfoId) {
        this.moreInfoId = moreInfoId;
    }

    public LongFilter getWritedCommentsId() {
        return writedCommentsId;
    }

    public LongFilter writedCommentsId() {
        if (writedCommentsId == null) {
            writedCommentsId = new LongFilter();
        }
        return writedCommentsId;
    }

    public void setWritedCommentsId(LongFilter writedCommentsId) {
        this.writedCommentsId = writedCommentsId;
    }

    public LongFilter getAudienceCommentsId() {
        return audienceCommentsId;
    }

    public LongFilter audienceCommentsId() {
        if (audienceCommentsId == null) {
            audienceCommentsId = new LongFilter();
        }
        return audienceCommentsId;
    }

    public void setAudienceCommentsId(LongFilter audienceCommentsId) {
        this.audienceCommentsId = audienceCommentsId;
    }

    public LongFilter getFoodTypesId() {
        return foodTypesId;
    }

    public LongFilter foodTypesId() {
        if (foodTypesId == null) {
            foodTypesId = new LongFilter();
        }
        return foodTypesId;
    }

    public void setFoodTypesId(LongFilter foodTypesId) {
        this.foodTypesId = foodTypesId;
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

    public LongFilter getContactsId() {
        return contactsId;
    }

    public LongFilter contactsId() {
        if (contactsId == null) {
            contactsId = new LongFilter();
        }
        return contactsId;
    }

    public void setContactsId(LongFilter contactsId) {
        this.contactsId = contactsId;
    }

    public LongFilter getAddressesId() {
        return addressesId;
    }

    public LongFilter addressesId() {
        if (addressesId == null) {
            addressesId = new LongFilter();
        }
        return addressesId;
    }

    public void setAddressesId(LongFilter addressesId) {
        this.addressesId = addressesId;
    }

    public LongFilter getMenuItemsId() {
        return menuItemsId;
    }

    public LongFilter menuItemsId() {
        if (menuItemsId == null) {
            menuItemsId = new LongFilter();
        }
        return menuItemsId;
    }

    public void setMenuItemsId(LongFilter menuItemsId) {
        this.menuItemsId = menuItemsId;
    }

    public LongFilter getProduceFoodsId() {
        return produceFoodsId;
    }

    public LongFilter produceFoodsId() {
        if (produceFoodsId == null) {
            produceFoodsId = new LongFilter();
        }
        return produceFoodsId;
    }

    public void setProduceFoodsId(LongFilter produceFoodsId) {
        this.produceFoodsId = produceFoodsId;
    }

    public LongFilter getDesignedFoodsId() {
        return designedFoodsId;
    }

    public LongFilter designedFoodsId() {
        if (designedFoodsId == null) {
            designedFoodsId = new LongFilter();
        }
        return designedFoodsId;
    }

    public void setDesignedFoodsId(LongFilter designedFoodsId) {
        this.designedFoodsId = designedFoodsId;
    }

    public LongFilter getBuyerFactorsId() {
        return buyerFactorsId;
    }

    public LongFilter buyerFactorsId() {
        if (buyerFactorsId == null) {
            buyerFactorsId = new LongFilter();
        }
        return buyerFactorsId;
    }

    public void setBuyerFactorsId(LongFilter buyerFactorsId) {
        this.buyerFactorsId = buyerFactorsId;
    }

    public LongFilter getSellerFactorsId() {
        return sellerFactorsId;
    }

    public LongFilter sellerFactorsId() {
        if (sellerFactorsId == null) {
            sellerFactorsId = new LongFilter();
        }
        return sellerFactorsId;
    }

    public void setSellerFactorsId(LongFilter sellerFactorsId) {
        this.sellerFactorsId = sellerFactorsId;
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

    public LongFilter getPartnerId() {
        return partnerId;
    }

    public LongFilter partnerId() {
        if (partnerId == null) {
            partnerId = new LongFilter();
        }
        return partnerId;
    }

    public void setPartnerId(LongFilter partnerId) {
        this.partnerId = partnerId;
    }

    public LongFilter getPersonId() {
        return personId;
    }

    public LongFilter personId() {
        if (personId == null) {
            personId = new LongFilter();
        }
        return personId;
    }

    public void setPersonId(LongFilter personId) {
        this.personId = personId;
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
            Objects.equals(lat, that.lat) &&
            Objects.equals(lon, that.lon) &&
            Objects.equals(address, that.address) &&
            Objects.equals(postalCode, that.postalCode) &&
            Objects.equals(mobile, that.mobile) &&
            Objects.equals(partyTypeClassId, that.partyTypeClassId) &&
            Objects.equals(description, that.description) &&
            Objects.equals(criticismsId, that.criticismsId) &&
            Objects.equals(filesId, that.filesId) &&
            Objects.equals(moreInfoId, that.moreInfoId) &&
            Objects.equals(writedCommentsId, that.writedCommentsId) &&
            Objects.equals(audienceCommentsId, that.audienceCommentsId) &&
            Objects.equals(foodTypesId, that.foodTypesId) &&
            Objects.equals(childrenId, that.childrenId) &&
            Objects.equals(contactsId, that.contactsId) &&
            Objects.equals(addressesId, that.addressesId) &&
            Objects.equals(menuItemsId, that.menuItemsId) &&
            Objects.equals(produceFoodsId, that.produceFoodsId) &&
            Objects.equals(designedFoodsId, that.designedFoodsId) &&
            Objects.equals(buyerFactorsId, that.buyerFactorsId) &&
            Objects.equals(sellerFactorsId, that.sellerFactorsId) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(partnerId, that.partnerId) &&
            Objects.equals(personId, that.personId)
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
            lat,
            lon,
            address,
            postalCode,
            mobile,
            partyTypeClassId,
            description,
            criticismsId,
            filesId,
            moreInfoId,
            writedCommentsId,
            audienceCommentsId,
            foodTypesId,
            childrenId,
            contactsId,
            addressesId,
            menuItemsId,
            produceFoodsId,
            designedFoodsId,
            buyerFactorsId,
            sellerFactorsId,
            parentId,
            partnerId,
            personId
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
            (lat != null ? "lat=" + lat + ", " : "") +
            (lon != null ? "lon=" + lon + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (postalCode != null ? "postalCode=" + postalCode + ", " : "") +
            (mobile != null ? "mobile=" + mobile + ", " : "") +
            (partyTypeClassId != null ? "partyTypeClassId=" + partyTypeClassId + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (criticismsId != null ? "criticismsId=" + criticismsId + ", " : "") +
            (filesId != null ? "filesId=" + filesId + ", " : "") +
            (moreInfoId != null ? "moreInfoId=" + moreInfoId + ", " : "") +
            (writedCommentsId != null ? "writedCommentsId=" + writedCommentsId + ", " : "") +
            (audienceCommentsId != null ? "audienceCommentsId=" + audienceCommentsId + ", " : "") +
            (foodTypesId != null ? "foodTypesId=" + foodTypesId + ", " : "") +
            (childrenId != null ? "childrenId=" + childrenId + ", " : "") +
            (contactsId != null ? "contactsId=" + contactsId + ", " : "") +
            (addressesId != null ? "addressesId=" + addressesId + ", " : "") +
            (menuItemsId != null ? "menuItemsId=" + menuItemsId + ", " : "") +
            (produceFoodsId != null ? "produceFoodsId=" + produceFoodsId + ", " : "") +
            (designedFoodsId != null ? "designedFoodsId=" + designedFoodsId + ", " : "") +
            (buyerFactorsId != null ? "buyerFactorsId=" + buyerFactorsId + ", " : "") +
            (sellerFactorsId != null ? "sellerFactorsId=" + sellerFactorsId + ", " : "") +
            (parentId != null ? "parentId=" + parentId + ", " : "") +
            (partnerId != null ? "partnerId=" + partnerId + ", " : "") +
            (personId != null ? "personId=" + personId + ", " : "") +
            "}";
    }
}
