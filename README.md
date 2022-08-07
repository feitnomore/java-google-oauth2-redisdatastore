# java-google-oauth2-redisdatastore


**Maintainers:** [feitnomore](https://github.com/feitnomore/)

This is a simple hack to be able to store Google Oauth2 Credential information on [Redis](https://redis.io/) using Java. This was developed to provide failover and scalability for an application running on top of containers. The library used is [Redisson](https://github.com/redisson/redisson).

*WARNING:* Use it at your own risk.

## INTRODUCTION

This was used in a plain Servlet application, with no fancy libs.

* Environment Variables
```sh
    export APP_NAME="my-app-name"
    export OAUTH_CLIENT_ID="my-oauth-client-id"
    export OAUTH_CLIENT_SECRET="my-oauth-client-secret"
    export OAUTH_SCOPES_FILE="scopes.txt"
    export REDIS_HOST="XXX.XXX.XXX.XXX"
    export REDIS_PORT="6379"
    export REDIS_NAME="oauth2"
```
  
* scopes.txt
```
https://www.googleapis.com/auth/drive
https://www.googleapis.com/auth/documents
```
  
* DoLogin.java

```java
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet;
import com.google.api.client.http.GenericUrl;
import com.github.feitnomore.googleoauth2.redisdatastore.controllers.Oauth2Controller;

public class DoLogin extends AbstractAuthorizationCodeServlet {

    @Override
    protected String getUserId(HttpServletRequest request) {
      return request.getSession().getId();
    }
    
    @Override
    protected AuthorizationCodeFlow initializeFlow() throws IOException {
      return Oauth2Controller.newFlow();
    }
  
    @Override
    protected String getRedirectUri(HttpServletRequest request) {
      GenericUrl url = new GenericUrl(request.getRequestURL().toString());
      url.setRawPath("/doLoginCallback");
      return url.build();
    }
}
```
  
* DoLoginCallback.java

```java
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet;
import com.google.api.services.oauth2.model.Userinfo;
import com.google.api.client.http.GenericUrl;
import com.github.feitnomore.googleoauth2.redisdatastore.controllers.Oauth2Controller;

public class DoLoginCallback extends AbstractAuthorizationCodeCallbackServlet {

    @Override
    protected AuthorizationCodeFlow initializeFlow() throws IOException {
      return Oauth2Controller.newFlow();
    }
  
    @Override
    protected String getRedirectUri(HttpServletRequest request) {
      GenericUrl url = new GenericUrl(request.getRequestURL().toString());
      url.setRawPath("/doLoginCallback");
      return url.build();
    }
  
    @Override
    protected String getUserId(HttpServletRequest request) {
      return request.getSession().getId();
    }
  
    @Override
    protected void onSuccess(HttpServletRequest request, HttpServletResponse response, Credential credential)
        throws IOException {
          String sessionId = request.getSession().getId();
          Userinfo userInfo = Oauth2Controller.getUserInfo(sessionId);
          if(userInfo.getName().length() > 0) {
            response.sendRedirect("/success");
          } else {
            response.sendRedirect("/needs-login");
          }
    }
  
    @Override
    protected void onError(
        HttpServletRequest request, HttpServletResponse response, AuthorizationCodeResponseUrl errorResponse)
        throws IOException {
      response.getWriter().print("Login cancelled.");
    }
}
```
  
* DoLogout.java

```java
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DoLogout extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    request.getSession().invalidate();
    response.sendRedirect("/needs-login");
  }

}
```
