package com.barad.bomb.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.barad.bomb.domain.MenuItemEntity} entity.
 */
@ApiModel(description = "4 field fixed")
public class MenuItemDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String title;

    @NotNull
    private Instant expirationDate;

    @Size(max = 1000)
    private String description;

    private PartyDTO party;

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

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
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
        if (!(o instanceof MenuItemDTO)) {
            return false;
        }

        MenuItemDTO menuItemDTO = (MenuItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, menuItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MenuItemDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", party=" + getParty() +
            ", food=" + getFood() +
            "}";
    }
}
