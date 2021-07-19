package com.barad.bomb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 4 field fixed
 */
@Entity
@Table(name = "file_document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FileDocumentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 250)
    @Column(name = "file_name", length = 250, nullable = false)
    private String fileName;

    @Lob
    @Column(name = "file_content")
    private byte[] fileContent;

    @Column(name = "file_content_content_type")
    private String fileContentContentType;

    @Size(max = 2000)
    @Column(name = "file_path", length = 2000)
    private String filePath;

    @NotNull
    @Size(max = 3000)
    @Column(name = "description", length = 3000, nullable = false)
    private String description;

    @ManyToOne(optional = false)
    @NotNull
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
    private PartyEntity party;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FileDocumentEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getFileName() {
        return this.fileName;
    }

    public FileDocumentEntity fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileContent() {
        return this.fileContent;
    }

    public FileDocumentEntity fileContent(byte[] fileContent) {
        this.fileContent = fileContent;
        return this;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileContentContentType() {
        return this.fileContentContentType;
    }

    public FileDocumentEntity fileContentContentType(String fileContentContentType) {
        this.fileContentContentType = fileContentContentType;
        return this;
    }

    public void setFileContentContentType(String fileContentContentType) {
        this.fileContentContentType = fileContentContentType;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public FileDocumentEntity filePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDescription() {
        return this.description;
    }

    public FileDocumentEntity description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PartyEntity getParty() {
        return this.party;
    }

    public FileDocumentEntity party(PartyEntity party) {
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
        if (!(o instanceof FileDocumentEntity)) {
            return false;
        }
        return id != null && id.equals(((FileDocumentEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileDocumentEntity{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", fileContent='" + getFileContent() + "'" +
            ", fileContentContentType='" + getFileContentContentType() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
