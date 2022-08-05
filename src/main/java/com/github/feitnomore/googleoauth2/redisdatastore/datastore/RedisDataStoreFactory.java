/* Filename: RedisDataStoreFactory.java
 * Author: Marcelo Feitoza Parisi
 * 
 * Description: DataStore Factory
 * Responsible for creating our DataStore
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

import java.io.IOException;
import com.google.api.client.util.store.DataStoreFactory;
import com.github.feitnomore.googleoauth2.redisdatastore.repository.GoogleCredentialRepository;

public class RedisDataStoreFactory implements DataStoreFactory {

    private GoogleCredentialRepository repository;

    public RedisDataStoreFactory(GoogleCredentialRepository repository) {
        this.repository = repository;

    }

    @Override
    @SuppressWarnings("unchecked")
    public RedisDataStore getDataStore(String id) throws IOException {
        return new RedisDataStore(this, id, repository);
    }



   

}
