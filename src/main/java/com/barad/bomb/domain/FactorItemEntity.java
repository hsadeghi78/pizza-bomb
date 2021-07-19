package com.barad.bomb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 4 field fixed
 */
@Entity
@Table(name = "factor_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FactorItemEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "row_num", nullable = false)
    private Integer rowNum;

    @NotNull
    @Size(max = 200)
    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @NotNull
    @Column(name = "count", nullable = false)
    private Integer count;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "tax")
    private Double tax;

    @Size(max = 300)
    @Column(name = "description", length = 300)
    private String description;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "menuItems", "factorItems", "materials", "producerParty", "designerParty", "foodType" },
        allowSetters = true
    )
    private FoodEntity food;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "factorItems", "buyerParty", "sellerParty", "deliveryAddress" }, allowSetters = true)
    private FactorEntity factor;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FactorItemEntity id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRowNum() {
        return this.rowNum;
    }

    public FactorItemEntity rowNum(Integer rowNum) {
        this.rowNum = rowNum;
        return this;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getTitle() {
        return this.title;
    }

    public FactorItemEntity title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCount() {
        return this.count;
    }

    public FactorItemEntity count(Integer count) {
        this.count = count;
        return this;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getDiscount() {
        return this.discount;
    }

    public FactorItemEntity discount(Double discount) {
        this.discount = discount;
        return this;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getTax() {
        return this.tax;
    }

    public FactorItemEntity tax(Double tax) {
        this.tax = tax;
        return this;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public String getDescription() {
        return this.description;
    }

    public FactorItemEntity description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FoodEntity getFood() {
        return this.food;
    }

    public FactorItemEntity food(FoodEntity food) {
        this.setFood(food);
        return this;
    }

    public void setFood(FoodEntity food) {
        this.food = food;
    }

    public FactorEntity getFactor() {
        return this.factor;
    }

    public FactorItemEntity factor(FactorEntity factor) {
        this.setFactor(factor);
        return this;
    }

    public void setFactor(FactorEntity factor) {
        this.factor = factor;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FactorItemEntity)) {
            return false;
        }
        return id != null && id.equals(((FactorItemEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactorItemEntity{" +
            "id=" + getId() +
            ", rowNum=" + getRowNum() +
            ", title='" + getTitle() + "'" +
            ", count=" + getCount() +
            ", discount=" + getDiscount() +
            ", tax=" + getTax() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
