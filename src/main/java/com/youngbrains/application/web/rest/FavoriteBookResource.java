package com.youngbrains.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.youngbrains.application.service.FavoriteBookQueryService;
import com.youngbrains.application.service.FavoriteBookService;
import com.youngbrains.application.service.dto.BookDTO;
import com.youngbrains.application.service.dto.FavoriteBookCriteria;
import com.youngbrains.application.web.rest.errors.BadRequestAlertException;
import com.youngbrains.application.web.rest.util.HeaderUtil;
import com.youngbrains.application.service.dto.FavoriteBookDTO;
import com.youngbrains.application.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FavoriteBook.
 */
@RestController
@RequestMapping("/api")
public class FavoriteBookResource {

    private final Logger log = LoggerFactory.getLogger(FavoriteBookResource.class);

    private static final String ENTITY_NAME = "favoriteBook";

    private final FavoriteBookService favoriteBookService;

    private final FavoriteBookQueryService favoriteBookQueryService;

    public FavoriteBookResource(FavoriteBookService favoriteBookService, FavoriteBookQueryService favoriteBookQueryService) {
        this.favoriteBookService = favoriteBookService;
        this.favoriteBookQueryService = favoriteBookQueryService;
    }

    /**
     * POST  /favorite-books : Create a new favoriteBook.
     *
     * @param favoriteBookDTO the favoriteBookDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new favoriteBookDTO, or with status 400 (Bad Request) if the favoriteBook has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/favorite-books")
    @Timed
    public ResponseEntity<FavoriteBookDTO> createFavoriteBook(@RequestBody FavoriteBookDTO favoriteBookDTO) throws URISyntaxException {
        log.debug("REST request to save FavoriteBook : {}", favoriteBookDTO);
        if (favoriteBookDTO.getId() != null) {
            throw new BadRequestAlertException("A new favoriteBook cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FavoriteBookDTO result = favoriteBookService.save(favoriteBookDTO);
        return ResponseEntity.created(new URI("/api/favorite-books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /favorite-books : Updates an existing favoriteBook.
     *
     * @param favoriteBookDTO the favoriteBookDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated favoriteBookDTO,
     * or with status 400 (Bad Request) if the favoriteBookDTO is not valid,
     * or with status 500 (Internal Server Error) if the favoriteBookDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/favorite-books")
    @Timed
    public ResponseEntity<FavoriteBookDTO> updateFavoriteBook(@RequestBody FavoriteBookDTO favoriteBookDTO) throws URISyntaxException {
        log.debug("REST request to update FavoriteBook : {}", favoriteBookDTO);
        if (favoriteBookDTO.getId() == null) {
            return createFavoriteBook(favoriteBookDTO);
        }
        FavoriteBookDTO result = favoriteBookService.save(favoriteBookDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, favoriteBookDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /favorite-books : get all the favoriteBooks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of favoriteBooks in body
     */
    @GetMapping("/favorite-books")
    @Timed
    public ResponseEntity<List<FavoriteBookDTO>> getAllFavoriteBooks(FavoriteBookCriteria criteria, Pageable pageable) {
        log.debug("REST request to get all FavoriteBooks");
        Page<FavoriteBookDTO> page = favoriteBookQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/favorite-books");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/favorite-books/top")
    public List<BookDTO> getTop10Books() {
        return favoriteBookService.findTop10Books();
    }

    /**
     * GET  /favorite-books/:id : get the "id" favoriteBook.
     *
     * @param id the id of the favoriteBookDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the favoriteBookDTO, or with status 404 (Not Found)
     */
    @GetMapping("/favorite-books/{id}")
    @Timed
    public ResponseEntity<FavoriteBookDTO> getFavoriteBook(@PathVariable Long id) {
        log.debug("REST request to get FavoriteBook : {}", id);
        FavoriteBookDTO favoriteBookDTO = favoriteBookService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(favoriteBookDTO));
    }

    /**
     * DELETE  /favorite-books/:id : delete the "id" favoriteBook.
     *
     * @param id the id of the favoriteBookDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/favorite-books/{id}")
    @Timed
    public ResponseEntity<Void> deleteFavoriteBook(@PathVariable Long id) {
        log.debug("REST request to delete FavoriteBook : {}", id);
        favoriteBookService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
