package com.phoneservices.unit;


import com.phoneservices.exeption.ResourceNotFoundException;
import com.phoneservices.model.Contact;
import com.phoneservices.repository.ContactRepository;
import com.phoneservices.service.ContactService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @Mock
    ContactRepository repository;

    @InjectMocks
    ContactService service;

    private List<Contact> createMockData(){
        List<Contact> contactList = new ArrayList<>();
        contactList.add( new Contact(1, "Alax", "Andrew", "123456"));
        contactList.add( new Contact(2, "Matt", "Lencher", "249389584"));
        return contactList;
    }

    @Test
    public void testFindAllWithEmptyContact(){
        List<Contact> contactList =  new ArrayList<>();
        when(repository.findAll()).thenReturn(contactList);
        List<Contact> contacts = service.findAll();
        assertEquals(contacts, contactList);
    }
    @Test
    public void testFindAllContact(){
        List<Contact> contactList =createMockData();

        when(repository.findAll()).thenReturn(contactList);
        List<Contact> contacts = service.findAll();
        assertEquals(contacts, contactList);
    }

    @Test
    public void testFindAllContactByGivenName(){
        List<Contact> contactList =createMockData();

        when(repository.findByGivenName(contactList.get(0).getGivenName())).thenReturn(contactList);
        List<Contact> contacts = service.findByGivenNameOrSurname(contactList.get(0).getGivenName(), null);
        assertEquals(contacts, contactList);
    }


    @Test
    public void testFindAllContactBySurname(){
        List<Contact> contactList =createMockData();

        when(repository.findBySurname(contactList.get(0).getSurName())).thenReturn(contactList);
        List<Contact> contacts = service.findByGivenNameOrSurname(null, contactList.get(0).getSurName());
        assertEquals(contacts, contactList);
    }

    @Test
    public void testFindAllContactByGivennameAndSurname(){
        List<Contact> contactList =createMockData();

        when(repository.findByGivenNameAndSurname(contactList.get(0).getGivenName(), contactList.get(0).getSurName())).thenReturn(contactList);
        List<Contact> contacts = service.findByGivenNameOrSurname(contactList.get(0).getGivenName(), contactList.get(0).getSurName());
        assertEquals(contacts, contactList);
    }

    @Test
    public void testCreateContact(){
        Contact c1 =new Contact(1, "Alax", "Andrew", "123456");
        when(repository.save(c1)).thenReturn(c1);
        Contact contact = service.save(c1);
        assertEquals(contact,c1);
    }

    @Test
    public void testGetContactById() throws ResourceNotFoundException {
        Contact c1 =new Contact(1, "Alax", "Andrew", "123456");
        when(repository.findById(c1.getId())).thenReturn(Optional.of(c1));
        Contact contact = service.findById(c1.getId());
        assertEquals(contact,c1);
    }

    @Test
    public void testDeleteContactById() throws ResourceNotFoundException {
        doNothing().when(repository).deleteById(2);
        service.deleteById(2);

    }


}
