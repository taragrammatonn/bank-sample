package md.maib.controller;

import md.maib.entity.Customer;
import md.maib.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @MockBean
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    private Customer initCustomer() {
        // Initializing the Customer object as per your request.
        return new Customer.Builder()
                .id(1L) // ID is usually set by the database, but we mock it here
                .firstName("Alice")
                .lastName("Smith")
                .pan("1234567891011122")
                .cvv("456")
                .build();
    }

    @Test
    void testGetCustomerById() throws Exception {
        Customer savedCustomer = initCustomer();
        given(customerService.getCustomerById(anyLong())).willReturn(savedCustomer);

        mockMvc.perform(get("/customers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Alice")))
                .andExpect(jsonPath("$.lastName", is("Smith")))
                .andExpect(jsonPath("$.pan", is("1234567891011122")))
                .andExpect(jsonPath("$.cvv", is("456")));
    }

    @Test
    void testGetAllCustomers() throws Exception {
        Customer customer1 = initCustomer(); // Reusing initCustomer for simplicity
        Customer customer2 = new Customer.Builder().id(2L).firstName("Bob").lastName("Jones").pan("9876543210987654").cvv("789").build();
        given(customerService.getAllCustomers()).willReturn(List.of(customer1, customer2));

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("Alice")))
                .andExpect(jsonPath("$[1].firstName", is("Bob")));
    }

    @Test
    void testUpdateCustomerById() throws Exception {
        Customer updatedCustomer = initCustomer();
        given(customerService.updateCustomerById(anyLong(), any(Customer.class))).willReturn(updatedCustomer);

        mockMvc.perform(put("/customers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Alice\",\"lastName\":\"Smith\",\"pan\":\"1234567891011122\",\"cvv\":\"456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Alice")))
                .andExpect(jsonPath("$.lastName", is("Smith")))
                .andExpect(jsonPath("$.pan", is("1234567891011122")))
                .andExpect(jsonPath("$.cvv", is("456")));
    }

    @Test
    void testDeleteCustomerById() throws Exception {
        given(customerService.getCustomerById(anyLong())).willReturn(initCustomer());

        mockMvc.perform(delete("/customers/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
