package com.barad.bomb.service.criteria;

import com.barad.bomb.domain.enumeration.FactorOrderWay;
import com.barad.bomb.domain.enumeration.FactorServing;
import com.barad.bomb.domain.enumeration.FactorStatus;
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
 * Criteria class for the {@link com.barad.bomb.domain.FactorEntity} entity. This class is used
 * in {@link com.barad.bomb.web.rest.FactorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /factors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FactorCriteria implements Serializable, Criteria {

    /**
     * Class for filtering FactorStatus
     */
    public static class FactorStatusFilter extends Filter<FactorStatus> {

        public FactorStatusFilter() {}

        public FactorStatusFilter(FactorStatusFilter filter) {
            super(filter);
        }

        @Override
        public FactorStatusFilter copy() {
            return new FactorStatusFilter(this);
        }
    }

    /**
     * Class for filtering FactorOrderWay
     */
    public static class FactorOrderWayFilter extends Filter<FactorOrderWay> {

        public FactorOrderWayFilter() {}

        public FactorOrderWayFilter(FactorOrderWayFilter filter) {
            super(filter);
        }

        @Override
        public FactorOrderWayFilter copy() {
            return new FactorOrderWayFilter(this);
        }
    }

    /**
     * Class for filtering FactorServing
     */
    public static class FactorServingFilter extends Filter<FactorServing> {

        public FactorServingFilter() {}

        public FactorServingFilter(FactorServingFilter filter) {
            super(filter);
        }

        @Override
        public FactorServingFilter copy() {
            return new FactorServingFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter factorCode;

    private FactorStatusFilter lastStatus;

    private FactorOrderWayFilter orderWay;

    private FactorServingFilter serving;

    private LongFilter paymentStateClassId;

    private LongFilter categoryClassId;

    private DoubleFilter totalPrice;

    private DoubleFilter discount;

    private DoubleFilter tax;

    private DoubleFilter netprice;

    private StringFilter description;

    private LongFilter factorItemsId;

    private LongFilter buyerPartyId;

    private LongFilter sellerPartyId;

    private LongFilter deliveryAddressId;

    public FactorCriteria() {}

    public FactorCriteria(FactorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.factorCode = other.factorCode == null ? null : other.factorCode.copy();
        this.lastStatus = other.lastStatus == null ? null : other.lastStatus.copy();
        this.orderWay = other.orderWay == null ? null : other.orderWay.copy();
        this.serving = other.serving == null ? null : other.serving.copy();
        this.paymentStateClassId = other.paymentStateClassId == null ? null : other.paymentStateClassId.copy();
        this.categoryClassId = other.categoryClassId == null ? null : other.categoryClassId.copy();
        this.totalPrice = other.totalPrice == null ? null : other.totalPrice.copy();
        this.discount = other.discount == null ? null : other.discount.copy();
        this.tax = other.tax == null ? null : other.tax.copy();
        this.netprice = other.netprice == null ? null : other.netprice.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.factorItemsId = other.factorItemsId == null ? null : other.factorItemsId.copy();
        this.buyerPartyId = other.buyerPartyId == null ? null : other.buyerPartyId.copy();
        this.sellerPartyId = other.sellerPartyId == null ? null : other.sellerPartyId.copy();
        this.deliveryAddressId = other.deliveryAddressId == null ? null : other.deliveryAddressId.copy();
    }

    @Override
    public FactorCriteria copy() {
        return new FactorCriteria(this);
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

    public StringFilter getFactorCode() {
        return factorCode;
    }

    public StringFilter factorCode() {
        if (factorCode == null) {
            factorCode = new StringFilter();
        }
        return factorCode;
    }

    public void setFactorCode(StringFilter factorCode) {
        this.factorCode = factorCode;
    }

    public FactorStatusFilter getLastStatus() {
        return lastStatus;
    }

    public FactorStatusFilter lastStatus() {
        if (lastStatus == null) {
            lastStatus = new FactorStatusFilter();
        }
        return lastStatus;
    }

    public void setLastStatus(FactorStatusFilter lastStatus) {
        this.lastStatus = lastStatus;
    }

    public FactorOrderWayFilter getOrderWay() {
        return orderWay;
    }

    public FactorOrderWayFilter orderWay() {
        if (orderWay == null) {
            orderWay = new FactorOrderWayFilter();
        }
        return orderWay;
    }

    public void setOrderWay(FactorOrderWayFilter orderWay) {
        this.orderWay = orderWay;
    }

    public FactorServingFilter getServing() {
        return serving;
    }

    public FactorServingFilter serving() {
        if (serving == null) {
            serving = new FactorServingFilter();
        }
        return serving;
    }

    public void setServing(FactorServingFilter serving) {
        this.serving = serving;
    }

    public LongFilter getPaymentStateClassId() {
        return paymentStateClassId;
    }

    public LongFilter paymentStateClassId() {
        if (paymentStateClassId == null) {
            paymentStateClassId = new LongFilter();
        }
        return paymentStateClassId;
    }

    public void setPaymentStateClassId(LongFilter paymentStateClassId) {
        this.paymentStateClassId = paymentStateClassId;
    }

    public LongFilter getCategoryClassId() {
        return categoryClassId;
    }

    public LongFilter categoryClassId() {
        if (categoryClassId == null) {
            categoryClassId = new LongFilter();
        }
        return categoryClassId;
    }

    public void setCategoryClassId(LongFilter categoryClassId) {
        this.categoryClassId = categoryClassId;
    }

    public DoubleFilter getTotalPrice() {
        return totalPrice;
    }

    public DoubleFilter totalPrice() {
        if (totalPrice == null) {
            totalPrice = new DoubleFilter();
        }
        return totalPrice;
    }

    public void setTotalPrice(DoubleFilter totalPrice) {
        this.totalPrice = totalPrice;
    }

    public DoubleFilter getDiscount() {
        return discount;
    }

    public DoubleFilter discount() {
        if (discount == null) {
            discount = new DoubleFilter();
        }
        return discount;
    }

    public void setDiscount(DoubleFilter discount) {
        this.discount = discount;
    }

    public DoubleFilter getTax() {
        return tax;
    }

    public DoubleFilter tax() {
        if (tax == null) {
            tax = new DoubleFilter();
        }
        return tax;
    }

    public void setTax(DoubleFilter tax) {
        this.tax = tax;
    }

    public DoubleFilter getNetprice() {
        return netprice;
    }

    public DoubleFilter netprice() {
        if (netprice == null) {
            netprice = new DoubleFilter();
        }
        return netprice;
    }

    public void setNetprice(DoubleFilter netprice) {
        this.netprice = netprice;
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

    public LongFilter getFactorItemsId() {
        return factorItemsId;
    }

    public LongFilter factorItemsId() {
        if (factorItemsId == null) {
            factorItemsId = new LongFilter();
        }
        return factorItemsId;
    }

    public void setFactorItemsId(LongFilter factorItemsId) {
        this.factorItemsId = factorItemsId;
    }

    public LongFilter getBuyerPartyId() {
        return buyerPartyId;
    }

    public LongFilter buyerPartyId() {
        if (buyerPartyId == null) {
            buyerPartyId = new LongFilter();
        }
        return buyerPartyId;
    }

    public void setBuyerPartyId(LongFilter buyerPartyId) {
        this.buyerPartyId = buyerPartyId;
    }

    public LongFilter getSellerPartyId() {
        return sellerPartyId;
    }

    public LongFilter sellerPartyId() {
        if (sellerPartyId == null) {
            sellerPartyId = new LongFilter();
        }
        return sellerPartyId;
    }

    public void setSellerPartyId(LongFilter sellerPartyId) {
        this.sellerPartyId = sellerPartyId;
    }

    public LongFilter getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public LongFilter deliveryAddressId() {
        if (deliveryAddressId == null) {
            deliveryAddressId = new LongFilter();
        }
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(LongFilter deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FactorCriteria that = (FactorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(factorCode, that.factorCode) &&
            Objects.equals(lastStatus, that.lastStatus) &&
            Objects.equals(orderWay, that.orderWay) &&
            Objects.equals(serving, that.serving) &&
            Objects.equals(paymentStateClassId, that.paymentStateClassId) &&
            Objects.equals(categoryClassId, that.categoryClassId) &&
            Objects.equals(totalPrice, that.totalPrice) &&
            Objects.equals(discount, that.discount) &&
            Objects.equals(tax, that.tax) &&
            Objects.equals(netprice, that.netprice) &&
            Objects.equals(description, that.description) &&
            Objects.equals(factorItemsId, that.factorItemsId) &&
            Objects.equals(buyerPartyId, that.buyerPartyId) &&
            Objects.equals(sellerPartyId, that.sellerPartyId) &&
            Objects.equals(deliveryAddressId, that.deliveryAddressId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            factorCode,
            lastStatus,
            orderWay,
            serving,
            paymentStateClassId,
            categoryClassId,
            totalPrice,
            discount,
            tax,
            netprice,
            description,
            factorItemsId,
            buyerPartyId,
            sellerPartyId,
            deliveryAddressId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactorCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (factorCode != null ? "factorCode=" + factorCode + ", " : "") +
            (lastStatus != null ? "lastStatus=" + lastStatus + ", " : "") +
            (orderWay != null ? "orderWay=" + orderWay + ", " : "") +
            (serving != null ? "serving=" + serving + ", " : "") +
            (paymentStateClassId != null ? "paymentStateClassId=" + paymentStateClassId + ", " : "") +
            (categoryClassId != null ? "categoryClassId=" + categoryClassId + ", " : "") +
            (totalPrice != null ? "totalPrice=" + totalPrice + ", " : "") +
            (discount != null ? "discount=" + discount + ", " : "") +
            (tax != null ? "tax=" + tax + ", " : "") +
            (netprice != null ? "netprice=" + netprice + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (factorItemsId != null ? "factorItemsId=" + factorItemsId + ", " : "") +
            (buyerPartyId != null ? "buyerPartyId=" + buyerPartyId + ", " : "") +
            (sellerPartyId != null ? "sellerPartyId=" + sellerPartyId + ", " : "") +
            (deliveryAddressId != null ? "deliveryAddressId=" + deliveryAddressId + ", " : "") +
            "}";
    }
}
