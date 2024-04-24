package md.maib.bank.sample.config.impl;

import jakarta.websocket.EncodeException;
import md.maib.bank.sample.config.AESCoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class AESCoderImpl implements AESCoder {

    @Value("${security.encryption.key}")
    private String secretKey;

    private static final String AES = "AES";
    private static final int GCM_TAG_LENGTH = 128; // bits
    private static final int GCM_IV_LENGTH = 12; // bytes recommended for GCM

    private SecretKeySpec getSecretKeySpec() {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        return new SecretKeySpec(decodedKey, AES);
    }

    public String encode(String data) throws EncodeException {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecureRandom secureRandom = new SecureRandom();
            byte[] iv = new byte[GCM_IV_LENGTH];
            secureRandom.nextBytes(iv); // Generate a random IV for each encryption
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

            cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(), parameterSpec);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(iv) + ":" + Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            throw new EncodeException(data, "Error occurred while encrypting data", e);
        }
    }

}
