package com.example.bookstore;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "springdoc.swagger-ui.enabled=false",
    "springdoc.api-docs.enabled=false"
})
class BookstoreApplicationTests {

    @Test
    void contextLoads() {
    }

}
