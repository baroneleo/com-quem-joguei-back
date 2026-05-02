package main.java.com.soccergame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Ponto de entrada da aplicação.
 * Habilita agendamento para timers de partidas (usado no servidor de WebSocket).
 */
@SpringBootApplication
@EnableScheduling
public class SoccerGameApplication {
    public static void main(String[] args) {
        SpringApplication.run(SoccerGameApplication.class, args);
    }
}
