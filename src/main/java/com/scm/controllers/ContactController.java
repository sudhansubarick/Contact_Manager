package com.scm.controllers;


import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helpers.AppConstants;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.ContactService;
import com.scm.services.UserServices;
import com.scm.services.imageService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private imageService imageService;
  

    @Autowired
    private UserServices userServices;

   Logger logger = LoggerFactory.getLogger(ContactController.class);

    @RequestMapping("/add")
    //add contact page : handler
public String addContactView(Model model){

    ContactForm contactForm = new ContactForm();
    model.addAttribute("contactForm",contactForm);

    return "user/add_contacts";

}

@RequestMapping(value="/add",method=RequestMethod.POST)
//@valid use for to falidate the contactForm
//authenticate use to get user
public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult bindingResult , Authentication authentication,HttpSession session){
System.out.println(contactForm);

//process the form data

//validate form  @valid and bindingresult check errors
if(bindingResult.hasErrors()){
    //message handle first way
    session.setAttribute("message", Message.builder()
    .content("please correct the following errors")
    .type(MessageType.red)
    .build());
   

    return "user/add_contacts";
}


String userName = Helper.getEmailOfLoggedInUser(authentication);
//convert contactForm to contact
User user= userServices.getUserByEmail(userName);
//process contact image



Contact contact = new Contact();

contact.setName(contactForm.getName());
contact.setEmail(contactForm.getEmail());
contact.setAddress(contactForm.getAddress());
contact.setPhoneNumber(contactForm.getPhoneNumber());
contact.setDescription(contactForm.getDescription());
contact.setWebsiteLink(contactForm.getWebsiteLink());
contact.setLinkedInLink(contactForm.getLinkedInLink());
contact.setFavorite(contactForm.isFavorite());
contact.setUser(user);

if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()){
    String fileName = UUID.randomUUID().toString();
    String fileURL = imageService.uploadImage(contactForm.getContactImage(),fileName);

    contact.setPicture(fileURL);
    contact.setCloudinaryImagePublicId(fileName);
}

contactService.saveContact(contact);

System.out.println("contact saved ...");
System.out.println(contactForm);

//message handle 2nd way
Message message = Message.builder().content("contact Saved ").type(MessageType.green).build();
session.setAttribute("message", message);


    return "redirect:/user/contacts/add";
}


@RequestMapping
public String viewContacts(
                            @RequestParam(value="page", defaultValue="0")int page,
                            @RequestParam(value="size", defaultValue=AppConstants.PAGE_SIZE + "")int size,
                            @RequestParam(value="sortBy", defaultValue="name")String sortBy,
                            @RequestParam(value="direction", defaultValue="asc")String direction, 
                            Model model,Authentication authentication){   


    //load all the contact of an user


    //fetch the loggedin user from helper 
    String userName =  Helper.getEmailOfLoggedInUser(authentication);
    //then get the user from userService
    User user = userServices.getUserByEmail(userName);
    //fetch the contacts from the user
    Page<Contact> pageContact  =  contactService.getByUser(user,page,size,sortBy,direction);

    model.addAttribute("pageContact",pageContact);
    model.addAttribute("pageSize",AppConstants.PAGE_SIZE);

    model.addAttribute("contactSearchForm",new ContactSearchForm());


    return "user/contacts";

}

@RequestMapping("/search")
public String searchHandler(@ModelAttribute ContactSearchForm contactSearchForm,
                            @RequestParam(value="size", defaultValue=AppConstants.PAGE_SIZE + "")int size,
                            @RequestParam(value="page", defaultValue="0")int page, 
                            @RequestParam(value="sortBy", defaultValue="name")String sortBy,
                            @RequestParam(value="direction", defaultValue="asc")String direction,
                            Model model,Authentication authentication ){
                                                           
logger.info("field {} keyword{}",contactSearchForm.getField(),contactSearchForm.getValue());

var user = userServices.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));
 
Page<Contact> pageContact = null;
if(contactSearchForm.getField().equalsIgnoreCase("name")){
    pageContact = contactService.searchByName(contactSearchForm.getValue(), size,page,  sortBy, direction,user);
}else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
    pageContact = contactService.searchByEmail(contactSearchForm.getValue(),  size,page, sortBy, direction ,user);    
}else if (contactSearchForm.getField().equalsIgnoreCase("phone")) {
    pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), size,page,  sortBy, direction,user);    
}

logger.info("pageContact{}", pageContact);

model.addAttribute("contactSeachForm", contactSearchForm);

model.addAttribute("pageContact",pageContact);
model.addAttribute("pageSize",AppConstants.PAGE_SIZE);

    return "user/search";
}


//delete contact
@RequestMapping("/delete/{contactId}") //By the help of  @pathvariable, use the requestMapping parameter to the method parameter like contactId
public String deleteContact(@PathVariable("contactId") String contactId,HttpSession session){

contactService.deleteContact(contactId);
logger.info( "contactId {} deleted",contactId);

session.setAttribute("message",
                     Message.builder()
                    .content("Contact Is Deleted Successfully !!")
                    .type(MessageType.green)
                    .build());

    return "redirect:/user/contacts";
}

//update contact view page
@RequestMapping("/view/{contactId}")
public String updateContactFormView(@PathVariable("contactId") String contactId,Model model){

    // here i get contact from contact id from contactService and then convert contact to contactform 
    // then write the contact form to the model so we can access in the view.
   var contact= contactService.getById(contactId);

   ContactForm contactForm = new ContactForm();
   contactForm.setName(contact.getName());
   contactForm.setEmail(contact.getEmail());
   contactForm.setAddress(contact.getAddress());
   contactForm.setDescription(contact.getDescription());
   contactForm.setPhoneNumber(contact.getPhoneNumber());
   contactForm.setLinkedInLink(contact.getLinkedInLink());
   contactForm.setWebsiteLink(contact.getWebsiteLink());
   contactForm.setFavorite(contact.isFavorite());
   contactForm.setPicture(contact.getPicture());

   model.addAttribute("contactForm", contactForm);
   model.addAttribute("contactId",contactId);


    return "user/update_contact_view";
}

//update contact
@RequestMapping(value="/update/{contactId}",method=RequestMethod.POST)
public String updateContact(@PathVariable("contactId") String contactId,
                            @Valid @ModelAttribute ContactForm contactForm,BindingResult result, Model model,HttpSession session){


    
    if(result.hasErrors()){
        return  "user/update_contact_view";
    }
    var con= contactService.getById(contactId);

    con.setId(contactId);
    con.setName(contactForm.getName());
    con.setEmail(contactForm.getEmail());
    con.setAddress(contactForm.getAddress());
    con.setDescription(contactForm.getDescription());
    con.setPhoneNumber(contactForm.getPhoneNumber());
    con.setFavorite(contactForm.isFavorite());
    con.setLinkedInLink(contactForm.getLinkedInLink());
    con.setWebsiteLink(contactForm.getWebsiteLink());
    

    //Image Process
    if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()){
    
    
        String fileName =UUID.randomUUID().toString();
        String imageUrl= imageService.uploadImage(contactForm.getContactImage(), fileName);
        con.setCloudinaryImagePublicId(fileName);
        con.setPicture(imageUrl);
        contactForm.setPicture(imageUrl);
    }
    //End Image Process

   var updatedContact= contactService.updateContact(con);
   logger.info("Contact Updated {}",updatedContact);

   Message message = Message.builder().content("Contact Updated ").type(MessageType.green).build();
    session.setAttribute("message", message);
   //model.addAttribute("message",Message.builder().content("contact Updated").type(MessageType.green));

    return "redirect:/user/contacts/view/"+contactId;
}

}
