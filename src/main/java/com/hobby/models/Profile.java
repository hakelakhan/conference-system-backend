package com.hobby.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@Data
public class Profile {
    @OneToOne
    User user;
    private String designation;
    private String description;
    private String profilePictureFileName;
}
