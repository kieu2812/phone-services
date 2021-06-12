package com.phoneservices.unit;

import com.phoneservices.model.Contact;
import com.phoneservices.service.ContactService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class ApiTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ContactService service;

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

        List<Contact> contactList =  new ArrayList<>();
        contactList.add( new Contact(1, "Alax", "Andrew", "123456"));
        contactList.add( new Contact(2, "Matt", "Lencher", "249389584"));

        when(service.findAll()).thenReturn(contactList);

        this.mvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()", is(2)))
                .andExpect(jsonPath("[0]").value(contactList.get(0)))
                .andExpect(jsonPath("[1]").value(contactList.get(1)));
    }

}
