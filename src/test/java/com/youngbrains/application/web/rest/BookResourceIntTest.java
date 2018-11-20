package com.youngbrains.application.web.rest;

import com.youngbrains.application.ELibraryApp;

import com.youngbrains.application.domain.Book;
import com.youngbrains.application.domain.Profile;
import com.youngbrains.application.domain.Genre;
import com.youngbrains.application.repository.BookRepository;
import com.youngbrains.application.repository.FavoriteBookRepository;
import com.youngbrains.application.repository.ReadBookRepository;
import com.youngbrains.application.repository.UserRepository;
import com.youngbrains.application.service.*;
import com.youngbrains.application.service.dto.BookDTO;
import com.youngbrains.application.service.mapper.BookMapper;
import com.youngbrains.application.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.youngbrains.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BookResource REST controller.
 *
 * @see BookResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ELibraryApp.class)
public class BookResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_PAGES = 1;
    private static final Integer UPDATED_PAGES = 2;

    private static final Boolean DEFAULT_APPROVED = false;
    private static final Boolean UPDATED_APPROVED = true;

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_COVER_PATH = "AAAAAAAAAA";
    private static final String UPDATED_COVER_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_YEAR_OF_PUBLISHING = 1;
    private static final Integer UPDATED_YEAR_OF_PUBLISHING = 2;

    private static final String DEFAULT_AUTHOR_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AUTHOR_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR_LAST_NAME = "BBBBBBBBBB";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private MailService mailService;

    @Autowired
    private BookQueryService bookQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ReadBookService readBookService;

    @Autowired
    private FavoriteBookService favoriteBookService;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBookMockMvc;

    private Book book;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BookResource bookResource = new BookResource(bookService, bookMapper, userService, profileService, mailService, bookQueryService, readBookService, favoriteBookService);
        this.restBookMockMvc = MockMvcBuilders.standaloneSetup(bookResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Book createEntity(EntityManager em) {
        Book book = new Book()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .pages(DEFAULT_PAGES)
            .approved(DEFAULT_APPROVED)
            .path(DEFAULT_PATH)
            .coverPath(DEFAULT_COVER_PATH)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .yearOfPublishing(DEFAULT_YEAR_OF_PUBLISHING)
            .authorFirstName(DEFAULT_AUTHOR_FIRST_NAME)
            .authorLastName(DEFAULT_AUTHOR_LAST_NAME);
        return book;
    }

    @Before
    public void initTest() {
        book = createEntity(em);
    }

    @Test
    @Transactional
    public void createBook() throws Exception {
        int databaseSizeBeforeCreate = bookRepository.findAll().size();

        // Create the Book
        BookDTO bookDTO = bookMapper.toDto(book);
        restBookMockMvc.perform(post("/api/books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookDTO)))
            .andExpect(status().isCreated());

        // Validate the Book in the database
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeCreate + 1);
        Book testBook = bookList.get(bookList.size() - 1);
        assertThat(testBook.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBook.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBook.getPages()).isEqualTo(DEFAULT_PAGES);
        assertThat(testBook.isApproved()).isEqualTo(DEFAULT_APPROVED);
        assertThat(testBook.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testBook.getCoverPath()).isEqualTo(DEFAULT_COVER_PATH);
        assertThat(testBook.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testBook.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testBook.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testBook.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testBook.getYearOfPublishing()).isEqualTo(DEFAULT_YEAR_OF_PUBLISHING);
        assertThat(testBook.getAuthorFirstName()).isEqualTo(DEFAULT_AUTHOR_FIRST_NAME);
        assertThat(testBook.getAuthorLastName()).isEqualTo(DEFAULT_AUTHOR_LAST_NAME);
    }

    @Test
    @Transactional
    public void createBookWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bookRepository.findAll().size();

        // Create the Book with an existing ID
        book.setId(1L);
        BookDTO bookDTO = bookMapper.toDto(book);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookMockMvc.perform(post("/api/books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Book in the database
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRepository.findAll().size();
        // set the field null
        book.setTitle(null);

        // Create the Book, which fails.
        BookDTO bookDTO = bookMapper.toDto(book);

        restBookMockMvc.perform(post("/api/books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookDTO)))
            .andExpect(status().isBadRequest());

        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkApprovedIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRepository.findAll().size();
        // set the field null
        book.setApproved(null);

        // Create the Book, which fails.
        BookDTO bookDTO = bookMapper.toDto(book);

        restBookMockMvc.perform(post("/api/books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookDTO)))
            .andExpect(status().isBadRequest());

        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYearOfPublishingIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRepository.findAll().size();
        // set the field null
        book.setYearOfPublishing(null);

        // Create the Book, which fails.
        BookDTO bookDTO = bookMapper.toDto(book);

        restBookMockMvc.perform(post("/api/books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookDTO)))
            .andExpect(status().isBadRequest());

        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAuthorFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRepository.findAll().size();
        // set the field null
        book.setAuthorFirstName(null);

        // Create the Book, which fails.
        BookDTO bookDTO = bookMapper.toDto(book);

        restBookMockMvc.perform(post("/api/books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookDTO)))
            .andExpect(status().isBadRequest());

        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAuthorLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookRepository.findAll().size();
        // set the field null
        book.setAuthorLastName(null);

        // Create the Book, which fails.
        BookDTO bookDTO = bookMapper.toDto(book);

        restBookMockMvc.perform(post("/api/books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookDTO)))
            .andExpect(status().isBadRequest());

        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBooks() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList
        restBookMockMvc.perform(get("/api/books?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(book.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].pages").value(hasItem(DEFAULT_PAGES)))
            .andExpect(jsonPath("$.[*].approved").value(hasItem(DEFAULT_APPROVED.booleanValue())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())))
            .andExpect(jsonPath("$.[*].coverPath").value(hasItem(DEFAULT_COVER_PATH.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].yearOfPublishing").value(hasItem(DEFAULT_YEAR_OF_PUBLISHING)))
            .andExpect(jsonPath("$.[*].authorFirstName").value(hasItem(DEFAULT_AUTHOR_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].authorLastName").value(hasItem(DEFAULT_AUTHOR_LAST_NAME.toString())));
    }

    @Test
    @Transactional
    public void getBook() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get the book
        restBookMockMvc.perform(get("/api/books/{id}", book.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(book.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.pages").value(DEFAULT_PAGES))
            .andExpect(jsonPath("$.approved").value(DEFAULT_APPROVED.booleanValue()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH.toString()))
            .andExpect(jsonPath("$.coverPath").value(DEFAULT_COVER_PATH.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.yearOfPublishing").value(DEFAULT_YEAR_OF_PUBLISHING))
            .andExpect(jsonPath("$.authorFirstName").value(DEFAULT_AUTHOR_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.authorLastName").value(DEFAULT_AUTHOR_LAST_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllBooksByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where title equals to DEFAULT_TITLE
        defaultBookShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the bookList where title equals to UPDATED_TITLE
        defaultBookShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllBooksByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultBookShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the bookList where title equals to UPDATED_TITLE
        defaultBookShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllBooksByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where title is not null
        defaultBookShouldBeFound("title.specified=true");

        // Get all the bookList where title is null
        defaultBookShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllBooksByPagesIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where pages equals to DEFAULT_PAGES
        defaultBookShouldBeFound("pages.equals=" + DEFAULT_PAGES);

        // Get all the bookList where pages equals to UPDATED_PAGES
        defaultBookShouldNotBeFound("pages.equals=" + UPDATED_PAGES);
    }

    @Test
    @Transactional
    public void getAllBooksByPagesIsInShouldWork() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where pages in DEFAULT_PAGES or UPDATED_PAGES
        defaultBookShouldBeFound("pages.in=" + DEFAULT_PAGES + "," + UPDATED_PAGES);

        // Get all the bookList where pages equals to UPDATED_PAGES
        defaultBookShouldNotBeFound("pages.in=" + UPDATED_PAGES);
    }

    @Test
    @Transactional
    public void getAllBooksByPagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where pages is not null
        defaultBookShouldBeFound("pages.specified=true");

        // Get all the bookList where pages is null
        defaultBookShouldNotBeFound("pages.specified=false");
    }

    @Test
    @Transactional
    public void getAllBooksByPagesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where pages greater than or equals to DEFAULT_PAGES
        defaultBookShouldBeFound("pages.greaterOrEqualThan=" + DEFAULT_PAGES);

        // Get all the bookList where pages greater than or equals to UPDATED_PAGES
        defaultBookShouldNotBeFound("pages.greaterOrEqualThan=" + UPDATED_PAGES);
    }

    @Test
    @Transactional
    public void getAllBooksByPagesIsLessThanSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where pages less than or equals to DEFAULT_PAGES
        defaultBookShouldNotBeFound("pages.lessThan=" + DEFAULT_PAGES);

        // Get all the bookList where pages less than or equals to UPDATED_PAGES
        defaultBookShouldBeFound("pages.lessThan=" + UPDATED_PAGES);
    }


    @Test
    @Transactional
    public void getAllBooksByApprovedIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where approved equals to DEFAULT_APPROVED
        defaultBookShouldBeFound("approved.equals=" + DEFAULT_APPROVED);

        // Get all the bookList where approved equals to UPDATED_APPROVED
        defaultBookShouldNotBeFound("approved.equals=" + UPDATED_APPROVED);
    }

    @Test
    @Transactional
    public void getAllBooksByApprovedIsInShouldWork() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where approved in DEFAULT_APPROVED or UPDATED_APPROVED
        defaultBookShouldBeFound("approved.in=" + DEFAULT_APPROVED + "," + UPDATED_APPROVED);

        // Get all the bookList where approved equals to UPDATED_APPROVED
        defaultBookShouldNotBeFound("approved.in=" + UPDATED_APPROVED);
    }

    @Test
    @Transactional
    public void getAllBooksByApprovedIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where approved is not null
        defaultBookShouldBeFound("approved.specified=true");

        // Get all the bookList where approved is null
        defaultBookShouldNotBeFound("approved.specified=false");
    }

    @Test
    @Transactional
    public void getAllBooksByPathIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where path equals to DEFAULT_PATH
        defaultBookShouldBeFound("path.equals=" + DEFAULT_PATH);

        // Get all the bookList where path equals to UPDATED_PATH
        defaultBookShouldNotBeFound("path.equals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    public void getAllBooksByPathIsInShouldWork() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where path in DEFAULT_PATH or UPDATED_PATH
        defaultBookShouldBeFound("path.in=" + DEFAULT_PATH + "," + UPDATED_PATH);

        // Get all the bookList where path equals to UPDATED_PATH
        defaultBookShouldNotBeFound("path.in=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    public void getAllBooksByPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where path is not null
        defaultBookShouldBeFound("path.specified=true");

        // Get all the bookList where path is null
        defaultBookShouldNotBeFound("path.specified=false");
    }

    @Test
    @Transactional
    public void getAllBooksByCoverPathIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where coverPath equals to DEFAULT_COVER_PATH
        defaultBookShouldBeFound("coverPath.equals=" + DEFAULT_COVER_PATH);

        // Get all the bookList where coverPath equals to UPDATED_COVER_PATH
        defaultBookShouldNotBeFound("coverPath.equals=" + UPDATED_COVER_PATH);
    }

    @Test
    @Transactional
    public void getAllBooksByCoverPathIsInShouldWork() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where coverPath in DEFAULT_COVER_PATH or UPDATED_COVER_PATH
        defaultBookShouldBeFound("coverPath.in=" + DEFAULT_COVER_PATH + "," + UPDATED_COVER_PATH);

        // Get all the bookList where coverPath equals to UPDATED_COVER_PATH
        defaultBookShouldNotBeFound("coverPath.in=" + UPDATED_COVER_PATH);
    }

    @Test
    @Transactional
    public void getAllBooksByCoverPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where coverPath is not null
        defaultBookShouldBeFound("coverPath.specified=true");

        // Get all the bookList where coverPath is null
        defaultBookShouldNotBeFound("coverPath.specified=false");
    }

    @Test
    @Transactional
    public void getAllBooksByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where createdBy equals to DEFAULT_CREATED_BY
        defaultBookShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the bookList where createdBy equals to UPDATED_CREATED_BY
        defaultBookShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllBooksByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultBookShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the bookList where createdBy equals to UPDATED_CREATED_BY
        defaultBookShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllBooksByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where createdBy is not null
        defaultBookShouldBeFound("createdBy.specified=true");

        // Get all the bookList where createdBy is null
        defaultBookShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllBooksByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where createdDate equals to DEFAULT_CREATED_DATE
        defaultBookShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the bookList where createdDate equals to UPDATED_CREATED_DATE
        defaultBookShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBooksByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultBookShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the bookList where createdDate equals to UPDATED_CREATED_DATE
        defaultBookShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBooksByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where createdDate is not null
        defaultBookShouldBeFound("createdDate.specified=true");

        // Get all the bookList where createdDate is null
        defaultBookShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllBooksByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultBookShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the bookList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultBookShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllBooksByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultBookShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the bookList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultBookShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllBooksByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where lastModifiedBy is not null
        defaultBookShouldBeFound("lastModifiedBy.specified=true");

        // Get all the bookList where lastModifiedBy is null
        defaultBookShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllBooksByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultBookShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the bookList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultBookShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllBooksByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultBookShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the bookList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultBookShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllBooksByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where lastModifiedDate is not null
        defaultBookShouldBeFound("lastModifiedDate.specified=true");

        // Get all the bookList where lastModifiedDate is null
        defaultBookShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllBooksByYearOfPublishingIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where yearOfPublishing equals to DEFAULT_YEAR_OF_PUBLISHING
        defaultBookShouldBeFound("yearOfPublishing.equals=" + DEFAULT_YEAR_OF_PUBLISHING);

        // Get all the bookList where yearOfPublishing equals to UPDATED_YEAR_OF_PUBLISHING
        defaultBookShouldNotBeFound("yearOfPublishing.equals=" + UPDATED_YEAR_OF_PUBLISHING);
    }

    @Test
    @Transactional
    public void getAllBooksByYearOfPublishingIsInShouldWork() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where yearOfPublishing in DEFAULT_YEAR_OF_PUBLISHING or UPDATED_YEAR_OF_PUBLISHING
        defaultBookShouldBeFound("yearOfPublishing.in=" + DEFAULT_YEAR_OF_PUBLISHING + "," + UPDATED_YEAR_OF_PUBLISHING);

        // Get all the bookList where yearOfPublishing equals to UPDATED_YEAR_OF_PUBLISHING
        defaultBookShouldNotBeFound("yearOfPublishing.in=" + UPDATED_YEAR_OF_PUBLISHING);
    }

    @Test
    @Transactional
    public void getAllBooksByYearOfPublishingIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where yearOfPublishing is not null
        defaultBookShouldBeFound("yearOfPublishing.specified=true");

        // Get all the bookList where yearOfPublishing is null
        defaultBookShouldNotBeFound("yearOfPublishing.specified=false");
    }

    @Test
    @Transactional
    public void getAllBooksByYearOfPublishingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where yearOfPublishing greater than or equals to DEFAULT_YEAR_OF_PUBLISHING
        defaultBookShouldBeFound("yearOfPublishing.greaterOrEqualThan=" + DEFAULT_YEAR_OF_PUBLISHING);

        // Get all the bookList where yearOfPublishing greater than or equals to UPDATED_YEAR_OF_PUBLISHING
        defaultBookShouldNotBeFound("yearOfPublishing.greaterOrEqualThan=" + UPDATED_YEAR_OF_PUBLISHING);
    }

    @Test
    @Transactional
    public void getAllBooksByYearOfPublishingIsLessThanSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where yearOfPublishing less than or equals to DEFAULT_YEAR_OF_PUBLISHING
        defaultBookShouldNotBeFound("yearOfPublishing.lessThan=" + DEFAULT_YEAR_OF_PUBLISHING);

        // Get all the bookList where yearOfPublishing less than or equals to UPDATED_YEAR_OF_PUBLISHING
        defaultBookShouldBeFound("yearOfPublishing.lessThan=" + UPDATED_YEAR_OF_PUBLISHING);
    }


    @Test
    @Transactional
    public void getAllBooksByAuthorFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where authorFirstName equals to DEFAULT_AUTHOR_FIRST_NAME
        defaultBookShouldBeFound("authorFirstName.equals=" + DEFAULT_AUTHOR_FIRST_NAME);

        // Get all the bookList where authorFirstName equals to UPDATED_AUTHOR_FIRST_NAME
        defaultBookShouldNotBeFound("authorFirstName.equals=" + UPDATED_AUTHOR_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllBooksByAuthorFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where authorFirstName in DEFAULT_AUTHOR_FIRST_NAME or UPDATED_AUTHOR_FIRST_NAME
        defaultBookShouldBeFound("authorFirstName.in=" + DEFAULT_AUTHOR_FIRST_NAME + "," + UPDATED_AUTHOR_FIRST_NAME);

        // Get all the bookList where authorFirstName equals to UPDATED_AUTHOR_FIRST_NAME
        defaultBookShouldNotBeFound("authorFirstName.in=" + UPDATED_AUTHOR_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllBooksByAuthorFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where authorFirstName is not null
        defaultBookShouldBeFound("authorFirstName.specified=true");

        // Get all the bookList where authorFirstName is null
        defaultBookShouldNotBeFound("authorFirstName.specified=false");
    }

    @Test
    @Transactional
    public void getAllBooksByAuthorLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where authorLastName equals to DEFAULT_AUTHOR_LAST_NAME
        defaultBookShouldBeFound("authorLastName.equals=" + DEFAULT_AUTHOR_LAST_NAME);

        // Get all the bookList where authorLastName equals to UPDATED_AUTHOR_LAST_NAME
        defaultBookShouldNotBeFound("authorLastName.equals=" + UPDATED_AUTHOR_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllBooksByAuthorLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where authorLastName in DEFAULT_AUTHOR_LAST_NAME or UPDATED_AUTHOR_LAST_NAME
        defaultBookShouldBeFound("authorLastName.in=" + DEFAULT_AUTHOR_LAST_NAME + "," + UPDATED_AUTHOR_LAST_NAME);

        // Get all the bookList where authorLastName equals to UPDATED_AUTHOR_LAST_NAME
        defaultBookShouldNotBeFound("authorLastName.in=" + UPDATED_AUTHOR_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllBooksByAuthorLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList where authorLastName is not null
        defaultBookShouldBeFound("authorLastName.specified=true");

        // Get all the bookList where authorLastName is null
        defaultBookShouldNotBeFound("authorLastName.specified=false");
    }

    @Test
    @Transactional
    public void getAllBooksByProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        Profile profile = ProfileResourceIntTest.createEntity(em);
        em.persist(profile);
        em.flush();
        book.setProfile(profile);
        bookRepository.saveAndFlush(book);
        Long profileId = profile.getId();

        // Get all the bookList where profile equals to profileId
        defaultBookShouldBeFound("profileId.equals=" + profileId);

        // Get all the bookList where profile equals to profileId + 1
        defaultBookShouldNotBeFound("profileId.equals=" + (profileId + 1));
    }


    @Test
    @Transactional
    public void getAllBooksByGenreIsEqualToSomething() throws Exception {
        // Initialize the database
        Genre genre = GenreResourceIntTest.createEntity(em);
        em.persist(genre);
        em.flush();
        book.setGenre(genre);
        bookRepository.saveAndFlush(book);
        Long genreId = genre.getId();

        // Get all the bookList where genre equals to genreId
        defaultBookShouldBeFound("genreId.equals=" + genreId);

        // Get all the bookList where genre equals to genreId + 1
        defaultBookShouldNotBeFound("genreId.equals=" + (genreId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultBookShouldBeFound(String filter) throws Exception {
        restBookMockMvc.perform(get("/api/books?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(book.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].pages").value(hasItem(DEFAULT_PAGES)))
            .andExpect(jsonPath("$.[*].approved").value(hasItem(DEFAULT_APPROVED.booleanValue())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())))
            .andExpect(jsonPath("$.[*].coverPath").value(hasItem(DEFAULT_COVER_PATH.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].yearOfPublishing").value(hasItem(DEFAULT_YEAR_OF_PUBLISHING)))
            .andExpect(jsonPath("$.[*].authorFirstName").value(hasItem(DEFAULT_AUTHOR_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].authorLastName").value(hasItem(DEFAULT_AUTHOR_LAST_NAME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultBookShouldNotBeFound(String filter) throws Exception {
        restBookMockMvc.perform(get("/api/books?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingBook() throws Exception {
        // Get the book
        restBookMockMvc.perform(get("/api/books/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBook() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);
        int databaseSizeBeforeUpdate = bookRepository.findAll().size();

        // Update the book
        Book updatedBook = bookRepository.findOne(book.getId());
        // Disconnect from session so that the updates on updatedBook are not directly saved in db
        em.detach(updatedBook);
        updatedBook
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .pages(UPDATED_PAGES)
            .approved(UPDATED_APPROVED)
            .path(UPDATED_PATH)
            .coverPath(UPDATED_COVER_PATH)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .yearOfPublishing(UPDATED_YEAR_OF_PUBLISHING)
            .authorFirstName(UPDATED_AUTHOR_FIRST_NAME)
            .authorLastName(UPDATED_AUTHOR_LAST_NAME);
        BookDTO bookDTO = bookMapper.toDto(updatedBook);

        restBookMockMvc.perform(put("/api/books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookDTO)))
            .andExpect(status().isOk());

        // Validate the Book in the database
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeUpdate);
        Book testBook = bookList.get(bookList.size() - 1);
        assertThat(testBook.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBook.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBook.getPages()).isEqualTo(UPDATED_PAGES);
        assertThat(testBook.isApproved()).isEqualTo(UPDATED_APPROVED);
        assertThat(testBook.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testBook.getCoverPath()).isEqualTo(UPDATED_COVER_PATH);
        assertThat(testBook.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testBook.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testBook.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testBook.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testBook.getYearOfPublishing()).isEqualTo(UPDATED_YEAR_OF_PUBLISHING);
        assertThat(testBook.getAuthorFirstName()).isEqualTo(UPDATED_AUTHOR_FIRST_NAME);
        assertThat(testBook.getAuthorLastName()).isEqualTo(UPDATED_AUTHOR_LAST_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingBook() throws Exception {
        int databaseSizeBeforeUpdate = bookRepository.findAll().size();

        // Create the Book
        BookDTO bookDTO = bookMapper.toDto(book);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBookMockMvc.perform(put("/api/books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookDTO)))
            .andExpect(status().isCreated());

        // Validate the Book in the database
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBook() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);
        int databaseSizeBeforeDelete = bookRepository.findAll().size();

        // Get the book
        restBookMockMvc.perform(delete("/api/books/{id}", book.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Book.class);
        Book book1 = new Book();
        book1.setId(1L);
        Book book2 = new Book();
        book2.setId(book1.getId());
        assertThat(book1).isEqualTo(book2);
        book2.setId(2L);
        assertThat(book1).isNotEqualTo(book2);
        book1.setId(null);
        assertThat(book1).isNotEqualTo(book2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookDTO.class);
        BookDTO bookDTO1 = new BookDTO();
        bookDTO1.setId(1L);
        BookDTO bookDTO2 = new BookDTO();
        assertThat(bookDTO1).isNotEqualTo(bookDTO2);
        bookDTO2.setId(bookDTO1.getId());
        assertThat(bookDTO1).isEqualTo(bookDTO2);
        bookDTO2.setId(2L);
        assertThat(bookDTO1).isNotEqualTo(bookDTO2);
        bookDTO1.setId(null);
        assertThat(bookDTO1).isNotEqualTo(bookDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(bookMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(bookMapper.fromId(null)).isNull();
    }
}
