package com.youngbrains.application.service;

import com.youngbrains.application.domain.FavoriteBook;
import com.youngbrains.application.repository.FavoriteBookRepository;
import com.youngbrains.application.service.dto.FavoriteBookDTO;
import com.youngbrains.application.service.mapper.FavoriteBookMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing FavoriteBook.
 */
@Service
@Transactional
public class FavoriteBookService {

    private final Logger log = LoggerFactory.getLogger(FavoriteBookService.class);

    private final FavoriteBookRepository favoriteBookRepository;

    private final FavoriteBookMapper favoriteBookMapper;

    public FavoriteBookService(FavoriteBookRepository favoriteBookRepository, FavoriteBookMapper favoriteBookMapper) {
        this.favoriteBookRepository = favoriteBookRepository;
        this.favoriteBookMapper = favoriteBookMapper;
    }

    /**
     * Save a favoriteBook.
     *
     * @param favoriteBookDTO the entity to save
     * @return the persisted entity
     */
    public FavoriteBookDTO save(FavoriteBookDTO favoriteBookDTO) {
        log.debug("Request to save FavoriteBook : {}", favoriteBookDTO);
        FavoriteBook favoriteBook = favoriteBookMapper.toEntity(favoriteBookDTO);
        favoriteBook = favoriteBookRepository.save(favoriteBook);
        return favoriteBookMapper.toDto(favoriteBook);
    }

    /**
     * Get all the favoriteBooks.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<FavoriteBookDTO> findAll() {
        log.debug("Request to get all FavoriteBooks");
        return favoriteBookRepository.findAll().stream()
            .map(favoriteBookMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one favoriteBook by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public FavoriteBookDTO findOne(Long id) {
        log.debug("Request to get FavoriteBook : {}", id);
        FavoriteBook favoriteBook = favoriteBookRepository.findOne(id);
        return favoriteBookMapper.toDto(favoriteBook);
    }

    /**
     * Delete the favoriteBook by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete FavoriteBook : {}", id);
        favoriteBookRepository.delete(id);
    }
}
