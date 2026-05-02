package com.soccergame.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configura beans de configuração JWT compartilhados.
 * - JwtProperties: wrapper para propriedades JWT que sua JwtTokenProvider pode consumir.
 *
 * O PasswordEncoder fica em com.soccergame.security.PasswordConfig.
 */
@Configuration
public class JwtConfig {

    @Value("${jwt.secret:verysecretkeychangeinprod-32-bytes-minimum-1234}")
    private String jwtSecret;

    @Value("${jwt.expirationMs:86400000}")
    private long jwtExpirationMs;
    

    @Bean
    public JwtProperties jwtProperties() {
        return new JwtProperties(jwtSecret, jwtExpirationMs);
    }

    public static class JwtProperties {
        private final String secret;
        private final long expirationMs;

        public JwtProperties(String secret, long expirationMs) {
            this.secret = secret;
            this.expirationMs = expirationMs;
        }

        public String getSecret() {
            return secret;
        }

        public long getExpirationMs() {
            return expirationMs;
        }
    }
}
