package com.barad.bomb.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.barad.bomb.domain.PartnerEntity} entity.
 */
@ApiModel(description = "4 field fixed")
public class PartnerDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String title;

    @NotNull
    @Size(max = 100)
    private String partnerCode;

    @NotNull
    @Size(max = 200)
    private String tradeTitle;

    @Size(max = 100)
    private String economicCode;

    private LocalDate activityDate;

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

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getTradeTitle() {
        return tradeTitle;
    }

    public void setTradeTitle(String tradeTitle) {
        this.tradeTitle = tradeTitle;
    }

    public String getEconomicCode() {
        return economicCode;
    }

    public void setEconomicCode(String economicCode) {
        this.economicCode = economicCode;
    }

    public LocalDate getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(LocalDate activityDate) {
        this.activityDate = activityDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PartnerDTO)) {
            return false;
        }

        PartnerDTO partnerDTO = (PartnerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, partnerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartnerDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", partnerCode='" + getPartnerCode() + "'" +
            ", tradeTitle='" + getTradeTitle() + "'" +
            ", economicCode='" + getEconomicCode() + "'" +
            ", activityDate='" + getActivityDate() + "'" +
            "}";
    }
}
