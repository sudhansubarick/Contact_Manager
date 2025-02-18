package com.scm.helpers;


import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;


public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication){

    

if(authentication instanceof OAuth2AuthenticationToken){
    var aOAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
    var clientId = aOAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

    var oAuth2User = (OAuth2User)authentication.getPrincipal();
    String userName = "";
//Google 
if(clientId.equalsIgnoreCase("google")){
    System.out.println("Getting Email form google");

    userName = oAuth2User.getAttribute("email").toString();

    //github
}else if(clientId.equalsIgnoreCase("github")){
    System.out.println("Getting Email form Github");

    userName = oAuth2User.getAttribute("email") != null ? 
    oAuth2User.getAttribute("email").toString() : oAuth2User.getAttribute("login").toString()+"@github.com";

}
return userName;

}else{
    System.out.println("Getting Email form local database");
    return authentication.getName();
       
    }
    
    } 

    //Email service Link

    public static String getLinkForEmailVerification(String emailToken ){

        String link = "http://localhost:8080/auth/verify-email?token=" + emailToken;

        return link;
    }
}
