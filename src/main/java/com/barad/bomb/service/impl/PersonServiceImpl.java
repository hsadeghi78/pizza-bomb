package com.barad.bomb.service.impl;

import com.barad.bomb.domain.PersonEntity;
import com.barad.bomb.repository.PersonRepository;
import com.barad.bomb.service.PersonService;
import com.barad.bomb.service.dto.PersonDTO;
import com.barad.bomb.service.mapper.PersonMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PersonEntity}.
 */
@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    private final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);

    private final PersonRepository personRepository;

    private final PersonMapper personMapper;

    public PersonServiceImpl(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    @Override
    public PersonDTO save(PersonDTO personDTO) {
        log.debug("Request to save Person : {}", personDTO);
        PersonEntity personEntity = personMapper.toEntity(personDTO);
        personEntity = personRepository.save(personEntity);
        return personMapper.toDto(personEntity);
    }

    @Override
    public Optional<PersonDTO> partialUpdate(PersonDTO personDTO) {
        log.debug("Request to partially update Person : {}", personDTO);

        return personRepository
            .findById(personDTO.getId())
            .map(
                existingPerson -> {
                    personMapper.partialUpdate(existingPerson, personDTO);
                    return existingPerson;
                }
            )
            .map(personRepository::save)
            .map(personMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PersonDTO> findAll(Pageable pageable) {
        log.debug("Request to get all People");
        return personRepository.findAll(pageable).map(personMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PersonDTO> findOne(Long id) {
        log.debug("Request to get Person : {}", id);
        return personRepository.findById(id).map(personMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Person : {}", id);
        personRepository.deleteById(id);
    }
}
