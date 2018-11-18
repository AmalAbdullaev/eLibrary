package com.youngbrains.application.service;


import com.youngbrains.application.domain.Book_;
import com.youngbrains.application.domain.FavoriteBook;
import com.youngbrains.application.domain.FavoriteBook_;
import com.youngbrains.application.domain.Profile_;
import com.youngbrains.application.repository.FavoriteBookRepository;
import com.youngbrains.application.service.dto.FavoriteBookCriteria;
import com.youngbrains.application.service.dto.FavoriteBookDTO;
import com.youngbrains.application.service.mapper.FavoriteBookMapper;
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
public class FavoriteBookQueryService extends QueryService<FavoriteBook> {

    private final Logger log = LoggerFactory.getLogger(BookQueryService.class);


    private final FavoriteBookRepository favoriteBookRepository;

    private final FavoriteBookMapper favoriteBookMapper;

    public FavoriteBookQueryService(FavoriteBookRepository favoriteBookRepository, FavoriteBookMapper favoriteBookMapper) {
        this.favoriteBookRepository = favoriteBookRepository;
        this.favoriteBookMapper = favoriteBookMapper;
    }

    @Transactional(readOnly = true)
    public List<FavoriteBookDTO> findByCriteria(FavoriteBookCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<FavoriteBook> specification = createSpecification(criteria);
        return favoriteBookMapper.toDto(favoriteBookRepository.findAll(specification));
    }
    
    @Transactional(readOnly = true)
    public Page<FavoriteBookDTO> findByCriteria(FavoriteBookCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<FavoriteBook> specification = createSpecification(criteria);
        final Page<FavoriteBook> result = favoriteBookRepository.findAll(specification, page);
        return result.map(favoriteBookMapper::toDto);
    }
    
    private Specifications<FavoriteBook> createSpecification(FavoriteBookCriteria criteria) {
        Specifications<FavoriteBook> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FavoriteBook_.id));
            }
            if (criteria.getBookId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getBookId(), FavoriteBook_.book, Book_.id));
            }
            if (criteria.getProfileId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProfileId(), FavoriteBook_.profile, Profile_.id));
            }
        }
        return specification;
    }
}
