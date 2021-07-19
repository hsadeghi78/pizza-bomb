package com.barad.bomb.service.dto;

import com.barad.bomb.domain.enumeration.PartyInfoType;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.barad.bomb.domain.PartyInformationEntity} entity.
 */
@ApiModel(description = "4 field fixed")
public class PartyInformationDTO implements Serializable {

    private Long id;

    @NotNull
    private PartyInfoType infoType;

    @NotNull
    @Size(max = 200)
    private String infoTitle;

    @Size(max = 2000)
    private String infoDesc;

    private PartyDTO party;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PartyInfoType getInfoType() {
        return infoType;
    }

    public void setInfoType(PartyInfoType infoType) {
        this.infoType = infoType;
    }

    public String getInfoTitle() {
        return infoTitle;
    }

    public void setInfoTitle(String infoTitle) {
        this.infoTitle = infoTitle;
    }

    public String getInfoDesc() {
        return infoDesc;
    }

    public void setInfoDesc(String infoDesc) {
        this.infoDesc = infoDesc;
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
        if (!(o instanceof PartyInformationDTO)) {
            return false;
        }

        PartyInformationDTO partyInformationDTO = (PartyInformationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, partyInformationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartyInformationDTO{" +
            "id=" + getId() +
            ", infoType='" + getInfoType() + "'" +
            ", infoTitle='" + getInfoTitle() + "'" +
            ", infoDesc='" + getInfoDesc() + "'" +
            ", party=" + getParty() +
            "}";
    }
}
