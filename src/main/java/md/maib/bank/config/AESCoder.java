package md.maib.bank.config;

import jakarta.websocket.DecodeException;
import jakarta.websocket.EncodeException;
import org.springframework.stereotype.Service;

@Service
public interface AESCoder {
    String encode(String data) throws EncodeException;

    String decode(String encodedData) throws DecodeException;
}
