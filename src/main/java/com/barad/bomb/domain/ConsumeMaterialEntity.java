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
@Table(name = "consume_material")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ConsumeMaterialEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Size(max = 100)
    @Column(name = "type", length = 100)
    private String type;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    /**
     * noe vahed andazegiri
     */
    @NotNull
    @Column(name = "amount_unit_class_id", nullable = false)
    private Long amountUnitClassId;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "menuItems", "factorItems", "materials", "producerParty", "designerParty", "foodType" },
        allowSetters = true
    )
    private FoodEntity food;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ConsumeMaterialEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public ConsumeMaterialEntity title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return this.type;
    }

    public ConsumeMaterialEntity type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return this.amount;
    }

    public ConsumeMaterialEntity amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getAmountUnitClassId() {
        return this.amountUnitClassId;
    }

    public ConsumeMaterialEntity amountUnitClassId(Long amountUnitClassId) {
        this.amountUnitClassId = amountUnitClassId;
        return this;
    }

    public void setAmountUnitClassId(Long amountUnitClassId) {
        this.amountUnitClassId = amountUnitClassId;
    }

    public FoodEntity getFood() {
        return this.food;
    }

    public ConsumeMaterialEntity food(FoodEntity food) {
        this.setFood(food);
        return this;
    }

    public void setFood(FoodEntity food) {
        this.food = food;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConsumeMaterialEntity)) {
            return false;
        }
        return id != null && id.equals(((ConsumeMaterialEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsumeMaterialEntity{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", type='" + getType() + "'" +
            ", amount=" + getAmount() +
            ", amountUnitClassId=" + getAmountUnitClassId() +
            "}";
    }
}
