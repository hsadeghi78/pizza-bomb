package com.barad.bomb.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.barad.bomb.domain.FactorItemEntity} entity.
 */
@ApiModel(description = "4 field fixed")
public class FactorItemDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer rowNum;

    @NotNull
    @Size(max = 200)
    private String title;

    @NotNull
    private Integer count;

    private Double discount;

    private Double tax;

    @Size(max = 300)
    private String description;

    private FoodDTO food;

    private FactorDTO factor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FoodDTO getFood() {
        return food;
    }

    public void setFood(FoodDTO food) {
        this.food = food;
    }

    public FactorDTO getFactor() {
        return factor;
    }

    public void setFactor(FactorDTO factor) {
        this.factor = factor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FactorItemDTO)) {
            return false;
        }

        FactorItemDTO factorItemDTO = (FactorItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, factorItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactorItemDTO{" +
            "id=" + getId() +
            ", rowNum=" + getRowNum() +
            ", title='" + getTitle() + "'" +
            ", count=" + getCount() +
            ", discount=" + getDiscount() +
            ", tax=" + getTax() +
            ", description='" + getDescription() + "'" +
            ", food=" + getFood() +
            ", factor=" + getFactor() +
            "}";
    }
}
