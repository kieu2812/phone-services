package com.phoneservices.controller;

import com.phoneservices.model.Contact;
import com.phoneservices.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ContractController {

    ContactService service;

    public ContractController(  ContactService service){
        this.service = service;
    }

    @GetMapping("/contacts")
    public List<Contact> findByGivenNameOrSurName(@RequestParam(required = false) String givenname, @RequestParam(required = false) String surname){
       if(givenname==null && surname==null )
           return service.findAll();
       return  service.findByGivenNameOrSurname(givenname, surname);
//        if(givenname != null || surname !=null) {
//            return service.findByGivenNameOrSurname(givenname, surname);
//        } else {
//            return service.findAll();
//        }
    }
}
