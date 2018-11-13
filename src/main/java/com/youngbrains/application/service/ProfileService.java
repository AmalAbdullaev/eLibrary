package com.youngbrains.application.service;

import com.youngbrains.application.domain.Profile;
import com.youngbrains.application.repository.ProfileRepository;
import com.youngbrains.application.service.dto.ProfileDTO;
import com.youngbrains.application.service.mapper.ProfileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Profile.
 */
@Service
@Transactional
public class ProfileService {

    private final Logger log = LoggerFactory.getLogger(ProfileService.class);

    private final ProfileRepository profileRepository;

    private final ProfileMapper profileMapper;

    public ProfileService(ProfileRepository profileRepository, ProfileMapper profileMapper) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
    }

    /**
     * Save a profile.
     *
     * @param profileDTO the entity to save
     * @return the persisted entity
     */
    public ProfileDTO save(ProfileDTO profileDTO) {
        log.debug("Request to save Profile : {}", profileDTO);

        Profile profile = profileMapper.toEntity(profileDTO);
        profile = profileRepository.save(profile);

        return profileMapper.toDto(profile);
    }

    /**
     * Get all the profiles.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ProfileDTO> findAll() {
        log.debug("Request to get all Profiles");
        return profileRepository.findAll().stream()
            .map(profileMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one profile by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ProfileDTO findOne(Long id) {
        log.debug("Request to get Profile : {}", id);
        Profile profile = profileRepository.findOne(id);
        return profileMapper.toDto(profile);
    }


    @Transactional(readOnly = true)
    public ProfileDTO findOneByUserId(Long userId) {
        log.debug("Request to get Profile : {}", userId);
        Profile profile = profileRepository.findProfileByUserId(userId);
        return profileMapper.toDto(profile);
    }
    /**
     * Delete the profile by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Profile : {}", id);
        profileRepository.delete(id);
    }
}
