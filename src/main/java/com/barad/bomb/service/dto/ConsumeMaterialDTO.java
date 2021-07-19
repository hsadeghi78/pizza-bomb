package com.barad.bomb.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.barad.bomb.domain.ConsumeMaterialEntity} entity.
 */
@ApiModel(description = "4 field fixed")
public class ConsumeMaterialDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String title;

    @Size(max = 100)
    private String type;

    @NotNull
    private Double amount;

    /**
     * noe vahed andazegiri
     */
    @NotNull
    @ApiModelProperty(value = "noe vahed andazegiri", required = true)
    private Long amountUnitClassId;

    private FoodDTO food;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getAmountUnitClassId() {
        return amountUnitClassId;
    }

    public void setAmountUnitClassId(Long amountUnitClassId) {
        this.amountUnitClassId = amountUnitClassId;
    }

    public FoodDTO getFood() {
        return food;
    }

    public void setFood(FoodDTO food) {
        this.food = food;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConsumeMaterialDTO)) {
            return false;
        }

        ConsumeMaterialDTO consumeMaterialDTO = (ConsumeMaterialDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, consumeMaterialDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsumeMaterialDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", type='" + getType() + "'" +
            ", amount=" + getAmount() +
            ", amountUnitClassId=" + getAmountUnitClassId() +
            ", food=" + getFood() +
            "}";
    }
}
