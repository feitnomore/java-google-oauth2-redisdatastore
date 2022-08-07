/* Filename: Oauth2Controller.java
 * Author: Marcelo Feitoza Parisi
 * 
 * Description: Oauth2 Controller
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
package com.github.feitnomore.googleoauth2.redisdatastore.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.github.feitnomore.googleoauth2.redisdatastore.datastore.RedisDataStoreFactory;
import com.github.feitnomore.googleoauth2.redisdatastore.repository.GoogleCredentialRepository;
import com.github.feitnomore.googleoauth2.redisdatastore.repository.GoogleCredentialRepositoryImpl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.oauth2.model.Userinfo;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;

public class Oauth2Controller {
    /* Sends requests to Google's OAuth 2.0 server. */   
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    
    private static String APP_NAME = System.getenv("APP_NAME");
    private static String oauthClientId = System.getenv("OAUTH_CLIENT_ID");
    private static String oauthClientSecret = System.getenv("OAUTH_CLIENT_SECRET");
    private static String oauthScopesFiles = System.getenv("OAUTH_SCOPES_FILE");

    public static GoogleAuthorizationCodeFlow newFlow() throws IOException {

        List<String> userScopes;

        try (Stream<String> fileScopes = Files.lines(Paths.get(oauthScopesFiles))) {
            userScopes = fileScopes.collect(Collectors.toList());
        }

        GoogleCredentialRepository repository = new GoogleCredentialRepositoryImpl();
        DataStoreFactory dataStore = new RedisDataStoreFactory(repository);

        return new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, GsonFactory.getDefaultInstance(), oauthClientId, oauthClientSecret, userScopes)
        .setDataStoreFactory(dataStore)
        .build();
      
    }
  
    public static boolean isUserLoggedIn(String sessionId) {
        try{
            Credential credential = newFlow().loadCredential(sessionId);
            if(credential.getAccessToken().equals("")) {
                return false;
            } else {
                return true;
            }
        } catch(Exception e){
            // Error getting login status
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static Userinfo getUserInfo(String sessionId) throws IOException {
        Credential credential = newFlow().loadCredential(sessionId);
        Oauth2 oauth2Client =
            new Oauth2.Builder(HTTP_TRANSPORT, GsonFactory.getDefaultInstance(), credential)
            .setApplicationName(APP_NAME)
            .build();

        Userinfo userInfo = oauth2Client.userinfo().get().execute();
        return userInfo;
    }

    public static Credential getCredential(String sessionId) throws IOException {
        return newFlow().loadCredential(sessionId);
    }

}
