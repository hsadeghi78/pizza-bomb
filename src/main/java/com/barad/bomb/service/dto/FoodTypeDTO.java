package com.barad.bomb.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.barad.bomb.domain.FoodTypeEntity} entity.
 */
@ApiModel(description = "4 field fixed sample:PIZZA, SANDWICH, SNACK, BURGER, SOKHARI")
public class FoodTypeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String title;

    @NotNull
    @Size(max = 50)
    private String typeCode;

    @Size(max = 3000)
    private String description;

    private PartyDTO party;

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

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PartyDTO getParty() {
        return party;
    }

    public void setParty(PartyDTO party) {
        this.party = party;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FoodTypeDTO)) {
            return false;
        }

        FoodTypeDTO foodTypeDTO = (FoodTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, foodTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FoodTypeDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", typeCode='" + getTypeCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", party=" + getParty() +
            "}";
    }
}
