package com.scm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class pageController {
    // home route
@RequestMapping("/home")
public String home(Model model){
    System.out.println("This is home page");
//sending data to view in html
    model.addAttribute("name","Sudhansu");
    model.addAttribute("youtubeChannel", "https://www.youtube.com/@SUDHANSU321");
    model.addAttribute("Linkedin", "https://www.linkedin.com/in/sudhansu-barick-ba3221231/");
    return "home";
}

// about route
@RequestMapping("/about")
public String aboutPage(Model model){
    model.addAttribute("isLogin","false");
    System.out.println("This is about page");
    return "about";
}

//services route
@RequestMapping("/services")
public String servicesPage(){
    System.out.println("This is services page");
    return "services";
}

}
