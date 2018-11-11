package com.youngbrains.application.service.mapper;

import com.youngbrains.application.domain.*;
import com.youngbrains.application.service.dto.ProfileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Profile and its DTO ProfileDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ProfileMapper extends EntityMapper<ProfileDTO, Profile> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.firstName", target = "userFirstName")
    @Mapping(source = "user.lastName", target = "userLastName")
    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(source = "user.login", target = "userLogin")
    ProfileDTO toDto(Profile profile);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "userFirstName", target = "user.firstName")
    @Mapping(source = "userLastName", target = "user.lastName")
    @Mapping(source = "userEmail", target = "user.email")
    @Mapping(source = "userLogin", target = "user.login")
    Profile toEntity(ProfileDTO profileDTO);


    default Profile fromId(Long id) {
        if (id == null) {
            return null;
        }
        Profile profile = new Profile();
        profile.setId(id);
        return profile;
    }
}
