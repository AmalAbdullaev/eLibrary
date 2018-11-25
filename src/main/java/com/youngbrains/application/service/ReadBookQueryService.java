package com.youngbrains.application.service;

import com.youngbrains.application.domain.Book_;
import com.youngbrains.application.domain.Profile_;
import com.youngbrains.application.domain.ReadBook;
import com.youngbrains.application.domain.ReadBook_;
import com.youngbrains.application.repository.ReadBookRepository;
import com.youngbrains.application.service.dto.ReadBookDTO;
import com.youngbrains.application.service.mapper.ReadBookMapper;
import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReadBookQueryService extends QueryService<ReadBook> {

    private final Logger log = LoggerFactory.getLogger(BookQueryService.class);

    private ReadBookRepository readBookRepository;

    private ReadBookMapper readBookMapper;

    public ReadBookQueryService(ReadBookRepository readBookRepository, ReadBookMapper readBookMapper) {
        this.readBookRepository = readBookRepository;
        this.readBookMapper = readBookMapper;
    }

    @Transactional(readOnly = true)
    public Page<ReadBookDTO> findByCriteria(ReadBookCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<ReadBook> specification = createSpecification(criteria);
        final Page<ReadBook> result = readBookRepository.findAll(specification, page);
        return result.map(readBookMapper::toDto);
    }

    private Specifications<ReadBook> createSpecification(ReadBookCriteria criteria) {
        Specifications<ReadBook> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ReadBook_.id));
            }
            if (criteria.getBookId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getBookId(), ReadBook_.book, Book_.id));
            }
            if (criteria.getProfileId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProfileId(), ReadBook_.profile, Profile_.id));
            }
        }
        return specification;
    }
}

