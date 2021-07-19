package com.barad.bomb.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.barad.bomb.domain.FoodEntity} entity.
 */
@ApiModel(description = "4 field fixed")
public class FoodDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String title;

    @NotNull
    @Size(max = 100)
    private String foodCode;

    private Long sizeClassId;

    @Lob
    private byte[] photo;

    private String photoContentType;

    /**
     * for Appetizer, Main Course, Desert, Drink
     */
    @ApiModelProperty(value = "for Appetizer, Main Course, Desert, Drink")
    private Long categoryClassId;

    @NotNull
    private Double lastPrice;

    @Size(max = 3000)
    private String description;

    private PartyDTO producerParty;

    private PartyDTO designerParty;

    private FoodTypeDTO foodType;

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

    public String getFoodCode() {
        return foodCode;
    }

    public void setFoodCode(String foodCode) {
        this.foodCode = foodCode;
    }

    public Long getSizeClassId() {
        return sizeClassId;
    }

    public void setSizeClassId(Long sizeClassId) {
        this.sizeClassId = sizeClassId;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Long getCategoryClassId() {
        return categoryClassId;
    }

    public void setCategoryClassId(Long categoryClassId) {
        this.categoryClassId = categoryClassId;
    }

    public Double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PartyDTO getProducerParty() {
        return producerParty;
    }

    public void setProducerParty(PartyDTO producerParty) {
        this.producerParty = producerParty;
    }

    public PartyDTO getDesignerParty() {
        return designerParty;
    }

    public void setDesignerParty(PartyDTO designerParty) {
        this.designerParty = designerParty;
    }

    public FoodTypeDTO getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodTypeDTO foodType) {
        this.foodType = foodType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FoodDTO)) {
            return false;
        }

        FoodDTO foodDTO = (FoodDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, foodDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FoodDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", foodCode='" + getFoodCode() + "'" +
            ", sizeClassId=" + getSizeClassId() +
            ", photo='" + getPhoto() + "'" +
            ", categoryClassId=" + getCategoryClassId() +
            ", lastPrice=" + getLastPrice() +
            ", description='" + getDescription() + "'" +
            ", producerParty=" + getProducerParty() +
            ", designerParty=" + getDesignerParty() +
            ", foodType=" + getFoodType() +
            "}";
    }
}
