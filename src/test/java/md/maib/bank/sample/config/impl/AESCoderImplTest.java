package md.maib.bank.sample.config.impl;

import jakarta.websocket.EncodeException;
import md.maib.bank.sample.mother.AbstractContainerBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AESCoderImplTest extends AbstractContainerBaseTest {

   private final AESCoderImpl securityService;

        @Autowired
        AESCoderImplTest(AESCoderImpl securityService) {
            this.securityService = securityService;
        }

        @Test
        void shouldEncodePan() throws EncodeException {
            var pan = "1234567890123456";
            var encodedPan = securityService.encode(pan);
            assertNotNull(encodedPan, "Pan should be encoded");
        }
}