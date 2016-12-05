/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.google.cal.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.security.Principal;
import javax.security.auth.Subject;

/**
 *
 * @author dhenton
 */
public class UserInfo implements Serializable, Principal {

    private final String id;
    private final String name;
    private final String givenName;
    private final String familyName;
    private final String gender;
    private final String picture;
    private final String link;

    @JsonCreator
    public UserInfo(@JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("given_name") String givenName,
            @JsonProperty("family_name") String familyName,
            @JsonProperty("gender") String gender,
            @JsonProperty("picture") String picture,
            @JsonProperty("link") String link) {
        this.id = id;
        this.name = name;
        this.givenName = givenName;
        this.familyName = familyName;
        this.gender = gender;
        this.picture = picture;
        this.link = link;
    }
    
    

    @Override
    public String getName() {
         return name;
    }

    

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the givenName
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * @return the familyName
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @return the picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }
}
