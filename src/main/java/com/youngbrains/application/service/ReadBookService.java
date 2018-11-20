package com.youngbrains.application.service;

import com.youngbrains.application.domain.ReadBook;
import com.youngbrains.application.repository.ReadBookRepository;
import com.youngbrains.application.service.dto.ReadBookDTO;
import com.youngbrains.application.service.mapper.ReadBookMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ReadBook.
 */
@Service
@Transactional
public class ReadBookService {

    private final Logger log = LoggerFactory.getLogger(ReadBookService.class);

    private final ReadBookRepository readBookRepository;

    private final ReadBookMapper readBookMapper;

    public ReadBookService(ReadBookRepository readBookRepository, ReadBookMapper readBookMapper) {
        this.readBookRepository = readBookRepository;
        this.readBookMapper = readBookMapper;
    }

    /**
     * Save a readBook.
     *
     * @param readBookDTO the entity to save
     * @return the persisted entity
     */
    public ReadBookDTO save(ReadBookDTO readBookDTO) {
        log.debug("Request to save ReadBook : {}", readBookDTO);
        ReadBook readBook = readBookMapper.toEntity(readBookDTO);
        readBook = readBookRepository.save(readBook);
        return readBookMapper.toDto(readBook);
    }

    /**
     * Get all the readBooks.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ReadBookDTO> findAll() {
        log.debug("Request to get all ReadBooks");
        return readBookRepository.findAll().stream()
            .map(readBookMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one readBook by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ReadBookDTO findOne(Long id) {
        log.debug("Request to get ReadBook : {}", id);
        ReadBook readBook = readBookRepository.findOne(id);
        return readBookMapper.toDto(readBook);
    }

    /**
     * Delete the readBook by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ReadBook : {}", id);
        readBookRepository.delete(id);
    }

    public void deleteAllByBookId(Long bookId) {
        readBookRepository.deleteAllByBookId(bookId);
    }
}
