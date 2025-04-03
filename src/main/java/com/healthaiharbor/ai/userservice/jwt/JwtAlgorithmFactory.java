package com.healthaiharbor.ai.userservice.jwt;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtAlgorithmFactory {

    private final HmacJwtAlgorithm hmacJwtAlgorithm;
    private final RsaJwtAlgorithm rsaJwtAlgorithm;

    @Value("${jwt.algorithm}")
    private String algorithm; // HS256, RS256, etc.

    public JwtAlgorithmFactory(HmacJwtAlgorithm hmacJwtAlgorithm, RsaJwtAlgorithm rsaJwtAlgorithm) {
        this.hmacJwtAlgorithm = hmacJwtAlgorithm;
        this.rsaJwtAlgorithm = rsaJwtAlgorithm;
    }

    public JwtAlgorithm getJwtAlgorithm() {
        return switch (algorithm) {
            case "HS256", "HS384", "HS512" -> hmacJwtAlgorithm;
            case "RS256", "RS384", "RS512" -> rsaJwtAlgorithm;
            default -> throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
        };
    }
}
