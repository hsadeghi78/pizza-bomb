package com.barad.bomb.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.barad.bomb.domain.PriceHistoryEntity} entity.
 */
@ApiModel(description = "4 field fixed")
public class PriceHistoryDTO implements Serializable {

    private Long id;

    private Long foodId;

    private Long materialId;

    @NotNull
    private Double price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFoodId() {
        return foodId;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PriceHistoryDTO)) {
            return false;
        }

        PriceHistoryDTO priceHistoryDTO = (PriceHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, priceHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PriceHistoryDTO{" +
            "id=" + getId() +
            ", foodId=" + getFoodId() +
            ", materialId=" + getMaterialId() +
            ", price=" + getPrice() +
            "}";
    }
}
