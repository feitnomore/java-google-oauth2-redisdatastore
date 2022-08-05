/* Filename: GoogleCredentialRepository.java
 * Author: Marcelo Feitoza Parisi
 * 
 * Description: Google Credential Repository Interface
 * Responsible for working with Google Oauth2
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
package com.github.feitnomore.googleoauth2.redisdatastore.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import com.github.feitnomore.googleoauth2.redisdatastore.beans.GoogleCredential;


public interface GoogleCredentialRepository {

    GoogleCredential findByKey(String key);
    GoogleCredential findById(String key);
    GoogleCredential findByAccessToken(String key);
    Set<String> findAllKeys();
    Collection<GoogleCredential> findAll();
    List<GoogleCredential> findAllById(String key);
    GoogleCredential getById(String key);
    void deleteAllInBatch();
    void deleteAllInBatch(List<String> keys);
    void save(GoogleCredential credential);
    boolean exists(String key);
    boolean exists(GoogleCredential credential);
    boolean existsById(String key);
    void deleteAllById(String key);
    void deleteById(String key);
    int count();

}