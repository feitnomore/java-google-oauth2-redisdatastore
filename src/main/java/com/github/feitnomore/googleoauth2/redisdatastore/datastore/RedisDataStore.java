/* Filename: RedisDataStore.java
 * Author: Marcelo Feitoza Parisi
 * 
 * Description: DataStore
 * Holds our DataStore methods
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

package com.github.feitnomore.googleoauth2.redisdatastore.datastore;

import java.util.Set;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.util.store.AbstractDataStore;
import com.github.feitnomore.googleoauth2.redisdatastore.beans.GoogleCredential;
import com.github.feitnomore.googleoauth2.redisdatastore.repository.GoogleCredentialRepository;

public class RedisDataStore extends AbstractDataStore<StoredCredential> {

    private GoogleCredentialRepository repository;
    private RedisDataStoreFactory dataStoreFactory;

    protected RedisDataStore(RedisDataStoreFactory dataStoreFactory, String id, GoogleCredentialRepository repository) {
        super(dataStoreFactory, id);
        this.repository = repository;
        this.dataStoreFactory = dataStoreFactory;
    }

    public RedisDataStoreFactory getDataStoreFactory() {
        return this.dataStoreFactory;
    }

    @Override
    public Set<String> keySet() throws IOException {
        return repository.findAllKeys();
    }

    @Override
    public Collection<StoredCredential> values() throws IOException {
        Collection<StoredCredential> myColletion = new ArrayList<>();
        myColletion.clear();
        try {
            Collection<GoogleCredential> googleCollection = repository.findAll();
            for(GoogleCredential thisCredential : googleCollection) {
                StoredCredential newCredential = new StoredCredential();
                newCredential.setAccessToken(thisCredential.getAccessToken());
                newCredential.setRefreshToken(thisCredential.getRefreshToken());
                newCredential.setExpirationTimeMilliseconds(thisCredential.getExpirationTimeMilliseconds());
                myColletion.add(newCredential);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myColletion;
    }

    @Override
    public StoredCredential get(String key) throws IOException {
        try {
            GoogleCredential thisCredential = repository.findByKey(key);
            StoredCredential newCredential = new StoredCredential();
            newCredential.setAccessToken(thisCredential.getAccessToken());
            newCredential.setRefreshToken(thisCredential.getRefreshToken());
            newCredential.setExpirationTimeMilliseconds(thisCredential.getExpirationTimeMilliseconds());
            return newCredential;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public DataStore<StoredCredential> set(String key, StoredCredential value) throws IOException {
        GoogleCredential newCredential = new GoogleCredential(key, value);
        repository.save(newCredential);
        return this;
    }

    @Override
    public DataStore<StoredCredential> clear() throws IOException {
        repository.deleteAllInBatch();
        return this;
    }

    @Override
    public DataStore<StoredCredential> delete(String key) throws IOException {
        repository.deleteAllById(key);
        return this;
    }

    @Override
    public int size() {
        return repository.count();
    }

    @Override
    public boolean isEmpty() {
        return repository.count() == 0;
    }

    @Override
    public boolean containsKey(String key) {
        return repository.exists(key);
    }

    @Override
    public boolean containsValue(StoredCredential value) {
        try {
            GoogleCredential thisCred = repository.findByAccessToken(value.getAccessToken());
            if (!thisCred.getAccessToken().isEmpty()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public DataStore<StoredCredential> getDataStore() throws IOException {
        return this;
    }
    
}
