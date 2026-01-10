package com.example.bookstore.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("BookstoreController Tests")
class BookstoreControllerTest {

    private MockMvc mockMvc;
    private BookstoreController controller;

    @Test
    @DisplayName("Should return home endpoint response")
    void shouldReturnHomeEndpointResponse() throws Exception {
        controller = new BookstoreController();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Welcome to Bookstore API"))
            .andExpect(jsonPath("$.status").value("running"))
            .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Should return health check response")
    void shouldReturnHealthCheckResponse() throws Exception {
        controller = new BookstoreController();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        mockMvc.perform(get("/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"));
    }
}
