package com.scm.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.scm.entities.Contact;
import com.scm.entities.User;

public interface ContactService {

    //save contact
    Contact saveContact(Contact contact);

    //update contact
    Contact updateContact(Contact contact);

    //delete contact
    void deleteContact(String id);

    //getContacts
    List<Contact> getAll();

    //getContact byid
    Contact getById(String id);

    //search contact
    Page<Contact>searchByName(String nameKeyword,int size,int page,String sortBy,String order,User user);

    Page<Contact>searchByEmail(String emailKeyword,int size,int page,String sortBy,String order,User user);

    Page<Contact>searchByPhoneNumber(String phoneNumberKeyword,int size,int page,String sortBy,String order,User user);

    //getContact by userId
    List<Contact>getByUserId(String userId);

    //get user
    Page<Contact>getByUser(User user,int page,int size,String sortField,String sortDirection);
    

}
