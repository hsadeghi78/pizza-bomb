package com.barad.bomb.repository;

import com.barad.bomb.domain.FileDocumentEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FileDocumentEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileDocumentRepository extends JpaRepository<FileDocumentEntity, Long>, JpaSpecificationExecutor<FileDocumentEntity> {}
