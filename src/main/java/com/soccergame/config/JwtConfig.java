package main.java.com.soccergame.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configura beans de configuração de segurança compartilhados.
 * - PasswordEncoder: usado em AuthService e em testes.
 * - JwtProperties: wrapper para propriedades JWT que sua JwtTokenProvider pode consumir.
 *
 * Se sua JwtTokenProvider já lê diretamente as propriedades via @Value, você pode remover JwtProperties.
 */
@Configuration
public class JwtConfig {

    @Value("${jwt.secret:verysecretkeychangeinprod}")
    private String jwtSecret;

    @Value("${jwt.expirationMs:86400000}")
    private long jwtExpirationMs;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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
