package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.repo.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler  {

  Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

  @Autowired
  UserRepo userRepo;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    logger.info("OAuthAuthenticationSuccessHandler");

    // identify the provider

    var oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
    String authorizedClientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

    logger.info(authorizedClientRegistrationId);

    var oauthUser = (DefaultOAuth2User) authentication.getPrincipal();
    oauthUser.getAttributes().forEach((key, value) -> {
      logger.info(key + " : " + value);
    });

    User user = new User();

    user.setUserId(UUID.randomUUID().toString());
    user.setEmailVerified(true);
    user.setRoleList(List.of(AppConstants.ROLE_USER));
    user.setEnabled(true);
    user.setPassword("Dummy");

    if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {
      // google
      // google attribute
      user.setEmail(oauthUser.getAttribute("email").toString());
      user.setProfilePic(oauthUser.getAttribute("picture").toString());
      user.setProviderUserId(oauthUser.getName());
      user.setName(oauthUser.getAttribute("name").toString());
      user.setAbout("This Account is created using Google");
      user.setProvider(Providers.GOOGLE);
      


    } else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {
      // github
      // github attribute

      // in this case if email not found then create username as email in github
      String email = oauthUser.getAttribute("email") != null ? 
                      oauthUser.getAttribute("email").toString() : oauthUser.getAttribute("login").toString()+"@github.com";

      String picture = oauthUser.getAttribute("avatar_url").toString();
      String name = oauthUser.getAttribute("login").toString();
      String providerUserId = oauthUser.getName();

      user.setEmail(email);
      user.setProfilePic(picture);
      user.setProviderUserId(providerUserId);
      user.setName(name);
      user.setProvider(Providers.GITHUB);
      user.setAbout("This Account is created by Github");


    } else if (authorizedClientRegistrationId.equalsIgnoreCase("linkedin")) {

      // linkedin

    } else {

      logger.info("No provider found");
    }


    

    // logger.info(user.getName());
    // user.getAttributes().forEach((key,value)->{
    // logger.info("{}=>{}",key,value);
    // });
    // logger.info(user.getAuthorities().toString());

    // save the data to database
    /*
     * DefaultOAuth2User user =(DefaultOAuth2User)authentication.getPrincipal();
     * String email = user.getAttribute("email").toString();
     * String name = user.getAttribute("name").toString();
     * String picture = user.getAttribute("picture").toString();
     * 
     * //create user and set to database
     * User user1 = new User();
     * 
     * user1.setEmail(email);
     * user1.setName(name);
     * user1.setPassword("password");
     * user1.setProfilePic(picture);
     * user1.setUserId(UUID.randomUUID().toString());
     * user1.setProvider(Providers.GOOGLE);
     * user1.setEnabled(true);
     * user1.setEmailVerified(true);
     * user1.setProviderUserId(user.getName());
     * user1.setRoleList(List.of(AppConstants.ROLE_USER));
     * user1.setAbout("This account is created using Google");
     * 
     * 
     * User user2 = userRepo.findByEmail(email).orElse(null);
     * 
     * if(user2==null){
     * userRepo.save(user1);
     * logger.info("user saved :- " + email);
     * }
     */


     //save the user.
      User user2 = userRepo.findByEmail(user.getEmail()).orElse(null);
     
      if(user2==null){
      userRepo.save(user);
      logger.info("user saved " );
    }
    new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");

 

}}
