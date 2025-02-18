package com.scm.controllers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.UserServices;



@ControllerAdvice
public class RootController {
 
    @Autowired
    private UserServices userServices;

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    //ModelAttribute use for to show the data all the pages.
    @ModelAttribute
    public void addLoggedInUserInformation(Model model,Authentication authentication){
        if(authentication == null){
            return ;
        }

        System.out.println("Adding Logged in User data to the model"); 
        String userName = Helper.getEmailOfLoggedInUser(authentication);

        logger.info("User logged in {}" , userName);
        User user=  userServices.getUserByEmail(userName);

       
       logger.info("User Name :- {}",user.getName());
       logger.info("Email :-{}",user.getEmail());
       logger.info("About :-{}",user.getAbout());
       model.addAttribute("loggedInUser",user);

    }


}
