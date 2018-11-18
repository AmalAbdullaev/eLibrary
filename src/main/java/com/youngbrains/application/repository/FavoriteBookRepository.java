package com.youngbrains.application.repository;

import com.youngbrains.application.domain.FavoriteBook;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the FavoriteBook entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FavoriteBookRepository extends JpaRepository<FavoriteBook, Long>, JpaSpecificationExecutor<FavoriteBook> {

}
