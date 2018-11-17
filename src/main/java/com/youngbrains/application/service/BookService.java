package com.youngbrains.application.service;

import com.youngbrains.application.config.BookStorageProperties;
import com.youngbrains.application.domain.Book;
import com.youngbrains.application.repository.BookRepository;
import com.youngbrains.application.service.dto.BookDTO;
import com.youngbrains.application.service.mapper.BookMapper;
import io.undertow.util.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;


/**
 * Service Implementation for managing Book.
 */
@Service
@Transactional
public class BookService {

    private final Logger log = LoggerFactory.getLogger(BookService.class);

    private final Path bookStorageLocation;

    private final Path coverStorageLocation;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper, BookStorageProperties storageProperties) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.bookStorageLocation = Paths.get(storageProperties.getUploadDir());
        this.coverStorageLocation = Paths.get(storageProperties.getCoverUploadDir());
        try {
            Files.createDirectories(Paths.get(storageProperties.getUploadDir()));
            Files.createDirectories(Paths.get(storageProperties.getCoverUploadDir()));
        } catch (IOException e) {
            log.error("Could not create the directory where the uploaded books will be stored");
        }

    }

    /**
     * Save a book.
     *
     * @param bookDTO the entity to save
     * @return the persisted entity
     */
    public BookDTO save(BookDTO bookDTO) {
        log.debug("Request to save Book : {}", bookDTO);
        Book book = bookMapper.toEntity(bookDTO);
        book = bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    /**
     * Get all the books.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BookDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Books");
        return bookRepository.findAll(pageable)
            .map(bookMapper::toDto);
    }

    /**
     * Get one book by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public BookDTO findOne(Long id) {
        log.debug("Request to get Book : {}", id);
        Book book = bookRepository.findOne(id);
        return bookMapper.toDto(book);
    }

    /**
     * Delete the book by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Book : {}", id);
        bookRepository.delete(id);
    }

    /**
     * Upload the book by id and upload operation type
     *
     * @param file file containing a book
     * @param id   the id of the book
     * @param type type of upload operation: "book" or "cover"
     * @return DTO of the uploaded book
     * @throws BadRequestException if the "type" parameter is incorrect
     */
    public BookDTO uploadBook(MultipartFile file, Long id, String type) throws BadRequestException, FileSystemException {
        Book book = bookRepository.getOne(id);
        BookDTO bookDTO = bookMapper.toDto(book);
        String extension = "";
        int start = file.getOriginalFilename().lastIndexOf('.');
        if (start != -1)
            extension = file.getOriginalFilename().substring(start).trim();
        String fileName = StringUtils.cleanPath(bookDTO.getTitle() + extension);
        Path targetLocation;
        switch (type) {
            case "book": {
                Path profileDir = bookStorageLocation.resolve(bookDTO.getProfileId().toString());
                boolean dirExists = Files.exists(profileDir);
                if (!dirExists) {
                    try {
                        Files.createDirectories(profileDir);
                    } catch (IOException e) {
                        throw new FileSystemException("Could not create profile directory: " + profileDir.getFileName());
                    }
                }
                targetLocation = profileDir.resolve(String.valueOf(id) + "-" + fileName);
                bookDTO.setPath(targetLocation.toString());
                break;
            }
            case "cover": {
                Path profileDir = coverStorageLocation.resolve(bookDTO.getProfileId().toString());
                boolean dirExists = Files.exists(profileDir);
                if (!dirExists)
                    try {
                        Files.createDirectories(profileDir);
                    } catch (IOException e) {
                        throw new FileSystemException("Could not create profile directory: " + profileDir.getFileName());
                    }
                targetLocation = profileDir.resolve(String.valueOf(id) + "-cover-" + fileName);
                bookDTO.setCoverPath(targetLocation.toString());
                break;
            }
            default:
                throw new BadRequestException("Unknown parameter: " + type);
        }
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileSystemException("Could not store file " + fileName);
        }
        return save(bookDTO);
    }

    /**
     * Loads the file located on the path "path"
     *
     * @param path location of a book
     * @return resource corresponding to the requested book
     * @throws MalformedURLException if file is not found
     */
    public Resource loadBookAsResource(String path) throws MalformedURLException {
        Path bookPath = Paths.get(path);
        Resource resource = new UrlResource(bookPath.toUri());
        if (resource.exists())
            return resource;
        else
            throw new MalformedURLException("File not found: " + path);
    }
}
