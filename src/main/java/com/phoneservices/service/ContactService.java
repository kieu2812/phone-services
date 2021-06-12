package com.phoneservices.service;

import com.phoneservices.model.Contact;
import com.phoneservices.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContactService {


    ContactRepository repository;

    public ContactService(ContactRepository repository) {
        this.repository = repository;
    }

    public List<Contact> findAll() {
        return repository.findAll();
    }

    public List<Contact> findByGivenNameOrSurname(String giveName, String surname) {
        return repository.findByGivenNameOrSurname(giveName, surname);
    }
}
