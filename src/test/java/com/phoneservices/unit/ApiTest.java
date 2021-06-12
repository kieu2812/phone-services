package com.phoneservices.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoneservices.exeption.ResourceNotFoundException;
import com.phoneservices.model.Contact;
import com.phoneservices.service.ContactService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class ApiTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ContactService service;

    ObjectMapper mapper = new ObjectMapper();

    private List<Contact> createMockData(){
        List<Contact> contactList = new ArrayList<>();
        contactList.add( new Contact(1, "Alax", "Andrew", "123456"));
        contactList.add( new Contact(2, "Matt", "Lencher", "249389584"));
        return contactList;
    }
    @Test
    public void testGetEmptyContacts() throws Exception {

        List<Contact> contactList =  new ArrayList<>();

        when(service.findAll()).thenReturn(contactList);

        this.mvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }
    @Test
    public void testGetAllContacts() throws Exception {

        List<Contact> contactList =createMockData();

        when(service.findAll()).thenReturn(contactList);

        this.mvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()", is(2)))
                .andExpect(jsonPath("[0]").value(contactList.get(0)))
                .andExpect(jsonPath("[1]").value(contactList.get(1)));
    }

    @Test
    public void testGivenNameOrSurname() throws Exception{
        List<Contact> contactList =createMockData();
        Contact c1 = contactList.get(0);
        Contact c2=  contactList.get(1);
        when(service.findByGivenNameOrSurname(c1.getGivenName(), c2.getSurName())).thenReturn(new ArrayList<>());
        this.mvc.perform(get("/contacts")
                    .param("givenname", c1.getGivenName())
                    .param("surname", c2.getSurName())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()", is(0 )));
    }
    @Test
    public void testGivenNameAndSurname() throws Exception{
        List<Contact> contactList =new ArrayList<>();
        Contact c1 = new Contact(1, "Alax", "Andrew", "123456");
        contactList.add(c1);

        when(service.findByGivenNameOrSurname(c1.getGivenName(), c1.getSurName())).thenReturn(contactList);
        this.mvc.perform(get("/contacts")
                .param("givenname", c1.getGivenName())
                .param("surname", c1.getSurName())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()", is(1 )))
                .andExpect(jsonPath("[0]").value(contactList.get(0)));
    }


    @Test
    @Description("Test to validate only when Givenname is provided")
    public void testGivenNameProvided() throws Exception{
        List<Contact> contactList =new ArrayList<>();
        Contact c1= new Contact(1, "Alax", "Andrew", "123456");
        contactList.add(c1);

        when(service.findByGivenNameOrSurname(c1.getGivenName(), null)).thenReturn(contactList);
        this.mvc.perform(get("/contacts?givenname=Alax"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()", is(1)))
                .andExpect(jsonPath("[0]").value(contactList.get(0)));
    }

    @Test
    @Description("Test to validate only when Givenname is provided")
    public void testSurNameProvided() throws Exception{
        List<Contact> contactList =new ArrayList<>();
        Contact c1= new Contact(1, "Alax", "Andrew", "123456");
        contactList.add(c1);

        when(service.findByGivenNameOrSurname(null, c1.getSurName())).thenReturn(contactList);
        this.mvc.perform(get("/contacts?surname=" + c1.getSurName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()", is(1)))
                .andExpect(jsonPath("[0]").value(contactList.get(0)));
    }
    @Test
    public void testCreateContact() throws Exception{
        Contact c1= new Contact(1, "Alax", "Andrew", "123456");

        when(service.save(c1)).thenReturn(c1);
        this.mvc.perform(post("/contact")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(mapper.writeValueAsString(c1))

            )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(c1.getId())))
                .andExpect(jsonPath("$.givenName", is(c1.getGivenName())))
                .andExpect(jsonPath("$.surName", is(c1.getSurName())))
                .andExpect(jsonPath("$.phoneNumber", is(c1.getPhoneNumber())));
    }


    @Test
    public void testGetContactById() throws Exception{
        Contact c1= new Contact(1, "Alax", "Andrew", "123456");

        when(service.findById(c1.getId())).thenReturn(c1);
        this.mvc.perform(get("/contact/{id}", c1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(c1.getId())))
                .andExpect(jsonPath("$.givenName", is(c1.getGivenName())))
                .andExpect(jsonPath("$.surName", is(c1.getSurName())))
                .andExpect(jsonPath("$.phoneNumber", is(c1.getPhoneNumber())));
    }

    @Test
    public void testGetContactByNotExistingId() throws Exception{

        when(service.findById(2)).thenThrow( new ResourceNotFoundException("Contact does not found") );
        this.mvc.perform(get("/contact/{id}", 2))
                .andExpect(status().isNotFound());

    }


    @Test
    public void testDeleteContactByNotExistingId() throws Exception{
        when(service.findById(2)).thenThrow( new ResourceNotFoundException("Contact does not found") );
        this.mvc.perform(delete("/contact/{id}", 2))
                .andExpect(status().isNotFound());

    }
    @Test
    public void testDeleteContactById() throws Exception{
        Contact c1= new Contact(1, "Alax", "Andrew", "123456");
        when(service.findById(c1.getId())).thenReturn(c1);
        doNothing().when(service).deleteById(c1.getId());
        this.mvc.perform(delete("/contact/{id}", c1.getId()))
                .andExpect(status().isOk());

    }
}
