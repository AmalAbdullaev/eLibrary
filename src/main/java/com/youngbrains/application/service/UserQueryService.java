package com.youngbrains.application.service;


import com.youngbrains.application.domain.*;
import com.youngbrains.application.repository.UserRepository;
import com.youngbrains.application.repository.UserRepository;
import com.youngbrains.application.service.dto.UserCriteria;
import com.youngbrains.application.service.dto.UserDTO;
import com.youngbrains.application.service.mapper.UserMapper;
import com.youngbrains.application.service.mapper.UserMapper;
import io.github.jhipster.service.QueryService;
import io.github.jhipster.service.filter.StringFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserQueryService extends QueryService<User> {

    private final Logger log = LoggerFactory.getLogger(UserQueryService.class);


    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserQueryService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional(readOnly = true)
    public List<UserDTO> findByCriteria(UserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<User> specification = createSpecification(criteria);
        return userMapper.usersToUserDTOs(userRepository.findAll(specification));
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> findByCriteria(UserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<User> specification = createSpecification(criteria);
        final Page<User> result = userRepository.findAll(specification, page);
        return result.map(userMapper::userToUserDTO);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> findByLoginOrName(String searchText, Pageable page) {
        log.debug("find by search text : {}, page: {}", searchText, page);
        UserCriteria criteria = new UserCriteria();

        StringFilter loginFilter = new StringFilter();
        loginFilter.setContains(searchText);
        criteria.setLogin(loginFilter);

        StringFilter emailFilter = new StringFilter();
        emailFilter.setContains(searchText);
        criteria.setEmail(emailFilter);

        StringFilter lastNameFilter = new StringFilter();
        lastNameFilter.setContains(searchText);
        criteria.setLastName(lastNameFilter);

        StringFilter firstNameFilter = new StringFilter();
        firstNameFilter.setContains(searchText);
        criteria.setFirstName(firstNameFilter);

        final Specifications<User> specification = createDisjunctiveSpecification(criteria);
        final Page<User> result = userRepository.findAll(specification, page);
        return result.map(userMapper::userToUserDTO);
    }

    private Specifications<User> createSpecification(UserCriteria criteria) {
        Specifications<User> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), User_.id));
            }
            if (criteria.getLogin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogin(), User_.login));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), User_.password));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), User_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), User_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), User_.email));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), User_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), User_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), User_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), User_.lastModifiedDate));
            }
            if (criteria.getActivated() != null) {
                specification = specification.and(buildSpecification(criteria.getActivated(), User_.activated));
            }
            if (criteria.getLangKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLangKey(), User_.langKey));
            }
            if (criteria.getImageUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageUrl(), User_.imageUrl));
            }
            if (criteria.getActivationKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getActivationKey(), User_.activationKey));
            }
            if (criteria.getResetKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResetKey(), User_.resetKey));
            }
            if (criteria.getResetDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getResetDate(), User_.resetDate));
            }
        }
        return specification;
    }


    private Specifications<User> createDisjunctiveSpecification(UserCriteria criteria) {
        Specifications<User> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getLogin() != null) {
                specification = specification.or(buildStringSpecification(criteria.getLogin(), User_.login));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.or(buildStringSpecification(criteria.getFirstName(), User_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.or(buildStringSpecification(criteria.getLastName(), User_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.or(buildStringSpecification(criteria.getEmail(), User_.email));
            }
        }
        return specification;
    }
}
