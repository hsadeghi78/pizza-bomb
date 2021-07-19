package com.barad.bomb.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 4 field fixed
 */
@Entity
@Table(name = "price_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PriceHistoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "food_id")
    private Long foodId;

    @Column(name = "material_id")
    private Long materialId;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PriceHistoryEntity id(Long id) {
        this.id = id;
        return this;
    }

    public Long getFoodId() {
        return this.foodId;
    }

    public PriceHistoryEntity foodId(Long foodId) {
        this.foodId = foodId;
        return this;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }

    public Long getMaterialId() {
        return this.materialId;
    }

    public PriceHistoryEntity materialId(Long materialId) {
        this.materialId = materialId;
        return this;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Double getPrice() {
        return this.price;
    }

    public PriceHistoryEntity price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PriceHistoryEntity)) {
            return false;
        }
        return id != null && id.equals(((PriceHistoryEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PriceHistoryEntity{" +
            "id=" + getId() +
            ", foodId=" + getFoodId() +
            ", materialId=" + getMaterialId() +
            ", price=" + getPrice() +
            "}";
    }
}
