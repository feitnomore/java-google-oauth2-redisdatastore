/* Filename: RedisController.java
 * Author: Marcelo Feitoza Parisi
 * 
 * Description: Redis Controller
 * Responsible for working with Redis
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

package com.github.feitnomore.googleoauth2.redisdatastore.controllers;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collection;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.config.Config;
import org.redisson.api.RedissonClient;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.github.feitnomore.googleoauth2.redisdatastore.beans.GoogleCredential;

public class RedisController {

    private String redisHost = System.getenv("REDIS_HOST");
    private String redisPort = System.getenv("REDIS_PORT");
    private String redisName = System.getenv("REDIS_NAME");

    private RedissonClient redisConn;
    private Config redisConfig;

    public RedisController() {
        this.redisConfig = new Config();

        redisConfig.useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort);

    }

    public void connect() {
        this.redisConn = Redisson.create(this.redisConfig);
    }

    public void saveRecord(GoogleCredential thisCredential) {
        RMap<String, GoogleCredential> credentialMap = redisConn.getMap(redisName);
        GoogleCredential storedCredential = credentialMap.put(thisCredential.getKey(), thisCredential);
    }

    public void deleteByKey(String key) {
        RMap<String, GoogleCredential> credentialMap = redisConn.getMap(redisName);
        credentialMap.remove(key);
    }

    public void deleteAll() {
        RMap<String, GoogleCredential> credentialMap = redisConn.getMap(redisName);
        credentialMap.delete();
    }

    public GoogleCredential printRecord(String key) {
        RMap<String, GoogleCredential> credentialMap = redisConn.getMap(redisName);
        GoogleCredential retrCredential = credentialMap.get(key);
        return retrCredential;
    }

    public GoogleCredential findByObject(GoogleCredential thisObj) {
        RMap<String, GoogleCredential> credentialMap = redisConn.getMap(redisName);
        GoogleCredential retrCredential = credentialMap.get(thisObj.getKey());
        return retrCredential;
    }

    public GoogleCredential findByKey(String key) {
        RMap<String, GoogleCredential> credentialMap = redisConn.getMap(redisName);
        GoogleCredential retrCredential = credentialMap.get(key);
        return retrCredential;
    }

    public GoogleCredential findByAccessToken(String key) {
        GoogleCredential thisCred = null;
        RMap<String, GoogleCredential> credentialMap = redisConn.getMap(redisName);
        Map<String, GoogleCredential> credList = credentialMap.readAllMap();
        for(Map.Entry<String, GoogleCredential> thisEntry : credList.entrySet()) {
            thisCred = thisEntry.getValue();
            if (thisCred.getAccessToken().equals(key)) {
                return thisCred;
            }
        }
        return thisCred;        
    }

    public Set<String> findAllKeys() {
        RMap<String, GoogleCredential> credentialMap = redisConn.getMap(redisName);
        return credentialMap.keySet();
    }

    public Collection<StoredCredential> getValues() {
        Collection<StoredCredential> myCollection = new ArrayList<>();
        myCollection.clear();
        RMap<String, GoogleCredential> credentialMap = redisConn.getMap(redisName);
        Map<String, GoogleCredential> credList = credentialMap.readAllMap();
        for(Map.Entry<String, GoogleCredential> thisEntry : credList.entrySet()) {
            GoogleCredential thisCredential = thisEntry.getValue();
            StoredCredential newCredential = new StoredCredential();
            newCredential.setAccessToken(thisCredential.getAccessToken());
            newCredential.setRefreshToken(thisCredential.getRefreshToken());
            newCredential.setExpirationTimeMilliseconds(thisCredential.getExpirationTimeMilliseconds());
            myCollection.add(newCredential);
        }
        return myCollection;
    }

    public Collection<GoogleCredential> findAll() {
        Collection<GoogleCredential> myCollection = new ArrayList<>();
        myCollection.clear();
        RMap<String, GoogleCredential> credentialMap = redisConn.getMap(redisName);
        Map<String, GoogleCredential> credList = credentialMap.readAllMap();
        for(Map.Entry<String, GoogleCredential> thisEntry : credList.entrySet()) {
            GoogleCredential thisCredential = thisEntry.getValue();
            myCollection.add(thisCredential);
        }
        return myCollection;
    }

    public Collection<GoogleCredential> findAllByKey(String key) {
        Collection<GoogleCredential> myCollection = new ArrayList<>();
        myCollection.clear();
        Set<String> keys = new HashSet<>();
        keys.add(key);
        RMap<String, GoogleCredential> credentialMap = redisConn.getMap(redisName);
        Map<String, GoogleCredential> credList = credentialMap.getAll(keys);
        for(Map.Entry<String, GoogleCredential> thisEntry : credList.entrySet()) {
            GoogleCredential thisCredential = thisEntry.getValue();
            myCollection.add(thisCredential);
        }
        return myCollection;
    }

    public StoredCredential get(String key) {
        RMap<String, GoogleCredential> credentialMap = redisConn.getMap(redisName);
        GoogleCredential retrCredential = credentialMap.get(key);
        StoredCredential thisCredential = new StoredCredential();
        thisCredential.setAccessToken(retrCredential.getAccessToken());
        thisCredential.setExpirationTimeMilliseconds(retrCredential.getExpirationTimeMilliseconds());
        thisCredential.setRefreshToken(retrCredential.getRefreshToken());
        return thisCredential;
    }

    public int getSize() {
        RMap<String, GoogleCredential> credentialMap = redisConn.getMap(redisName);
        return credentialMap.size();
    }

    public boolean containsKey(String key) {
        RMap<String, GoogleCredential> credentialMap = redisConn.getMap(redisName);
        return credentialMap.containsKey(key);
    }
    
}
