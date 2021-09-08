package com.hobby.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Data
public class Profile {
    @Id
    @GeneratedValue
    private long id;
    @OneToOne
    User user;
    private String designation;
    private String description;
    private String profilePictureFileName;
}
