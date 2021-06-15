package com.phoneservices.controller;

import com.phoneservices.exeption.ResourceNotFoundException;
import com.phoneservices.model.Contact;
import com.phoneservices.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    }

    @PostMapping("/contact")
    public Contact createContact(@RequestBody Contact contact){
        return  service.save(contact);
    }

    @GetMapping("/contact/{id}")
    public Contact getById(@PathVariable int id) throws Exception {
        return service.findById(id);
    }

    @DeleteMapping("/contact/{id}")
    public void deleteById(@PathVariable int id) throws Exception {
        try {
            Contact contact = service.findById(id);
            service.deleteById(id);
        }catch(Exception e){

            throw new ResourceNotFoundException("Contact does not found");

        }

    }

    @PutMapping("/contact/{id}")
    public Contact updateContact(@RequestBody Contact contact, @PathVariable int id) throws ResourceNotFoundException {
        Contact contactInDb =  this.service.findById(id);
        contact.setId(contactInDb.getId());
        service.save(contact);
        return contact;
    }

    @PatchMapping("/contact/{id}")
    public Contact updatePartOfContact(@RequestBody Contact contact, @PathVariable int id) throws ResourceNotFoundException {
        Contact contactInDb =  this.service.findById(id);
        if(contact.getGivenName()!=null)
            contactInDb.setGivenName(contact.getGivenName());
        if(contact.getSurName()!=null)
            contactInDb.setSurName(contact.getSurName());
        if(contact.getPhoneNumber()!=null)
            contactInDb.setPhoneNumber(contact.getPhoneNumber());

        service.save(contactInDb);
        return contactInDb;
    }
}
