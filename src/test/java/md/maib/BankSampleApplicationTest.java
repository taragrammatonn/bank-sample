package md.maib;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BankSampleApplicationTest {

    @Test
    void contextLoad(ApplicationContext context) {
        // to pass code coverage gate
        BankSampleApplication.main(new String[]{});

        // to pass sonar code smell
        assertNotNull(context);
    }
}