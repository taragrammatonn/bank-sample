package md.maib.infra.registration.generator;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.security.SecureRandom;
import java.util.stream.Collectors;

@Component
public class PANGenerator {
    public String generatePAN() {
        String bankIdentifier = "4562";
        String uniqueNumber = String.valueOf(Instant.now().getEpochSecond());
        String randomDigits = generateRandomDigits(10);
        String pan = bankIdentifier + uniqueNumber + randomDigits;

        return pan.substring(0, 16);
    }

    private String generateRandomDigits(int length) {
        return new SecureRandom().ints(length, 0, 10)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining());
    }
}