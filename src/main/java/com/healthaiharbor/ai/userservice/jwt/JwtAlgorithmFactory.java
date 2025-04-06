package com.healthaiharbor.ai.userservice.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Factory class to provide the appropriate {@link JwtAlgorithm} implementation
 * based on the configured JWT signing algorithm.
 */
@Component
public class JwtAlgorithmFactory {

    private static final Logger logger = LoggerFactory.getLogger(JwtAlgorithmFactory.class);

    private final HmacJwtAlgorithm hmacJwtAlgorithm;
    private final RsaJwtAlgorithm rsaJwtAlgorithm;

    @Value("${jwt.algorithm}")
    private String algorithm; // HS256, RS256, etc.

    /**
     * Constructor to initialize the available JWT algorithms.
     *
     * @param hmacJwtAlgorithm Instance of HMAC-based JWT algorithm.
     * @param rsaJwtAlgorithm  Instance of RSA-based JWT algorithm.
     */
    public JwtAlgorithmFactory(HmacJwtAlgorithm hmacJwtAlgorithm, RsaJwtAlgorithm rsaJwtAlgorithm) {
        this.hmacJwtAlgorithm = hmacJwtAlgorithm;
        this.rsaJwtAlgorithm = rsaJwtAlgorithm;
    }

    /**
     * Returns the appropriate JWT algorithm implementation based on the configured algorithm.
     *
     * @return An instance of {@link JwtAlgorithm} corresponding to the selected signing method.
     * @throws IllegalArgumentException if the algorithm is not supported.
     */
    public JwtAlgorithm getJwtAlgorithm() {
        logger.info("Selecting JWT algorithm: {}", algorithm);
        return switch (algorithm) {
            case "HS256", "HS384", "HS512" -> {
                logger.debug("Using HMAC-based JWT algorithm");
                yield hmacJwtAlgorithm;
            }
            case "RS256", "RS384", "RS512" -> {
                logger.debug("Using RSA-based JWT algorithm");
                yield rsaJwtAlgorithm;
            }
            default -> {
                logger.error("Unsupported JWT algorithm: {}", algorithm);
                throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
            }
        };
    }
}
