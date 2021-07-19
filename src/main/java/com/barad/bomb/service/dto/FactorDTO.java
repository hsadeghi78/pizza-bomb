package com.barad.bomb.service.dto;

import com.barad.bomb.domain.enumeration.FactorOrderWay;
import com.barad.bomb.domain.enumeration.FactorServing;
import com.barad.bomb.domain.enumeration.FactorStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.barad.bomb.domain.FactorEntity} entity.
 */
@ApiModel(description = "4 field fixed")
public class FactorDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String title;

    @NotNull
    @Size(max = 100)
    private String factorCode;

    @NotNull
    private FactorStatus lastStatus;

    @NotNull
    private FactorOrderWay orderWay;

    @NotNull
    private FactorServing serving;

    /**
     * for FREE, UNPAID, PREPAID_PARTIAL, PREPAID_COMPLETE, POSTPAID, SETTELMENT
     */
    @NotNull
    @ApiModelProperty(value = "for FREE, UNPAID, PREPAID_PARTIAL, PREPAID_COMPLETE, POSTPAID, SETTELMENT", required = true)
    private Long paymentStateClassId;

    /**
     * for *
     */
    @ApiModelProperty(value = "for *")
    private Long categoryClassId;

    @NotNull
    private Double totalPrice;

    private Double discount;

    private Double tax;

    @NotNull
    private Double netprice;

    @Size(max = 1000)
    private String description;

    private PartyDTO buyerParty;

    private PartyDTO sellerParty;

    private AddressDTO deliveryAddress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFactorCode() {
        return factorCode;
    }

    public void setFactorCode(String factorCode) {
        this.factorCode = factorCode;
    }

    public FactorStatus getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(FactorStatus lastStatus) {
        this.lastStatus = lastStatus;
    }

    public FactorOrderWay getOrderWay() {
        return orderWay;
    }

    public void setOrderWay(FactorOrderWay orderWay) {
        this.orderWay = orderWay;
    }

    public FactorServing getServing() {
        return serving;
    }

    public void setServing(FactorServing serving) {
        this.serving = serving;
    }

    public Long getPaymentStateClassId() {
        return paymentStateClassId;
    }

    public void setPaymentStateClassId(Long paymentStateClassId) {
        this.paymentStateClassId = paymentStateClassId;
    }

    public Long getCategoryClassId() {
        return categoryClassId;
    }

    public void setCategoryClassId(Long categoryClassId) {
        this.categoryClassId = categoryClassId;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getNetprice() {
        return netprice;
    }

    public void setNetprice(Double netprice) {
        this.netprice = netprice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PartyDTO getBuyerParty() {
        return buyerParty;
    }

    public void setBuyerParty(PartyDTO buyerParty) {
        this.buyerParty = buyerParty;
    }

    public PartyDTO getSellerParty() {
        return sellerParty;
    }

    public void setSellerParty(PartyDTO sellerParty) {
        this.sellerParty = sellerParty;
    }

    public AddressDTO getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(AddressDTO deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FactorDTO)) {
            return false;
        }

        FactorDTO factorDTO = (FactorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, factorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactorDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", factorCode='" + getFactorCode() + "'" +
            ", lastStatus='" + getLastStatus() + "'" +
            ", orderWay='" + getOrderWay() + "'" +
            ", serving='" + getServing() + "'" +
            ", paymentStateClassId=" + getPaymentStateClassId() +
            ", categoryClassId=" + getCategoryClassId() +
            ", totalPrice=" + getTotalPrice() +
            ", discount=" + getDiscount() +
            ", tax=" + getTax() +
            ", netprice=" + getNetprice() +
            ", description='" + getDescription() + "'" +
            ", buyerParty=" + getBuyerParty() +
            ", sellerParty=" + getSellerParty() +
            ", deliveryAddress=" + getDeliveryAddress() +
            "}";
    }
}
