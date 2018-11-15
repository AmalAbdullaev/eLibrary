package com.youngbrains.application.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Profile entity.
 */
public class ProfileDTO implements Serializable {

    private Long id;

    private Boolean trusted;

    private Boolean banned;

    private Long userId;

    private String userLogin;

    private String userEmail;

    private String userFirstName;

    private String userLastName;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isTrusted() {
        return trusted;
    }

    public void setTrusted(Boolean trusted) {
        this.trusted = trusted;
    }

    public Boolean isBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    @Override
    public String toString() {
        return "ProfileDTO{" +
            "id=" + id +
            ", trusted=" + trusted +
            ", banned=" + banned +
            ", userId=" + userId +
            ", userLogin='" + userLogin + '\'' +
            ", userEmail='" + userEmail + '\'' +
            ", userFirstName='" + userFirstName + '\'' +
            ", userLastName='" + userLastName + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProfileDTO profileDTO = (ProfileDTO) o;
        if(profileDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), profileDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
