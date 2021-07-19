package com.barad.bomb.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.barad.bomb.domain.FileDocumentEntity} entity.
 */
@ApiModel(description = "4 field fixed")
public class FileDocumentDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 250)
    private String fileName;

    @Lob
    private byte[] fileContent;

    private String fileContentContentType;

    @Size(max = 2000)
    private String filePath;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileContentContentType() {
        return fileContentContentType;
    }

    public void setFileContentContentType(String fileContentContentType) {
        this.fileContentContentType = fileContentContentType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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
        if (!(o instanceof FileDocumentDTO)) {
            return false;
        }

        FileDocumentDTO fileDocumentDTO = (FileDocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fileDocumentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileDocumentDTO{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", fileContent='" + getFileContent() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", description='" + getDescription() + "'" +
            ", party=" + getParty() +
            "}";
    }
}
