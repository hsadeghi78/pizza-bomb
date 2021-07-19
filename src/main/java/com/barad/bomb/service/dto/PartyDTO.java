package com.barad.bomb.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
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

    @Lob
    private byte[] photo;

    private String photoContentType;

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

    @NotNull
    private Double lat;

    @NotNull
    private Double lon;

    @NotNull
    @Size(max = 3000)
    private String address;

    @NotNull
    @Size(max = 12)
    private String postalCode;

    @NotNull
    @Size(max = 15)
    private String mobile;

    /**
     * for flatOgranization, Horizontal Organization, Legal person, Individual Person
     */
    @NotNull
    @ApiModelProperty(value = "for flatOgranization, Horizontal Organization, Legal person, Individual Person", required = true)
    private Long partyTypeClassId;

    @Size(max = 3000)
    private String description;

    private PartyDTO parent;

    private PartnerDTO partner;

    private PersonDTO person;

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

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getPartyTypeClassId() {
        return partyTypeClassId;
    }

    public void setPartyTypeClassId(Long partyTypeClassId) {
        this.partyTypeClassId = partyTypeClassId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PartyDTO getParent() {
        return parent;
    }

    public void setParent(PartyDTO parent) {
        this.parent = parent;
    }

    public PartnerDTO getPartner() {
        return partner;
    }

    public void setPartner(PartnerDTO partner) {
        this.partner = partner;
    }

    public PersonDTO getPerson() {
        return person;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
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
            ", photo='" + getPhoto() + "'" +
            ", partyCode='" + getPartyCode() + "'" +
            ", tradeTitle='" + getTradeTitle() + "'" +
            ", activationDate='" + getActivationDate() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", activationStatus='" + getActivationStatus() + "'" +
            ", lat=" + getLat() +
            ", lon=" + getLon() +
            ", address='" + getAddress() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", partyTypeClassId=" + getPartyTypeClassId() +
            ", description='" + getDescription() + "'" +
            ", parent=" + getParent() +
            ", partner=" + getPartner() +
            ", person=" + getPerson() +
            "}";
    }
}
