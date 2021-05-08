package com.barad.bomb.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.barad.bomb.domain.CriticismEntity} entity.
 */
@ApiModel(description = "4 field fixed")
public class CriticismDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 150)
    private String fullName;

    @Size(max = 150)
    private String email;

    @Size(max = 15)
    private String contactNumber;

    @NotNull
    @Size(max = 3000)
    private String description;

    private PartyDTO party;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
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
        if (!(o instanceof CriticismDTO)) {
            return false;
        }

        CriticismDTO criticismDTO = (CriticismDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, criticismDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CriticismDTO{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", email='" + getEmail() + "'" +
            ", contactNumber='" + getContactNumber() + "'" +
            ", description='" + getDescription() + "'" +
            ", party=" + getParty() +
            "}";
    }
}
