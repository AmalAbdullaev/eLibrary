package com.youngbrains.application.web.rest;

import com.youngbrains.application.ELibraryApp;

import com.youngbrains.application.domain.ReadBook;
import com.youngbrains.application.repository.ReadBookRepository;
import com.youngbrains.application.service.ReadBookService;
import com.youngbrains.application.service.dto.ReadBookDTO;
import com.youngbrains.application.service.mapper.ReadBookMapper;
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
import java.util.List;

import static com.youngbrains.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ReadBookResource REST controller.
 *
 * @see ReadBookResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ELibraryApp.class)
public class ReadBookResourceIntTest {

    @Autowired
    private ReadBookRepository readBookRepository;

    @Autowired
    private ReadBookMapper readBookMapper;

    @Autowired
    private ReadBookService readBookService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReadBookMockMvc;

    private ReadBook readBook;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReadBookResource readBookResource = new ReadBookResource(readBookService);
        this.restReadBookMockMvc = MockMvcBuilders.standaloneSetup(readBookResource)
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
    public static ReadBook createEntity(EntityManager em) {
        ReadBook readBook = new ReadBook();
        return readBook;
    }

    @Before
    public void initTest() {
        readBook = createEntity(em);
    }

    @Test
    @Transactional
    public void createReadBook() throws Exception {
        int databaseSizeBeforeCreate = readBookRepository.findAll().size();

        // Create the ReadBook
        ReadBookDTO readBookDTO = readBookMapper.toDto(readBook);
        restReadBookMockMvc.perform(post("/api/read-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(readBookDTO)))
            .andExpect(status().isCreated());

        // Validate the ReadBook in the database
        List<ReadBook> readBookList = readBookRepository.findAll();
        assertThat(readBookList).hasSize(databaseSizeBeforeCreate + 1);
        ReadBook testReadBook = readBookList.get(readBookList.size() - 1);
    }

    @Test
    @Transactional
    public void createReadBookWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = readBookRepository.findAll().size();

        // Create the ReadBook with an existing ID
        readBook.setId(1L);
        ReadBookDTO readBookDTO = readBookMapper.toDto(readBook);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReadBookMockMvc.perform(post("/api/read-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(readBookDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReadBook in the database
        List<ReadBook> readBookList = readBookRepository.findAll();
        assertThat(readBookList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllReadBooks() throws Exception {
        // Initialize the database
        readBookRepository.saveAndFlush(readBook);

        // Get all the readBookList
        restReadBookMockMvc.perform(get("/api/read-books?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(readBook.getId().intValue())));
    }

    @Test
    @Transactional
    public void getReadBook() throws Exception {
        // Initialize the database
        readBookRepository.saveAndFlush(readBook);

        // Get the readBook
        restReadBookMockMvc.perform(get("/api/read-books/{id}", readBook.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(readBook.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingReadBook() throws Exception {
        // Get the readBook
        restReadBookMockMvc.perform(get("/api/read-books/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReadBook() throws Exception {
        // Initialize the database
        readBookRepository.saveAndFlush(readBook);
        int databaseSizeBeforeUpdate = readBookRepository.findAll().size();

        // Update the readBook
        ReadBook updatedReadBook = readBookRepository.findOne(readBook.getId());
        // Disconnect from session so that the updates on updatedReadBook are not directly saved in db
        em.detach(updatedReadBook);
        ReadBookDTO readBookDTO = readBookMapper.toDto(updatedReadBook);

        restReadBookMockMvc.perform(put("/api/read-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(readBookDTO)))
            .andExpect(status().isOk());

        // Validate the ReadBook in the database
        List<ReadBook> readBookList = readBookRepository.findAll();
        assertThat(readBookList).hasSize(databaseSizeBeforeUpdate);
        ReadBook testReadBook = readBookList.get(readBookList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingReadBook() throws Exception {
        int databaseSizeBeforeUpdate = readBookRepository.findAll().size();

        // Create the ReadBook
        ReadBookDTO readBookDTO = readBookMapper.toDto(readBook);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReadBookMockMvc.perform(put("/api/read-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(readBookDTO)))
            .andExpect(status().isCreated());

        // Validate the ReadBook in the database
        List<ReadBook> readBookList = readBookRepository.findAll();
        assertThat(readBookList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReadBook() throws Exception {
        // Initialize the database
        readBookRepository.saveAndFlush(readBook);
        int databaseSizeBeforeDelete = readBookRepository.findAll().size();

        // Get the readBook
        restReadBookMockMvc.perform(delete("/api/read-books/{id}", readBook.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ReadBook> readBookList = readBookRepository.findAll();
        assertThat(readBookList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReadBook.class);
        ReadBook readBook1 = new ReadBook();
        readBook1.setId(1L);
        ReadBook readBook2 = new ReadBook();
        readBook2.setId(readBook1.getId());
        assertThat(readBook1).isEqualTo(readBook2);
        readBook2.setId(2L);
        assertThat(readBook1).isNotEqualTo(readBook2);
        readBook1.setId(null);
        assertThat(readBook1).isNotEqualTo(readBook2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReadBookDTO.class);
        ReadBookDTO readBookDTO1 = new ReadBookDTO();
        readBookDTO1.setId(1L);
        ReadBookDTO readBookDTO2 = new ReadBookDTO();
        assertThat(readBookDTO1).isNotEqualTo(readBookDTO2);
        readBookDTO2.setId(readBookDTO1.getId());
        assertThat(readBookDTO1).isEqualTo(readBookDTO2);
        readBookDTO2.setId(2L);
        assertThat(readBookDTO1).isNotEqualTo(readBookDTO2);
        readBookDTO1.setId(null);
        assertThat(readBookDTO1).isNotEqualTo(readBookDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(readBookMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(readBookMapper.fromId(null)).isNull();
    }
}
