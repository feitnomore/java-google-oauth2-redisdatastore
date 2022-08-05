/* Filename: GoogleCredentialRepositoryImpl.java
 * Author: Marcelo Feitoza Parisi
 * 
 * Description: Google Credential Repository Implementation
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.github.feitnomore.googleoauth2.redisdatastore.beans.GoogleCredential;
import com.github.feitnomore.googleoauth2.redisdatastore.controllers.RedisController;


public class GoogleCredentialRepositoryImpl implements GoogleCredentialRepository {

    private RedisController myRedisController;

    public GoogleCredentialRepositoryImpl() {
        this.myRedisController = new RedisController();
        this.myRedisController.connect();
    }

    public GoogleCredential findByKey(String key) {
        return this.myRedisController.findByKey(key);
    }

    public GoogleCredential findById(String key) {
        return this.myRedisController.findByKey(key);
    }

    public GoogleCredential findByAccessToken(String key) {
        return this.myRedisController.findByAccessToken(key);
    }

    public Set<String> findAllKeys() {
        return this.myRedisController.findAllKeys();
    }

    public Collection<GoogleCredential> findAll() {
        return this.myRedisController.findAll();
    }

    public List<GoogleCredential> findAllById(String key) {
        Collection<GoogleCredential> thisCollection = this.myRedisController.findAllByKey(key);
        List<GoogleCredential> myList = new ArrayList<GoogleCredential>();
        for (GoogleCredential thisCred : thisCollection) {
            myList.add(thisCred);
        }
        return myList;
    }

    public GoogleCredential getById(String key) {
        return this.myRedisController.findByKey(key);
    }

    public void deleteAllInBatch() {
        this.myRedisController.deleteAll();
    }

    public void deleteAllInBatch(List<String> keys) {
        for(String thisKey : keys) {
            this.myRedisController.deleteByKey(thisKey);
        }
    }

    public void save(GoogleCredential credential) {
        this.myRedisController.saveRecord(credential);
    }

    public boolean exists(String key) {
        return this.myRedisController.containsKey(key);
    }

    public boolean exists(GoogleCredential credential) {
        GoogleCredential retrCred = this.myRedisController.findByObject(credential);
        if (retrCred.getAccessToken().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean existsById(String key) {
        return this.myRedisController.containsKey(key);
    }

    public void deleteAllById(String key) {

    }

    public void deleteById(String key) {
        this.myRedisController.deleteByKey(key);
    }

    public int count() {
        return this.myRedisController.getSize();
    }
    
}