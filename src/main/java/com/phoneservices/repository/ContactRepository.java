package com.phoneservices.repository;

import com.phoneservices.model.Contact;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContactRepository extends CrudRepository<Contact, Integer> {
    List<Contact> findAll();

    @Query("Select c from Contact c Where c.givenName = ?1 Or c.surName = ?2")
    List<Contact> findByGivenNameOrSurname(String giveName, String surname);
}
