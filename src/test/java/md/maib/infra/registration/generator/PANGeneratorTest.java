package md.maib.infra.registration.generator;

import md.maib.bank.sample.mother.AbstractContainerBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class PANGeneratorTest extends AbstractContainerBaseTest {

    private final PANGenerator panGenerator;

    @Autowired
    PANGeneratorTest(PANGenerator panGenerator) {
        this.panGenerator = panGenerator;
    }

    @Test
    void shouldGeneratePAN() {
        var pan = panGenerator.generatePAN();
        assertNotNull(pan, "PAN should not be null");
        assertEquals(16, pan.length(), "PAN should have 16 digits");
    }

}