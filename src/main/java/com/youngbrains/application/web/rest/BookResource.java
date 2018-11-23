package com.youngbrains.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.youngbrains.application.domain.Book;
import com.youngbrains.application.domain.User;
import com.youngbrains.application.service.*;
import com.youngbrains.application.service.dto.BookCriteria;
import com.youngbrains.application.service.dto.BookDTO;
import com.youngbrains.application.service.dto.FeedbackDTO;
import com.youngbrains.application.service.dto.ProfileDTO;
import com.youngbrains.application.service.mapper.BookMapper;
import com.youngbrains.application.web.rest.errors.BadRequestAlertException;
import com.youngbrains.application.web.rest.util.HeaderUtil;
import com.youngbrains.application.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.undertow.util.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystemException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Book.
 */
@RestController
@RequestMapping("/api")
public class BookResource {

    private final Logger log = LoggerFactory.getLogger(BookResource.class);

    private static final String ENTITY_NAME = "book";

    private final BookService bookService;

    private final BookMapper bookMapper;

    private final UserService userService;

    private final ProfileService profileService;

    private final MailService mailService;

    private final BookQueryService bookQueryService;

    private final ReadBookService readBookService;

    private final FavoriteBookService favoriteBookService;

    private final GenreService genreService;

    public BookResource(BookService bookService, BookMapper bookMapper, UserService userService, ProfileService profileService, MailService mailService, BookQueryService bookQueryService, ReadBookService readBookService, FavoriteBookService favoriteBookService, GenreService genreService) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
        this.userService = userService;
        this.profileService = profileService;
        this.mailService = mailService;
        this.bookQueryService = bookQueryService;
        this.readBookService = readBookService;
        this.favoriteBookService = favoriteBookService;
        this.genreService = genreService;
    }

    /**
     * POST  /books : Create a new book.
     *
     * @param bookDTO the bookDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bookDTO, or with status 400 (Bad Request) if the book has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/books")
    @Timed
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) throws URISyntaxException {
        log.debug("REST request to save Book : {}", bookDTO);
        if (bookDTO.getId() != null) {
            throw new BadRequestAlertException("A new book cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProfileDTO profile = profileService.findOne(bookDTO.getProfileId());
        if (profile.isTrusted())
            bookDTO.setApproved(true);
        BookDTO result = bookService.save(bookDTO);
        return ResponseEntity.created(new URI("/api/books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * POST  /books/upload : Upload a new book
     *
     * @param file file containing a book
     * @param id   the id of the book
     * @param type type of upload operation: "book" or "cover"
     * @return the ResponseEntity with status 200 (OK) and with body the uploaded bookDTO,
     * or with status 400 (Bad Request) if file could not be stored or type parameter is incorrect
     */
    @PostMapping("/books/upload")
    public ResponseEntity<BookDTO> uploadBook(@RequestParam MultipartFile file, @RequestParam Long id,
                                              @RequestParam String type) {
        BookDTO bookDTO;
        log.debug("REST request to upload Book : {}:", id);
        try {
            bookDTO = bookService.uploadBook(file, id, type);
            ProfileDTO profile = profileService.findOne(bookService.findOne(id).getProfileId());
            if (!profile.isTrusted()) {
                if (type != null && type.equals("book")) {
                    Book book = bookMapper.toEntity(bookDTO);
                    Long profileId = book.getProfile().getId();
                    Long userId = profileService.findOne(profileId).getUserId();
                    Optional<User> optionalUser = userService.getUserWithAuthorities(userId);
                    optionalUser.ifPresent(user -> mailService.sendNewBookEmail(user, book, "newBookEmail"));
                }
            }
        } catch (FileSystemException | BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(bookDTO);
    }

    @PostMapping("/feedback")
    public ResponseEntity<FeedbackDTO> sendFeedback(@RequestBody FeedbackDTO feedback) {
        log.debug("REST requst to send feedback email: ", feedback);
        mailService.sendFeedbackEmail(feedback, "feedbackEmail");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * PUT  /books : Updates an existing book.
     *
     * @param bookDTO the bookDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bookDTO,
     * or with status 400 (Bad Request) if the bookDTO is not valid,
     * or with status 500 (Internal Server Error) if the bookDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/books")
    @Timed
    public ResponseEntity<BookDTO> updateBook(@Valid @RequestBody BookDTO bookDTO) throws URISyntaxException {
        log.debug("REST request to update Book : {}", bookDTO);
        if (bookDTO.getId() == null) {
            return createBook(bookDTO);
        }
        BookDTO result = bookService.save(bookDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bookDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /books : get all the books.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of books in body
     */
    @GetMapping("/books")
    @Timed
    public ResponseEntity<List<BookDTO>> getAllBooks(BookCriteria criteria,
                                                     @RequestParam(value = "search", required = false) String searchText,
                                                     Pageable pageable) {
        log.debug("REST request to get Books by criteria: {}", criteria);
        Page<BookDTO> page;
        if (searchText != null)
            page = bookQueryService.findByTitleOrAuthor(searchText, pageable);
        else
            page = bookQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/books");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/profiles/top")
    public ResponseEntity<List<ProfileDTO>> findTop10Profiles() {
        List<ProfileDTO> profiles = bookService.findTop10Profiles();
        return new ResponseEntity<>(profiles, HttpStatus.OK);
    }

    /**
     * GET  /books/:id : get the "id" book.
     *
     * @param id the id of the bookDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bookDTO, or with status 404 (Not Found)
     */
    @GetMapping("/books/{id}")
    @Timed
    public ResponseEntity<BookDTO> getBook(@PathVariable Long id) {
        log.debug("REST request to get Book : {}", id);
        BookDTO bookDTO = bookService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bookDTO));
    }

    /**
     * GET /books/:id/download : download the "id" book
     *
     * @param id the id of the bookDTO to retrieve
     * @return resource corresponding to the requested book
     */
    @GetMapping("/books/{id}/download")
    public ResponseEntity<Resource> downloadBook(@PathVariable Long id, HttpServletRequest request) {
        log.debug("REST request to download Book : {}", id);
        BookDTO bookDTO = bookService.findOne(id);

        Resource bookResource;
        try {
            bookResource = bookService.loadBookAsResource(bookDTO.getPath());
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(bookResource.getFile().getAbsolutePath());
        } catch (IOException e) {
            log.info("Could not determine file type.");
        }
        if (contentType == null)
            contentType = "application/octet-stream";
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                + bookResource.getFilename() + "\"")
            .body(bookResource);
    }

    /**
     * DELETE  /books/:id : delete the "id" book.
     *
     * @param id the id of the bookDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/books/{id}")
    @Timed
    public ResponseEntity<Void> deleteBook(@PathVariable Long id,
                                           @RequestParam(value = "reason", required = false) String reason) {
        log.debug("REST request to delete Book : {}", id);
        BookDTO bookDTO = bookService.findOne(id);
        if (bookDTO != null) {
            if (bookDTO.getPath() != null) {
                try {
                    File bookFile = new File(bookDTO.getPath());
                    boolean deleted = bookFile.delete();
                    if (deleted)
                        log.debug("Book " + bookFile.getName() + " successfully deleted");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bookDTO.getCoverPath() != null) {
                try {
                    File coverFile = new File(bookDTO.getCoverPath());
                    boolean deleted = coverFile.delete();
                    if (deleted)
                        log.debug("Cover " + coverFile.getName() + " successfully deleted");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (reason != null) {
                Long userId = profileService.findOne(bookDTO.getProfileId()).getUserId();
                User user = userService.getUserWithAuthorities(userId).orElse(null);
                Book book = bookMapper.toEntity(bookDTO);
                mailService.sendDeletionEmail(user, book, reason, "deletionEmail");
            }
            favoriteBookService.deleteAllByBookId(bookDTO.getId());
            readBookService.deleteAllByBookId(bookDTO.getId());
            bookService.delete(id);
            long booksInGenre = bookService.countBookByGenreId(bookDTO.getGenreId());
            if (booksInGenre == 0)
                genreService.delete(bookDTO.getGenreId());
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
