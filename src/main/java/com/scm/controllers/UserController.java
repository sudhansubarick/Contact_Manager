package com.scm.controllers;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.UserServices;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {
  private final  Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private UserServices userServices;

  
    //user dashboard page
    @RequestMapping(value = "/dashboard" )
    public String userDashboard(){

        return "user/dashboard";
    }

    // user profile page
    @RequestMapping(value = "/profile" )
//Model use for to show the data in html page
    public String userProfile(Model model,Authentication authentication ){
      
        return "user/profile";
    }
    
    //Todo
    
    //delete user


    //update user view
    @RequestMapping("/view/")
    public String updateUserView(Model model,Authentication authentication){

         if(authentication == null){
            return null  ;
        }

        
        String userName = Helper.getEmailOfLoggedInUser(authentication);



        User user = userServices.getUserByEmail(userName);
       

        UserForm userForm = new UserForm();
        userForm.setName(user.getName());
        userForm.setEmail(user.getEmail());
        userForm.setPassword(user.getPassword());
        userForm.setPhoneNumber(user.getPhoneNumber());
        userForm.setAbout(user.getAbout());

        model.addAttribute(userForm);
        model.addAttribute("userName"+user);

        return "user/update_user_view";
    }

    //update user
    @RequestMapping(value="/update/",method=RequestMethod.POST)
    public String updateUserView(@Valid @ModelAttribute UserForm userForm,
                             BindingResult result,Model model,HttpSession session,Authentication authentication){

        if(result.hasErrors()){
            return "user/update_user_view";
        }

        if(authentication == null){
            return null  ;
        }

        
        String userName = Helper.getEmailOfLoggedInUser(authentication);

        User users = userServices.getUserByEmail(userName);

        users.setUserId(userName);
        users.setName(userForm.getName());
        users.setEmail(userForm.getEmail());
        users.setPassword(userForm.getPassword());
        users.setPhoneNumber(userForm.getPhoneNumber());
        users.setAbout(userForm.getAbout());

        var updateUser = userServices.updateUser(users);
        logger.info(userName);

        logger.info("User updated {}",updateUser);
        Message message= Message.builder().content("user Updated").type(MessageType.green).build();
        session.setAttribute("message", message);
      


        return "redirect:user/view/"+userName;
    }
    
   
   

}
