package com.phoneservices.intergration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoneservices.model.Contact;
import com.phoneservices.repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MockRepoTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ContactRepository contactRepository;

    ObjectMapper mapper = new ObjectMapper();


    private List<Contact> createMockData(){
        List<Contact> contactList = new ArrayList<>();
        contactList.add( new Contact(1, "Alax", "Andrew", "123456"));
        contactList.add( new Contact(2, "Matt", "Lencher", "249389584"));
        return contactList;
    }
    @Test
    public void testGetAllContact() throws Exception {
        List<Contact> contactList =createMockData();
        when(contactRepository.findAll()).thenReturn(contactList);
        this.mvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(2))
                .andExpect(jsonPath("[0]").value(contactList.get(0)))
                .andExpect(jsonPath("[1]").value(contactList.get(1)));
    }

    @Test
    public void testGetAllContactByGivenName() throws Exception {
        List<Contact> contactList =createMockData();

        when(contactRepository.findAll()).thenReturn(contactList);
        this.mvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(2))
                .andExpect(jsonPath("[0]").value(contactList.get(0)))
                .andExpect(jsonPath("[1]").value(contactList.get(1)));
    }

    @Test
    public void testGetContactById() throws Exception {
        Contact c1 = new  Contact(1, "Alax", "Andrew", "123456");
        when(contactRepository.findById(c1.getId())).thenReturn(Optional.of(c1));
        this.mvc.perform(get("/contact/{id}", c1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(c1.getId())))
                .andExpect(jsonPath("$.givenName", is(c1.getGivenName())))
                .andExpect(jsonPath("$.surName", is(c1.getSurName())))
                .andExpect(jsonPath("$.phoneNumber", is(c1.getPhoneNumber())));
    }


    @Test
    public void testDeleteContactById() throws Exception {
        Contact c1 = new  Contact(1, "Alax", "Andrew", "123456");
        when(contactRepository.findById(c1.getId())).thenReturn(Optional.of(c1));
        doNothing().when(contactRepository).deleteById(c1.getId());
        this.mvc.perform(delete("/contact/{id}", c1.getId()) )
                .andExpect(status().isOk());
    }


}
