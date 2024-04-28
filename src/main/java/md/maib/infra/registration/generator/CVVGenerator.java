package md.maib.infra.registration.generator;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.stream.Collectors;

@Component
public class CVVGenerator {

    private static final int CVV_LENGTH = 3;

    public String generateCVV() {
        return generateRandomDigits(CVV_LENGTH);
    }

    private String generateRandomDigits(int length) {
        return new SecureRandom().ints(length, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }
}