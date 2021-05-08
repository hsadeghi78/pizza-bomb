package com.barad.bomb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 4 field fixed
 */
@Entity
@Table(name = "branch")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BranchEntity extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @NotNull
    @Size(max = 100)
    @Column(name = "branch_code", length = 100, nullable = false, unique = true)
    private String branchCode;

    @NotNull
    @Size(max = 200)
    @Column(name = "trade_title", length = 200, nullable = false, unique = true)
    private String tradeTitle;

    @NotNull
    @Column(name = "activation_date", nullable = false)
    private LocalDate activationDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @NotNull
    @Column(name = "activation_status", nullable = false)
    private Boolean activationStatus;

    @NotNull
    @Column(name = "lat", nullable = false)
    private Double lat;

    @NotNull
    @Size(max = 3000)
    @Column(name = "address", length = 3000, nullable = false)
    private String address;

    @NotNull
    @Size(max = 12)
    @Column(name = "postal_code", length = 12, nullable = false)
    private String postalCode;

    @Size(max = 3000)
    @Column(name = "description", length = 3000)
    private String description;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "branchs", "criticisms" }, allowSetters = true)
    private PartyEntity party;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BranchEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public BranchEntity title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBranchCode() {
        return this.branchCode;
    }

    public BranchEntity branchCode(String branchCode) {
        this.branchCode = branchCode;
        return this;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getTradeTitle() {
        return this.tradeTitle;
    }

    public BranchEntity tradeTitle(String tradeTitle) {
        this.tradeTitle = tradeTitle;
        return this;
    }

    public void setTradeTitle(String tradeTitle) {
        this.tradeTitle = tradeTitle;
    }

    public LocalDate getActivationDate() {
        return this.activationDate;
    }

    public BranchEntity activationDate(LocalDate activationDate) {
        this.activationDate = activationDate;
        return this;
    }

    public void setActivationDate(LocalDate activationDate) {
        this.activationDate = activationDate;
    }

    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    public BranchEntity expirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getActivationStatus() {
        return this.activationStatus;
    }

    public BranchEntity activationStatus(Boolean activationStatus) {
        this.activationStatus = activationStatus;
        return this;
    }

    public void setActivationStatus(Boolean activationStatus) {
        this.activationStatus = activationStatus;
    }

    public Double getLat() {
        return this.lat;
    }

    public BranchEntity lat(Double lat) {
        this.lat = lat;
        return this;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return this.address;
    }

    public BranchEntity address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public BranchEntity postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getDescription() {
        return this.description;
    }

    public BranchEntity description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PartyEntity getParty() {
        return this.party;
    }

    public BranchEntity party(PartyEntity party) {
        this.setParty(party);
        return this;
    }

    public void setParty(PartyEntity party) {
        this.party = party;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BranchEntity)) {
            return false;
        }
        return id != null && id.equals(((BranchEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BranchEntity{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", branchCode='" + getBranchCode() + "'" +
            ", tradeTitle='" + getTradeTitle() + "'" +
            ", activationDate='" + getActivationDate() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", activationStatus='" + getActivationStatus() + "'" +
            ", lat=" + getLat() +
            ", address='" + getAddress() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
