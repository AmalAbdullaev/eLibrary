package com.youngbrains.application.repository;

import com.youngbrains.application.domain.Profile;
import com.youngbrains.application.service.dto.ProfileDTO;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.Optional;


/**
 * Spring Data JPA repository for the Profile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findProfileByUserId(Long userId);
}
