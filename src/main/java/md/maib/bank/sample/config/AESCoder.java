package md.maib.bank.sample.config;

import jakarta.websocket.EncodeException;
import org.springframework.stereotype.Service;

@Service
public interface AESCoder {

    String encode(String encodedData) throws EncodeException;

}
