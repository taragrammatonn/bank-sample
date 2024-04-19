package md.maib.bank.config;

import jakarta.websocket.EncodeException;
import md.maib.bank.config.impl.AESCoderImpl;
import md.maib.bank.mother.AbstractContainerBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecurityServiceTest extends AbstractContainerBaseTest {

    private final AESCoderImpl securityService;

    @Autowired
    SecurityServiceTest(AESCoderImpl securityService) {
        this.securityService = securityService;
    }

    @Test
    void shouldEncodePan() throws EncodeException {
        var pan = "1234567890123456";
        var encodedPan = securityService.encode(pan);
        assertNotNull(encodedPan, "Pan should be encoded");
    }
}