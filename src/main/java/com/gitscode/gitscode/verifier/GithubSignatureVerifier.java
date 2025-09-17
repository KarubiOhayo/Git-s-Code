package com.gitscode.gitscode.verifier;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GithubSignatureVerifier {

  private final String secret;

  public GithubSignatureVerifier(@Value("${github.webhook.secret}") String secret) {
    this.secret = secret;
  }

  public boolean verify(String sigHeader, String body) {
    if (sigHeader == null || !sigHeader.startsWith("sha256=")) {
      return false;
    }
    String expectedHex = hmacCha256Hex(secret, body);
    String providedHex = sigHeader.substring("sha256=".length());
    return MessageDigest.isEqual(
        expectedHex.getBytes(StandardCharsets.UTF_8),
        providedHex.getBytes(StandardCharsets.UTF_8)
    );
  }

  private String hmacCha256Hex(String key, String data) {
    try {
      Mac mac = Mac.getInstance("HmacSHA256");
      mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
      byte[] raw = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(raw);
    } catch (Exception e) {
      throw new IllegalStateException("HMAC failed", e);
    }
  }
}
