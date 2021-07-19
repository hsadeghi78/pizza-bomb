package com.barad.bomb.domain;

import com.barad.bomb.domain.enumeration.FactorOrderWay;
import com.barad.bomb.domain.enumeration.FactorServing;
import com.barad.bomb.domain.enumeration.FactorStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 4 field fixed
 */
@Entity
@Table(name = "factor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FactorEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @NotNull
    @Size(max = 100)
    @Column(name = "factor_code", length = 100, nullable = false, unique = true)
    private String factorCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "last_status", nullable = false)
    private FactorStatus lastStatus;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "order_way", nullable = false)
    private FactorOrderWay orderWay;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "serving", nullable = false)
    private FactorServing serving;

    /**
     * for FREE, UNPAID, PREPAID_PARTIAL, PREPAID_COMPLETE, POSTPAID, SETTELMENT
     */
    @NotNull
    @Column(name = "payment_state_class_id", nullable = false)
    private Long paymentStateClassId;

    /**
     * for *
     */
    @Column(name = "category_class_id")
    private Long categoryClassId;

    @NotNull
    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "tax")
    private Double tax;

    @NotNull
    @Column(name = "netprice", nullable = false)
    private Double netprice;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @OneToMany(mappedBy = "factor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "food", "factor" }, allowSetters = true)
    private Set<FactorItemEntity> factorItems = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "criticisms",
            "files",
            "moreInfos",
            "writedComments",
            "audienceComments",
            "foodTypes",
            "children",
            "contacts",
            "addresses",
            "menuItems",
            "produceFoods",
            "designedFoods",
            "buyerFactors",
            "sellerFactors",
            "parent",
            "partner",
            "person",
        },
        allowSetters = true
    )
    private PartyEntity buyerParty;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "criticisms",
            "files",
            "moreInfos",
            "writedComments",
            "audienceComments",
            "foodTypes",
            "children",
            "contacts",
            "addresses",
            "menuItems",
            "produceFoods",
            "designedFoods",
            "buyerFactors",
            "sellerFactors",
            "parent",
            "partner",
            "person",
        },
        allowSetters = true
    )
    private PartyEntity sellerParty;

    @ManyToOne
    @JsonIgnoreProperties(value = { "factors", "party" }, allowSetters = true)
    private AddressEntity deliveryAddress;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FactorEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public FactorEntity title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFactorCode() {
        return this.factorCode;
    }

    public FactorEntity factorCode(String factorCode) {
        this.factorCode = factorCode;
        return this;
    }

    public void setFactorCode(String factorCode) {
        this.factorCode = factorCode;
    }

    public FactorStatus getLastStatus() {
        return this.lastStatus;
    }

    public FactorEntity lastStatus(FactorStatus lastStatus) {
        this.lastStatus = lastStatus;
        return this;
    }

    public void setLastStatus(FactorStatus lastStatus) {
        this.lastStatus = lastStatus;
    }

    public FactorOrderWay getOrderWay() {
        return this.orderWay;
    }

    public FactorEntity orderWay(FactorOrderWay orderWay) {
        this.orderWay = orderWay;
        return this;
    }

    public void setOrderWay(FactorOrderWay orderWay) {
        this.orderWay = orderWay;
    }

    public FactorServing getServing() {
        return this.serving;
    }

    public FactorEntity serving(FactorServing serving) {
        this.serving = serving;
        return this;
    }

    public void setServing(FactorServing serving) {
        this.serving = serving;
    }

    public Long getPaymentStateClassId() {
        return this.paymentStateClassId;
    }

    public FactorEntity paymentStateClassId(Long paymentStateClassId) {
        this.paymentStateClassId = paymentStateClassId;
        return this;
    }

    public void setPaymentStateClassId(Long paymentStateClassId) {
        this.paymentStateClassId = paymentStateClassId;
    }

    public Long getCategoryClassId() {
        return this.categoryClassId;
    }

    public FactorEntity categoryClassId(Long categoryClassId) {
        this.categoryClassId = categoryClassId;
        return this;
    }

    public void setCategoryClassId(Long categoryClassId) {
        this.categoryClassId = categoryClassId;
    }

    public Double getTotalPrice() {
        return this.totalPrice;
    }

    public FactorEntity totalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getDiscount() {
        return this.discount;
    }

    public FactorEntity discount(Double discount) {
        this.discount = discount;
        return this;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getTax() {
        return this.tax;
    }

    public FactorEntity tax(Double tax) {
        this.tax = tax;
        return this;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getNetprice() {
        return this.netprice;
    }

    public FactorEntity netprice(Double netprice) {
        this.netprice = netprice;
        return this;
    }

    public void setNetprice(Double netprice) {
        this.netprice = netprice;
    }

    public String getDescription() {
        return this.description;
    }

    public FactorEntity description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<FactorItemEntity> getFactorItems() {
        return this.factorItems;
    }

    public FactorEntity factorItems(Set<FactorItemEntity> factorItems) {
        this.setFactorItems(factorItems);
        return this;
    }

    public FactorEntity addFactorItems(FactorItemEntity factorItem) {
        this.factorItems.add(factorItem);
        factorItem.setFactor(this);
        return this;
    }

    public FactorEntity removeFactorItems(FactorItemEntity factorItem) {
        this.factorItems.remove(factorItem);
        factorItem.setFactor(null);
        return this;
    }

    public void setFactorItems(Set<FactorItemEntity> factorItems) {
        if (this.factorItems != null) {
            this.factorItems.forEach(i -> i.setFactor(null));
        }
        if (factorItems != null) {
            factorItems.forEach(i -> i.setFactor(this));
        }
        this.factorItems = factorItems;
    }

    public PartyEntity getBuyerParty() {
        return this.buyerParty;
    }

    public FactorEntity buyerParty(PartyEntity party) {
        this.setBuyerParty(party);
        return this;
    }

    public void setBuyerParty(PartyEntity party) {
        this.buyerParty = party;
    }

    public PartyEntity getSellerParty() {
        return this.sellerParty;
    }

    public FactorEntity sellerParty(PartyEntity party) {
        this.setSellerParty(party);
        return this;
    }

    public void setSellerParty(PartyEntity party) {
        this.sellerParty = party;
    }

    public AddressEntity getDeliveryAddress() {
        return this.deliveryAddress;
    }

    public FactorEntity deliveryAddress(AddressEntity address) {
        this.setDeliveryAddress(address);
        return this;
    }

    public void setDeliveryAddress(AddressEntity address) {
        this.deliveryAddress = address;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FactorEntity)) {
            return false;
        }
        return id != null && id.equals(((FactorEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactorEntity{" +
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
            "}";
    }
}
