package com.youngbrains.application.service.mapper;

import com.youngbrains.application.domain.Authority;
import com.youngbrains.application.domain.Profile;
import com.youngbrains.application.domain.User;
import com.youngbrains.application.repository.ProfileRepository;
import com.youngbrains.application.service.dto.ProfileDTO;
import com.youngbrains.application.service.dto.UserDTO;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Mapper for the entity User and its DTO called UserDTO.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class UserMapper {

    private final ProfileRepository profileRepository;

    private final ProfileMapper profileMapper;

    public UserMapper(ProfileRepository profileRepository, ProfileMapper profileMapper) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
    }

    public UserDTO userToUserDTO(User user) {
        return new UserDTO(user);
    }

    public ProfileDTO userToProfileDTO(User user) {
        Profile profile = profileRepository.findProfileByUserId(user.getId());
        ProfileDTO profileDTO = profileMapper.toDto(profile);
        profileDTO.setUserEmail(user.getEmail());
        profileDTO.setUserFirstName(user.getFirstName());
        profileDTO.setUserLastName(user.getLastName());
        profileDTO.setUserLogin(user.getLogin());
        return profileDTO;
    }

    public List<UserDTO> usersToUserDTOs(List<User> users) {
        return users.stream()
            .filter(Objects::nonNull)
            .map(this::userToUserDTO)
            .collect(Collectors.toList());
    }

    public User userDTOToUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        } else {
            User user = new User();
            user.setId(userDTO.getId());
            user.setLogin(userDTO.getLogin());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setImageUrl(userDTO.getImageUrl());
            user.setActivated(userDTO.isActivated());
            user.setLangKey(userDTO.getLangKey());
            Set<Authority> authorities = this.authoritiesFromStrings(userDTO.getAuthorities());
            if (authorities != null) {
                user.setAuthorities(authorities);
            }
            return user;
        }
    }

    public List<User> userDTOsToUsers(List<UserDTO> userDTOs) {
        return userDTOs.stream()
            .filter(Objects::nonNull)
            .map(this::userDTOToUser)
            .collect(Collectors.toList());
    }

    public User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }

    public Set<Authority> authoritiesFromStrings(Set<String> strings) {
        return strings.stream().map(string -> {
            Authority auth = new Authority();
            auth.setName(string);
            return auth;
        }).collect(Collectors.toSet());
    }
}
