package com.phoneservices.intergration;

import com.phoneservices.model.Contact;
import com.phoneservices.repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MockRepoTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ContactRepository contactRepository;

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
}
