package org.example.back;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BackApplicationTests {

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Test
    void contextLoads() {
        System.out.println("Datasource URL: " + dataSourceUrl); // Vérifie si l'URL est correcte
    }
}

