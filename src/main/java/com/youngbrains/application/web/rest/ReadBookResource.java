package com.youngbrains.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.youngbrains.application.service.ReadBookService;
import com.youngbrains.application.web.rest.errors.BadRequestAlertException;
import com.youngbrains.application.web.rest.util.HeaderUtil;
import com.youngbrains.application.service.dto.ReadBookDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ReadBook.
 */
@RestController
@RequestMapping("/api")
public class ReadBookResource {

    private final Logger log = LoggerFactory.getLogger(ReadBookResource.class);

    private static final String ENTITY_NAME = "readBook";

    private final ReadBookService readBookService;

    public ReadBookResource(ReadBookService readBookService) {
        this.readBookService = readBookService;
    }

    /**
     * POST  /read-books : Create a new readBook.
     *
     * @param readBookDTO the readBookDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new readBookDTO, or with status 400 (Bad Request) if the readBook has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/read-books")
    @Timed
    public ResponseEntity<ReadBookDTO> createReadBook(@RequestBody ReadBookDTO readBookDTO) throws URISyntaxException {
        log.debug("REST request to save ReadBook : {}", readBookDTO);
        if (readBookDTO.getId() != null) {
            throw new BadRequestAlertException("A new readBook cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReadBookDTO result = readBookService.save(readBookDTO);
        return ResponseEntity.created(new URI("/api/read-books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /read-books : Updates an existing readBook.
     *
     * @param readBookDTO the readBookDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated readBookDTO,
     * or with status 400 (Bad Request) if the readBookDTO is not valid,
     * or with status 500 (Internal Server Error) if the readBookDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/read-books")
    @Timed
    public ResponseEntity<ReadBookDTO> updateReadBook(@RequestBody ReadBookDTO readBookDTO) throws URISyntaxException {
        log.debug("REST request to update ReadBook : {}", readBookDTO);
        if (readBookDTO.getId() == null) {
            return createReadBook(readBookDTO);
        }
        ReadBookDTO result = readBookService.save(readBookDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, readBookDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /read-books : get all the readBooks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of readBooks in body
     */
    @GetMapping("/read-books")
    @Timed
    public List<ReadBookDTO> getAllReadBooks() {
        log.debug("REST request to get all ReadBooks");
        return readBookService.findAll();
        }

    /**
     * GET  /read-books/:id : get the "id" readBook.
     *
     * @param id the id of the readBookDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the readBookDTO, or with status 404 (Not Found)
     */
    @GetMapping("/read-books/{id}")
    @Timed
    public ResponseEntity<ReadBookDTO> getReadBook(@PathVariable Long id) {
        log.debug("REST request to get ReadBook : {}", id);
        ReadBookDTO readBookDTO = readBookService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(readBookDTO));
    }

    /**
     * DELETE  /read-books/:id : delete the "id" readBook.
     *
     * @param id the id of the readBookDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/read-books/{id}")
    @Timed
    public ResponseEntity<Void> deleteReadBook(@PathVariable Long id) {
        log.debug("REST request to delete ReadBook : {}", id);
        readBookService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
