package md.maib.infra.registration.generator;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.stream.Collectors;

@Component
public class CVVGenerator {
    public String generateCVV() {
        return generateRandomDigits();
    }

    private String generateRandomDigits() {
        return new SecureRandom().ints(0, 10)
                .limit(3)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }
}
