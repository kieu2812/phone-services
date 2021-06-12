package com.phoneservices.controller;

import com.phoneservices.model.Contact;
import com.phoneservices.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ContractController {

    ContactService service;

    public ContractController(  ContactService service){
        this.service = service;
    }


    @GetMapping("/contacts")
    public List<Contact> getContacts(){
        return service.findAll();
    }
}
