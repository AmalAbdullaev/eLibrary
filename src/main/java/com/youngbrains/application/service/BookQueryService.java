package com.youngbrains.application.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.youngbrains.application.domain.Book;
import com.youngbrains.application.domain.*; // for static metamodels
import com.youngbrains.application.repository.BookRepository;
import com.youngbrains.application.service.dto.BookCriteria;

import com.youngbrains.application.service.dto.BookDTO;
import com.youngbrains.application.service.mapper.BookMapper;

/**
 * Service for executing complex queries for Book entities in the database.
 * The main input is a {@link BookCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BookDTO} or a {@link Page} of {@link BookDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BookQueryService extends QueryService<Book> {

    private final Logger log = LoggerFactory.getLogger(BookQueryService.class);


    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    public BookQueryService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    /**
     * Return a {@link List} of {@link BookDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BookDTO> findByCriteria(BookCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Book> specification = createSpecification(criteria);
        return bookMapper.toDto(bookRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BookDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BookDTO> findByCriteria(BookCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Book> specification = createSpecification(criteria);
        final Page<Book> result = bookRepository.findAll(specification, page);
        return result.map(bookMapper::toDto);
    }

    /**
     * Function to convert BookCriteria to a {@link Specifications}
     */
    private Specifications<Book> createSpecification(BookCriteria criteria) {
        Specifications<Book> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Book_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Book_.title));
            }
            if (criteria.getPages() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPages(), Book_.pages));
            }
            if (criteria.getApproved() != null) {
                specification = specification.and(buildSpecification(criteria.getApproved(), Book_.approved));
            }
            if (criteria.getPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPath(), Book_.path));
            }
            if (criteria.getCoverPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCoverPath(), Book_.coverPath));
            }
            if (criteria.getYearOfPublishing() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getYearOfPublishing(), Book_.yearOfPublishing));
            }
            if (criteria.getAuthorFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAuthorFirstName(), Book_.authorFirstName));
            }
            if (criteria.getAuthorLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAuthorLastName(), Book_.authorLastName));
            }
            if (criteria.getProfileId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProfileId(), Book_.profile, Profile_.id));
            }
            if (criteria.getGenreId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getGenreId(), Book_.genre, Genre_.id));
            }
        }
        return specification;
    }

}
