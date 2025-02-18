package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.UserServices;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class pageController {

    @Autowired
    private UserServices userServices;

    @RequestMapping("/")
    public String index() {
        return ("redirect:/home");
    }

    // home route
    @RequestMapping("/home")
    public String home(Model model) {
        System.out.println("This is home page");
//sending data to view in html
        // model.addAttribute("name","Sudhansu");
        // model.addAttribute("youtubeChannel", "https://www.youtube.com/@SUDHANSU321");
        // model.addAttribute("Linkedin", "https://www.linkedin.com/in/sudhansu-barick-ba3221231/");
        return "home";
    }

// about route
    @RequestMapping("/about")
    public String aboutPage(Model model) {
        model.addAttribute("isLogin", "false");
        System.out.println("This is about page");
        return "about";
    }

//services route
    @RequestMapping("/services")
    public String servicesPage() {
        System.out.println("This is services page");
        return "services";
    }

//contact route
    @RequestMapping("/contact")
    public String contactPage() {
        System.out.println("This is contact page");
        return "contact";
    }

//login route 
    @GetMapping("/login")
    public String login() {

        return "login";
    }

//signup route
    @GetMapping("/signup")
    public String signup(Model model) {
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);

        return "signup";

    }

// processing signup data
    @RequestMapping(value = "/do-signup", method = RequestMethod.POST)
    public String signupProcess(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult, HttpSession session) { // @modelAttriibute values are fetch in the userform object parameter
        System.out.println("Sign up processing");
//To fetch form data , so we create UserForm
        System.out.println(userForm);

//BindingResult class checks errors
// validate form data help of User form class
        if (rBindingResult.hasErrors()) {
            return "signup";
        }

// save to database using  user service
// convert UserForm --> user
// User user = User.builder()
// .name(userForm.getName())
// .email(userForm.getEmail())
// .password(userForm.getPassword())
// .phoneNumber(userForm.getPhoneNumber())
// .about(userForm.getAbout())
// .profilePic("https://commons.wikimedia.org/wiki/File:Windows_10_Default_Profile_Picture.svg")
// .build();
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setAbout(userForm.getAbout());
        user.setPassword(userForm.getPassword());
// This enable the user.if it false then it will give you a varification link then after the user can login
        user.setEnabled(true);
        user.setProfilePic("https://commons.wikimedia.org/wiki/File:Windows_10_Default_Profile_Picture.svg");

        User saveUser = userServices.saveUser(user);
        System.out.println("User saved ...");

// messagehandle = "Registration successfull"
        Message message = Message.builder().content("Registration Successful").type(MessageType.green).build();
        session.setAttribute("message", message);

// redirect to login
        return "redirect:/signup";
    }
}
