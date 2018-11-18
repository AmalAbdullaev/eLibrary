package com.youngbrains.application.web.rest;

import com.youngbrains.application.ELibraryApp;

import com.youngbrains.application.domain.FavoriteBook;
import com.youngbrains.application.repository.FavoriteBookRepository;
import com.youngbrains.application.service.FavoriteBookQueryService;
import com.youngbrains.application.service.FavoriteBookService;
import com.youngbrains.application.service.dto.FavoriteBookDTO;
import com.youngbrains.application.service.mapper.FavoriteBookMapper;
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
 * Test class for the FavoriteBookResource REST controller.
 *
 * @see FavoriteBookResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ELibraryApp.class)
public class FavoriteBookResourceIntTest {

    @Autowired
    private FavoriteBookRepository favoriteBookRepository;

    @Autowired
    private FavoriteBookMapper favoriteBookMapper;

    @Autowired
    private FavoriteBookService favoriteBookService;

    @Autowired
    private FavoriteBookQueryService favoriteBookQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFavoriteBookMockMvc;

    private FavoriteBook favoriteBook;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FavoriteBookResource favoriteBookResource = new FavoriteBookResource(favoriteBookService, favoriteBookQueryService);
        this.restFavoriteBookMockMvc = MockMvcBuilders.standaloneSetup(favoriteBookResource)
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
    public static FavoriteBook createEntity(EntityManager em) {
        FavoriteBook favoriteBook = new FavoriteBook();
        return favoriteBook;
    }

    @Before
    public void initTest() {
        favoriteBook = createEntity(em);
    }

    @Test
    @Transactional
    public void createFavoriteBook() throws Exception {
        int databaseSizeBeforeCreate = favoriteBookRepository.findAll().size();

        // Create the FavoriteBook
        FavoriteBookDTO favoriteBookDTO = favoriteBookMapper.toDto(favoriteBook);
        restFavoriteBookMockMvc.perform(post("/api/favorite-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(favoriteBookDTO)))
            .andExpect(status().isCreated());

        // Validate the FavoriteBook in the database
        List<FavoriteBook> favoriteBookList = favoriteBookRepository.findAll();
        assertThat(favoriteBookList).hasSize(databaseSizeBeforeCreate + 1);
        FavoriteBook testFavoriteBook = favoriteBookList.get(favoriteBookList.size() - 1);
    }

    @Test
    @Transactional
    public void createFavoriteBookWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = favoriteBookRepository.findAll().size();

        // Create the FavoriteBook with an existing ID
        favoriteBook.setId(1L);
        FavoriteBookDTO favoriteBookDTO = favoriteBookMapper.toDto(favoriteBook);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFavoriteBookMockMvc.perform(post("/api/favorite-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(favoriteBookDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FavoriteBook in the database
        List<FavoriteBook> favoriteBookList = favoriteBookRepository.findAll();
        assertThat(favoriteBookList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFavoriteBooks() throws Exception {
        // Initialize the database
        favoriteBookRepository.saveAndFlush(favoriteBook);

        // Get all the favoriteBookList
        restFavoriteBookMockMvc.perform(get("/api/favorite-books?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(favoriteBook.getId().intValue())));
    }

    @Test
    @Transactional
    public void getFavoriteBook() throws Exception {
        // Initialize the database
        favoriteBookRepository.saveAndFlush(favoriteBook);

        // Get the favoriteBook
        restFavoriteBookMockMvc.perform(get("/api/favorite-books/{id}", favoriteBook.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(favoriteBook.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFavoriteBook() throws Exception {
        // Get the favoriteBook
        restFavoriteBookMockMvc.perform(get("/api/favorite-books/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFavoriteBook() throws Exception {
        // Initialize the database
        favoriteBookRepository.saveAndFlush(favoriteBook);
        int databaseSizeBeforeUpdate = favoriteBookRepository.findAll().size();

        // Update the favoriteBook
        FavoriteBook updatedFavoriteBook = favoriteBookRepository.findOne(favoriteBook.getId());
        // Disconnect from session so that the updates on updatedFavoriteBook are not directly saved in db
        em.detach(updatedFavoriteBook);
        FavoriteBookDTO favoriteBookDTO = favoriteBookMapper.toDto(updatedFavoriteBook);

        restFavoriteBookMockMvc.perform(put("/api/favorite-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(favoriteBookDTO)))
            .andExpect(status().isOk());

        // Validate the FavoriteBook in the database
        List<FavoriteBook> favoriteBookList = favoriteBookRepository.findAll();
        assertThat(favoriteBookList).hasSize(databaseSizeBeforeUpdate);
        FavoriteBook testFavoriteBook = favoriteBookList.get(favoriteBookList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingFavoriteBook() throws Exception {
        int databaseSizeBeforeUpdate = favoriteBookRepository.findAll().size();

        // Create the FavoriteBook
        FavoriteBookDTO favoriteBookDTO = favoriteBookMapper.toDto(favoriteBook);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFavoriteBookMockMvc.perform(put("/api/favorite-books")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(favoriteBookDTO)))
            .andExpect(status().isCreated());

        // Validate the FavoriteBook in the database
        List<FavoriteBook> favoriteBookList = favoriteBookRepository.findAll();
        assertThat(favoriteBookList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFavoriteBook() throws Exception {
        // Initialize the database
        favoriteBookRepository.saveAndFlush(favoriteBook);
        int databaseSizeBeforeDelete = favoriteBookRepository.findAll().size();

        // Get the favoriteBook
        restFavoriteBookMockMvc.perform(delete("/api/favorite-books/{id}", favoriteBook.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FavoriteBook> favoriteBookList = favoriteBookRepository.findAll();
        assertThat(favoriteBookList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FavoriteBook.class);
        FavoriteBook favoriteBook1 = new FavoriteBook();
        favoriteBook1.setId(1L);
        FavoriteBook favoriteBook2 = new FavoriteBook();
        favoriteBook2.setId(favoriteBook1.getId());
        assertThat(favoriteBook1).isEqualTo(favoriteBook2);
        favoriteBook2.setId(2L);
        assertThat(favoriteBook1).isNotEqualTo(favoriteBook2);
        favoriteBook1.setId(null);
        assertThat(favoriteBook1).isNotEqualTo(favoriteBook2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FavoriteBookDTO.class);
        FavoriteBookDTO favoriteBookDTO1 = new FavoriteBookDTO();
        favoriteBookDTO1.setId(1L);
        FavoriteBookDTO favoriteBookDTO2 = new FavoriteBookDTO();
        assertThat(favoriteBookDTO1).isNotEqualTo(favoriteBookDTO2);
        favoriteBookDTO2.setId(favoriteBookDTO1.getId());
        assertThat(favoriteBookDTO1).isEqualTo(favoriteBookDTO2);
        favoriteBookDTO2.setId(2L);
        assertThat(favoriteBookDTO1).isNotEqualTo(favoriteBookDTO2);
        favoriteBookDTO1.setId(null);
        assertThat(favoriteBookDTO1).isNotEqualTo(favoriteBookDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(favoriteBookMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(favoriteBookMapper.fromId(null)).isNull();
    }
}
