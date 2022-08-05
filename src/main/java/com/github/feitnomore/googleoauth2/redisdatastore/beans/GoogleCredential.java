/* Filename: GoogleCredential.java
 * Author: Marcelo Feitoza Parisi
 * 
 * Description: Credential Bean
 * Responsible for User Credential
 * 
 * ###########################
 * # DISCLAIMER - IMPORTANT! #
 * ###########################
 * 
 * Stuff found here was built as a
 * Proof-Of-Concept or Study material
 * and should not be considered
 * production ready!
 * 
 * USE WITH CARE!
 */

package com.github.feitnomore.googleoauth2.redisdatastore.beans;

import java.time.Instant;
import java.io.Serializable;
import com.google.api.client.auth.oauth2.StoredCredential;

public class GoogleCredential implements Serializable {

    /* Data for this credential */
    private String key;
    private String accessToken;
    private Long expirationTimeMilliseconds;
    private String refreshToken;
    private Instant createdAt;
    private Instant updatedAt;


    public GoogleCredential (String key, StoredCredential credential) {
        this.key = key;
        this.accessToken = credential.getAccessToken();
        this.expirationTimeMilliseconds = credential.getExpirationTimeMilliseconds();
        this.refreshToken = credential.getRefreshToken();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void apply(StoredCredential credential) {
        this.accessToken = credential.getAccessToken();
        this.expirationTimeMilliseconds = credential.getExpirationTimeMilliseconds();
        this.refreshToken = credential.getRefreshToken();
        this.updatedAt = Instant.now();
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
       this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpirationTimeMilliseconds() {
        return this.expirationTimeMilliseconds;
    }

    public void setExpirationTimeMilliseconds(Long expirationTimeMilliseconds) {
        this.expirationTimeMilliseconds = expirationTimeMilliseconds;
    }

    public Instant getcreatedAt() {
        return this.createdAt;
    }

    public void setcreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getupdatedAt() {
        return this.updatedAt;
    }

    public void setupdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
