package md.maib.infra.registration.generator;

import md.maib.bank.sample.mother.AbstractContainerBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class CVVGeneratorTest extends AbstractContainerBaseTest {

    private final CVVGenerator cvvGenerator;

    @Autowired
    CVVGeneratorTest(CVVGenerator cvvGenerator) {
        this.cvvGenerator = cvvGenerator;
    }


    @Test
    void shouldGenerateCVV() {
        var cvv = cvvGenerator.generateCVV();
        assertNotNull(cvv, "CVV should not be null");
        assertEquals(3, cvv.length(), "CVV should have 3 digits");
    }

}