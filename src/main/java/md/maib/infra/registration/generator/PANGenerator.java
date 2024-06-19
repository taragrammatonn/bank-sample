package md.maib.infra.registration.generator;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.stream.Collectors;

@Component
public class PANGenerator {

    private static final String BANK_IDENTIFIER = "4562";
    private static final int PAN_LENGTH = 16;
    private static final int RANDOM_DIGITS_LENGTH = 10;

    public String generatePAN() {
        String uniqueNumber = String.valueOf(Instant.now().getEpochSecond());
        String randomDigits = generateRandomDigits(RANDOM_DIGITS_LENGTH);
        String pan = BANK_IDENTIFIER + uniqueNumber + randomDigits;

        return pan.substring(0, PAN_LENGTH);
    }

    private String generateRandomDigits(int length) {
        return new SecureRandom().ints(length, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }
}