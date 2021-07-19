package com.barad.bomb.service.impl;

import com.barad.bomb.domain.FileDocumentEntity;
import com.barad.bomb.repository.FileDocumentRepository;
import com.barad.bomb.service.FileDocumentService;
import com.barad.bomb.service.dto.FileDocumentDTO;
import com.barad.bomb.service.mapper.FileDocumentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FileDocumentEntity}.
 */
@Service
@Transactional
public class FileDocumentServiceImpl implements FileDocumentService {

    private final Logger log = LoggerFactory.getLogger(FileDocumentServiceImpl.class);

    private final FileDocumentRepository fileDocumentRepository;

    private final FileDocumentMapper fileDocumentMapper;

    public FileDocumentServiceImpl(FileDocumentRepository fileDocumentRepository, FileDocumentMapper fileDocumentMapper) {
        this.fileDocumentRepository = fileDocumentRepository;
        this.fileDocumentMapper = fileDocumentMapper;
    }

    @Override
    public FileDocumentDTO save(FileDocumentDTO fileDocumentDTO) {
        log.debug("Request to save FileDocument : {}", fileDocumentDTO);
        FileDocumentEntity fileDocumentEntity = fileDocumentMapper.toEntity(fileDocumentDTO);
        fileDocumentEntity = fileDocumentRepository.save(fileDocumentEntity);
        return fileDocumentMapper.toDto(fileDocumentEntity);
    }

    @Override
    public Optional<FileDocumentDTO> partialUpdate(FileDocumentDTO fileDocumentDTO) {
        log.debug("Request to partially update FileDocument : {}", fileDocumentDTO);

        return fileDocumentRepository
            .findById(fileDocumentDTO.getId())
            .map(
                existingFileDocument -> {
                    fileDocumentMapper.partialUpdate(existingFileDocument, fileDocumentDTO);
                    return existingFileDocument;
                }
            )
            .map(fileDocumentRepository::save)
            .map(fileDocumentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FileDocumentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FileDocuments");
        return fileDocumentRepository.findAll(pageable).map(fileDocumentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FileDocumentDTO> findOne(Long id) {
        log.debug("Request to get FileDocument : {}", id);
        return fileDocumentRepository.findById(id).map(fileDocumentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FileDocument : {}", id);
        fileDocumentRepository.deleteById(id);
    }
}
