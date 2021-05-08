package com.barad.bomb.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.barad.bomb.domain.PartyEntity} entity.
 */
@ApiModel(description = "4 field fixed")
public class PartyDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String title;

    @NotNull
    @Size(max = 100)
    private String partyCode;

    @NotNull
    @Size(max = 200)
    private String tradeTitle;

    @NotNull
    private LocalDate activationDate;

    private LocalDate expirationDate;

    @NotNull
    private Boolean activationStatus;

    @Size(max = 3000)
    private String description;

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

    public String getPartyCode() {
        return partyCode;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }

    public String getTradeTitle() {
        return tradeTitle;
    }

    public void setTradeTitle(String tradeTitle) {
        this.tradeTitle = tradeTitle;
    }

    public LocalDate getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(LocalDate activationDate) {
        this.activationDate = activationDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getActivationStatus() {
        return activationStatus;
    }

    public void setActivationStatus(Boolean activationStatus) {
        this.activationStatus = activationStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PartyDTO)) {
            return false;
        }

        PartyDTO partyDTO = (PartyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, partyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartyDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", partyCode='" + getPartyCode() + "'" +
            ", tradeTitle='" + getTradeTitle() + "'" +
            ", activationDate='" + getActivationDate() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", activationStatus='" + getActivationStatus() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
