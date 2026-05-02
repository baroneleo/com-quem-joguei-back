package com.soccergame.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

 import jakarta.annotation.PostConstruct;
import java.util.*;

/**
 * Serviço leve de validação de "jogadores" e clubes.
 * Implementa um mock em memória que relaciona nome de jogador -> clubes onde atuou.
 * Métodos:
 * - isKnownPlayer(name): verifica se o nome existe no mock
 * - hasPlayedInSameClub(name1, name2): verifica se os dois atuaram em algum mesmo clube
 * - normalize(name): normaliza input para comparação
 *
 * Em produção, substitua por busca real em banco/dataset.
 */
@Service
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    // playerName (lowercase) -> set of club identifiers
    private final Map<String, Set<String>> playerClubs = new HashMap<>();

    @PostConstruct
    public void init() {
        // seed mock data. Use nomes simples e alguns clubes.
        // Em ambiente real, substitua por injeção/repositório.
        addPlayer("cristiano ronaldo", "manchester united");
        addPlayer("cristiano ronaldo", "real madrid");
        addPlayer("cristiano ronaldo", "sporting");

        addPlayer("ronaldo nazario", "barcelona");
        addPlayer("ronaldo nazario", "real madrid");
        addPlayer("ronaldo nazario", "inter milan");

        addPlayer("messi", "barcelona");
        addPlayer("messi", "psg");

        addPlayer("neymar", "barcelona");
        addPlayer("neymar", "psg");

        addPlayer("zidane", "juventus");
        addPlayer("zidane", "real madrid");

        log.info("GameService seeded {} players", playerClubs.size());
    }

    private void addPlayer(String name, String club) {
        String n = normalize(name);
        playerClubs.computeIfAbsent(n, k -> new HashSet<>()).add(club.toLowerCase());
    }

    public boolean isKnownPlayer(String name) {
        if (name == null) return false;
        return playerClubs.containsKey(normalize(name));
    }

    /**
     * Verifica se dois jogadores atuaram no mesmo clube (interseção não vazia).
     */
    public boolean hasPlayedInSameClub(String name1, String name2) {
        if (name1 == null || name2 == null) return false;
        Set<String> c1 = playerClubs.get(normalize(name1));
        Set<String> c2 = playerClubs.get(normalize(name2));
        if (c1 == null || c2 == null) return false;
        for (String club : c1) {
            if (c2.contains(club)) return true;
        }
        return false;
    }

    public String normalize(String name) {
        return Optional.ofNullable(name).map(s -> s.trim().toLowerCase()).orElse(null);
    }

    /**
     * Retorna clubes do jogador (cópia imutável) ou empty set se desconhecido.
     */
    public Set<String> clubsOf(String name) {
        return Collections.unmodifiableSet(playerClubs.getOrDefault(normalize(name), Collections.emptySet()));
    }
}
