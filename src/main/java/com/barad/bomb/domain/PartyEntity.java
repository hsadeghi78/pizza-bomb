package com.barad.bomb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 4 field fixed
 */
@Entity
@Table(name = "party")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PartyEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @NotNull
    @Size(max = 100)
    @Column(name = "party_code", length = 100, nullable = false, unique = true)
    private String partyCode;

    @NotNull
    @Size(max = 200)
    @Column(name = "trade_title", length = 200, nullable = false)
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
    @Column(name = "lon", nullable = false)
    private Double lon;

    @NotNull
    @Size(max = 3000)
    @Column(name = "address", length = 3000, nullable = false)
    private String address;

    @NotNull
    @Size(max = 12)
    @Column(name = "postal_code", length = 12, nullable = false)
    private String postalCode;

    @NotNull
    @Size(max = 15)
    @Column(name = "mobile", length = 15, nullable = false)
    private String mobile;

    /**
     * for flatOgranization, Horizontal Organization, Legal person, Individual Person
     */
    @NotNull
    @Column(name = "party_type_class_id", nullable = false)
    private Long partyTypeClassId;

    @Size(max = 3000)
    @Column(name = "description", length = 3000)
    private String description;

    @OneToMany(mappedBy = "party")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "party" }, allowSetters = true)
    private Set<CriticismEntity> criticisms = new HashSet<>();

    @OneToMany(mappedBy = "party")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "party" }, allowSetters = true)
    private Set<FileDocumentEntity> files = new HashSet<>();

    @OneToMany(mappedBy = "party")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "party" }, allowSetters = true)
    private Set<PartyInformationEntity> moreInfos = new HashSet<>();

    @OneToMany(mappedBy = "writerParty")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "children", "writerParty", "audienceParty", "parent" }, allowSetters = true)
    private Set<CommentEntity> writedComments = new HashSet<>();

    @OneToMany(mappedBy = "audienceParty")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "children", "writerParty", "audienceParty", "parent" }, allowSetters = true)
    private Set<CommentEntity> audienceComments = new HashSet<>();

    @OneToMany(mappedBy = "party")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "foods", "party" }, allowSetters = true)
    private Set<FoodTypeEntity> foodTypes = new HashSet<>();

    @OneToMany(mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "criticisms",
            "files",
            "moreInfos",
            "writedComments",
            "audienceComments",
            "foodTypes",
            "children",
            "contacts",
            "addresses",
            "menuItems",
            "produceFoods",
            "designedFoods",
            "buyerFactors",
            "sellerFactors",
            "parent",
            "partner",
            "person",
        },
        allowSetters = true
    )
    private Set<PartyEntity> children = new HashSet<>();

    @OneToMany(mappedBy = "party")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "party" }, allowSetters = true)
    private Set<ContactEntity> contacts = new HashSet<>();

    @OneToMany(mappedBy = "party")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "factors", "party" }, allowSetters = true)
    private Set<AddressEntity> addresses = new HashSet<>();

    @OneToMany(mappedBy = "party")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "party", "food" }, allowSetters = true)
    private Set<MenuItemEntity> menuItems = new HashSet<>();

    @OneToMany(mappedBy = "producerParty")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "menuItems", "factorItems", "materials", "producerParty", "designerParty", "foodType" },
        allowSetters = true
    )
    private Set<FoodEntity> produceFoods = new HashSet<>();

    @OneToMany(mappedBy = "designerParty")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "menuItems", "factorItems", "materials", "producerParty", "designerParty", "foodType" },
        allowSetters = true
    )
    private Set<FoodEntity> designedFoods = new HashSet<>();

    @OneToMany(mappedBy = "buyerParty")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "factorItems", "buyerParty", "sellerParty", "deliveryAddress" }, allowSetters = true)
    private Set<FactorEntity> buyerFactors = new HashSet<>();

    @OneToMany(mappedBy = "sellerParty")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "factorItems", "buyerParty", "sellerParty", "deliveryAddress" }, allowSetters = true)
    private Set<FactorEntity> sellerFactors = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "criticisms",
            "files",
            "moreInfos",
            "writedComments",
            "audienceComments",
            "foodTypes",
            "children",
            "contacts",
            "addresses",
            "menuItems",
            "produceFoods",
            "designedFoods",
            "buyerFactors",
            "sellerFactors",
            "parent",
            "partner",
            "person",
        },
        allowSetters = true
    )
    private PartyEntity parent;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parties" }, allowSetters = true)
    private PartnerEntity partner;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parties" }, allowSetters = true)
    private PersonEntity person;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PartyEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public PartyEntity title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public PartyEntity photo(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public PartyEntity photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getPartyCode() {
        return this.partyCode;
    }

    public PartyEntity partyCode(String partyCode) {
        this.partyCode = partyCode;
        return this;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }

    public String getTradeTitle() {
        return this.tradeTitle;
    }

    public PartyEntity tradeTitle(String tradeTitle) {
        this.tradeTitle = tradeTitle;
        return this;
    }

    public void setTradeTitle(String tradeTitle) {
        this.tradeTitle = tradeTitle;
    }

    public LocalDate getActivationDate() {
        return this.activationDate;
    }

    public PartyEntity activationDate(LocalDate activationDate) {
        this.activationDate = activationDate;
        return this;
    }

    public void setActivationDate(LocalDate activationDate) {
        this.activationDate = activationDate;
    }

    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    public PartyEntity expirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getActivationStatus() {
        return this.activationStatus;
    }

    public PartyEntity activationStatus(Boolean activationStatus) {
        this.activationStatus = activationStatus;
        return this;
    }

    public void setActivationStatus(Boolean activationStatus) {
        this.activationStatus = activationStatus;
    }

    public Double getLat() {
        return this.lat;
    }

    public PartyEntity lat(Double lat) {
        this.lat = lat;
        return this;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return this.lon;
    }

    public PartyEntity lon(Double lon) {
        this.lon = lon;
        return this;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getAddress() {
        return this.address;
    }

    public PartyEntity address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public PartyEntity postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getMobile() {
        return this.mobile;
    }

    public PartyEntity mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getPartyTypeClassId() {
        return this.partyTypeClassId;
    }

    public PartyEntity partyTypeClassId(Long partyTypeClassId) {
        this.partyTypeClassId = partyTypeClassId;
        return this;
    }

    public void setPartyTypeClassId(Long partyTypeClassId) {
        this.partyTypeClassId = partyTypeClassId;
    }

    public String getDescription() {
        return this.description;
    }

    public PartyEntity description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<CriticismEntity> getCriticisms() {
        return this.criticisms;
    }

    public PartyEntity criticisms(Set<CriticismEntity> criticisms) {
        this.setCriticisms(criticisms);
        return this;
    }

    public PartyEntity addCriticisms(CriticismEntity criticism) {
        this.criticisms.add(criticism);
        criticism.setParty(this);
        return this;
    }

    public PartyEntity removeCriticisms(CriticismEntity criticism) {
        this.criticisms.remove(criticism);
        criticism.setParty(null);
        return this;
    }

    public void setCriticisms(Set<CriticismEntity> criticisms) {
        if (this.criticisms != null) {
            this.criticisms.forEach(i -> i.setParty(null));
        }
        if (criticisms != null) {
            criticisms.forEach(i -> i.setParty(this));
        }
        this.criticisms = criticisms;
    }

    public Set<FileDocumentEntity> getFiles() {
        return this.files;
    }

    public PartyEntity files(Set<FileDocumentEntity> fileDocuments) {
        this.setFiles(fileDocuments);
        return this;
    }

    public PartyEntity addFiles(FileDocumentEntity fileDocument) {
        this.files.add(fileDocument);
        fileDocument.setParty(this);
        return this;
    }

    public PartyEntity removeFiles(FileDocumentEntity fileDocument) {
        this.files.remove(fileDocument);
        fileDocument.setParty(null);
        return this;
    }

    public void setFiles(Set<FileDocumentEntity> fileDocuments) {
        if (this.files != null) {
            this.files.forEach(i -> i.setParty(null));
        }
        if (fileDocuments != null) {
            fileDocuments.forEach(i -> i.setParty(this));
        }
        this.files = fileDocuments;
    }

    public Set<PartyInformationEntity> getMoreInfos() {
        return this.moreInfos;
    }

    public PartyEntity moreInfos(Set<PartyInformationEntity> partyInformations) {
        this.setMoreInfos(partyInformations);
        return this;
    }

    public PartyEntity addMoreInfo(PartyInformationEntity partyInformation) {
        this.moreInfos.add(partyInformation);
        partyInformation.setParty(this);
        return this;
    }

    public PartyEntity removeMoreInfo(PartyInformationEntity partyInformation) {
        this.moreInfos.remove(partyInformation);
        partyInformation.setParty(null);
        return this;
    }

    public void setMoreInfos(Set<PartyInformationEntity> partyInformations) {
        if (this.moreInfos != null) {
            this.moreInfos.forEach(i -> i.setParty(null));
        }
        if (partyInformations != null) {
            partyInformations.forEach(i -> i.setParty(this));
        }
        this.moreInfos = partyInformations;
    }

    public Set<CommentEntity> getWritedComments() {
        return this.writedComments;
    }

    public PartyEntity writedComments(Set<CommentEntity> comments) {
        this.setWritedComments(comments);
        return this;
    }

    public PartyEntity addWritedComments(CommentEntity comment) {
        this.writedComments.add(comment);
        comment.setWriterParty(this);
        return this;
    }

    public PartyEntity removeWritedComments(CommentEntity comment) {
        this.writedComments.remove(comment);
        comment.setWriterParty(null);
        return this;
    }

    public void setWritedComments(Set<CommentEntity> comments) {
        if (this.writedComments != null) {
            this.writedComments.forEach(i -> i.setWriterParty(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setWriterParty(this));
        }
        this.writedComments = comments;
    }

    public Set<CommentEntity> getAudienceComments() {
        return this.audienceComments;
    }

    public PartyEntity audienceComments(Set<CommentEntity> comments) {
        this.setAudienceComments(comments);
        return this;
    }

    public PartyEntity addAudienceComments(CommentEntity comment) {
        this.audienceComments.add(comment);
        comment.setAudienceParty(this);
        return this;
    }

    public PartyEntity removeAudienceComments(CommentEntity comment) {
        this.audienceComments.remove(comment);
        comment.setAudienceParty(null);
        return this;
    }

    public void setAudienceComments(Set<CommentEntity> comments) {
        if (this.audienceComments != null) {
            this.audienceComments.forEach(i -> i.setAudienceParty(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setAudienceParty(this));
        }
        this.audienceComments = comments;
    }

    public Set<FoodTypeEntity> getFoodTypes() {
        return this.foodTypes;
    }

    public PartyEntity foodTypes(Set<FoodTypeEntity> foodTypes) {
        this.setFoodTypes(foodTypes);
        return this;
    }

    public PartyEntity addFoodTypes(FoodTypeEntity foodType) {
        this.foodTypes.add(foodType);
        foodType.setParty(this);
        return this;
    }

    public PartyEntity removeFoodTypes(FoodTypeEntity foodType) {
        this.foodTypes.remove(foodType);
        foodType.setParty(null);
        return this;
    }

    public void setFoodTypes(Set<FoodTypeEntity> foodTypes) {
        if (this.foodTypes != null) {
            this.foodTypes.forEach(i -> i.setParty(null));
        }
        if (foodTypes != null) {
            foodTypes.forEach(i -> i.setParty(this));
        }
        this.foodTypes = foodTypes;
    }

    public Set<PartyEntity> getChildren() {
        return this.children;
    }

    public PartyEntity children(Set<PartyEntity> parties) {
        this.setChildren(parties);
        return this;
    }

    public PartyEntity addChildren(PartyEntity party) {
        this.children.add(party);
        party.setParent(this);
        return this;
    }

    public PartyEntity removeChildren(PartyEntity party) {
        this.children.remove(party);
        party.setParent(null);
        return this;
    }

    public void setChildren(Set<PartyEntity> parties) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (parties != null) {
            parties.forEach(i -> i.setParent(this));
        }
        this.children = parties;
    }

    public Set<ContactEntity> getContacts() {
        return this.contacts;
    }

    public PartyEntity contacts(Set<ContactEntity> contacts) {
        this.setContacts(contacts);
        return this;
    }

    public PartyEntity addContacts(ContactEntity contact) {
        this.contacts.add(contact);
        contact.setParty(this);
        return this;
    }

    public PartyEntity removeContacts(ContactEntity contact) {
        this.contacts.remove(contact);
        contact.setParty(null);
        return this;
    }

    public void setContacts(Set<ContactEntity> contacts) {
        if (this.contacts != null) {
            this.contacts.forEach(i -> i.setParty(null));
        }
        if (contacts != null) {
            contacts.forEach(i -> i.setParty(this));
        }
        this.contacts = contacts;
    }

    public Set<AddressEntity> getAddresses() {
        return this.addresses;
    }

    public PartyEntity addresses(Set<AddressEntity> addresses) {
        this.setAddresses(addresses);
        return this;
    }

    public PartyEntity addAddresses(AddressEntity address) {
        this.addresses.add(address);
        address.setParty(this);
        return this;
    }

    public PartyEntity removeAddresses(AddressEntity address) {
        this.addresses.remove(address);
        address.setParty(null);
        return this;
    }

    public void setAddresses(Set<AddressEntity> addresses) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setParty(null));
        }
        if (addresses != null) {
            addresses.forEach(i -> i.setParty(this));
        }
        this.addresses = addresses;
    }

    public Set<MenuItemEntity> getMenuItems() {
        return this.menuItems;
    }

    public PartyEntity menuItems(Set<MenuItemEntity> menuItems) {
        this.setMenuItems(menuItems);
        return this;
    }

    public PartyEntity addMenuItems(MenuItemEntity menuItem) {
        this.menuItems.add(menuItem);
        menuItem.setParty(this);
        return this;
    }

    public PartyEntity removeMenuItems(MenuItemEntity menuItem) {
        this.menuItems.remove(menuItem);
        menuItem.setParty(null);
        return this;
    }

    public void setMenuItems(Set<MenuItemEntity> menuItems) {
        if (this.menuItems != null) {
            this.menuItems.forEach(i -> i.setParty(null));
        }
        if (menuItems != null) {
            menuItems.forEach(i -> i.setParty(this));
        }
        this.menuItems = menuItems;
    }

    public Set<FoodEntity> getProduceFoods() {
        return this.produceFoods;
    }

    public PartyEntity produceFoods(Set<FoodEntity> foods) {
        this.setProduceFoods(foods);
        return this;
    }

    public PartyEntity addProduceFoods(FoodEntity food) {
        this.produceFoods.add(food);
        food.setProducerParty(this);
        return this;
    }

    public PartyEntity removeProduceFoods(FoodEntity food) {
        this.produceFoods.remove(food);
        food.setProducerParty(null);
        return this;
    }

    public void setProduceFoods(Set<FoodEntity> foods) {
        if (this.produceFoods != null) {
            this.produceFoods.forEach(i -> i.setProducerParty(null));
        }
        if (foods != null) {
            foods.forEach(i -> i.setProducerParty(this));
        }
        this.produceFoods = foods;
    }

    public Set<FoodEntity> getDesignedFoods() {
        return this.designedFoods;
    }

    public PartyEntity designedFoods(Set<FoodEntity> foods) {
        this.setDesignedFoods(foods);
        return this;
    }

    public PartyEntity addDesignedFoods(FoodEntity food) {
        this.designedFoods.add(food);
        food.setDesignerParty(this);
        return this;
    }

    public PartyEntity removeDesignedFoods(FoodEntity food) {
        this.designedFoods.remove(food);
        food.setDesignerParty(null);
        return this;
    }

    public void setDesignedFoods(Set<FoodEntity> foods) {
        if (this.designedFoods != null) {
            this.designedFoods.forEach(i -> i.setDesignerParty(null));
        }
        if (foods != null) {
            foods.forEach(i -> i.setDesignerParty(this));
        }
        this.designedFoods = foods;
    }

    public Set<FactorEntity> getBuyerFactors() {
        return this.buyerFactors;
    }

    public PartyEntity buyerFactors(Set<FactorEntity> factors) {
        this.setBuyerFactors(factors);
        return this;
    }

    public PartyEntity addBuyerFactors(FactorEntity factor) {
        this.buyerFactors.add(factor);
        factor.setBuyerParty(this);
        return this;
    }

    public PartyEntity removeBuyerFactors(FactorEntity factor) {
        this.buyerFactors.remove(factor);
        factor.setBuyerParty(null);
        return this;
    }

    public void setBuyerFactors(Set<FactorEntity> factors) {
        if (this.buyerFactors != null) {
            this.buyerFactors.forEach(i -> i.setBuyerParty(null));
        }
        if (factors != null) {
            factors.forEach(i -> i.setBuyerParty(this));
        }
        this.buyerFactors = factors;
    }

    public Set<FactorEntity> getSellerFactors() {
        return this.sellerFactors;
    }

    public PartyEntity sellerFactors(Set<FactorEntity> factors) {
        this.setSellerFactors(factors);
        return this;
    }

    public PartyEntity addSellerFactors(FactorEntity factor) {
        this.sellerFactors.add(factor);
        factor.setSellerParty(this);
        return this;
    }

    public PartyEntity removeSellerFactors(FactorEntity factor) {
        this.sellerFactors.remove(factor);
        factor.setSellerParty(null);
        return this;
    }

    public void setSellerFactors(Set<FactorEntity> factors) {
        if (this.sellerFactors != null) {
            this.sellerFactors.forEach(i -> i.setSellerParty(null));
        }
        if (factors != null) {
            factors.forEach(i -> i.setSellerParty(this));
        }
        this.sellerFactors = factors;
    }

    public PartyEntity getParent() {
        return this.parent;
    }

    public PartyEntity parent(PartyEntity party) {
        this.setParent(party);
        return this;
    }

    public void setParent(PartyEntity party) {
        this.parent = party;
    }

    public PartnerEntity getPartner() {
        return this.partner;
    }

    public PartyEntity partner(PartnerEntity partner) {
        this.setPartner(partner);
        return this;
    }

    public void setPartner(PartnerEntity partner) {
        this.partner = partner;
    }

    public PersonEntity getPerson() {
        return this.person;
    }

    public PartyEntity person(PersonEntity person) {
        this.setPerson(person);
        return this;
    }

    public void setPerson(PersonEntity person) {
        this.person = person;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PartyEntity)) {
            return false;
        }
        return id != null && id.equals(((PartyEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartyEntity{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
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
            "}";
    }
}
