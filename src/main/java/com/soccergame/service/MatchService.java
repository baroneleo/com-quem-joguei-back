package com.soccergame.service;

import com.soccergame.dto.game.MatchEndRequest;
import com.soccergame.dto.game.MatchResponse;
import com.soccergame.dto.game.MatchStartRequest;
import com.soccergame.entity.CountryStats;
import com.soccergame.entity.Match;
import com.soccergame.entity.Stats;
import com.soccergame.entity.User;
import com.soccergame.exception.BadRequestException;
import com.soccergame.exception.NotFoundException;
import com.soccergame.repository.CountryStatsRepository;
import com.soccergame.repository.MatchRepository;
import com.soccergame.repository.StatsRepository;
import com.soccergame.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Serviço para iniciar e finalizar partidas, persistindo Match e atualizando Stats / CountryStats.
 */
@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final StatsRepository statsRepository;
    private final CountryStatsRepository countryStatsRepository;
    private final Logger log = LoggerFactory.getLogger(MatchService.class);

    public MatchService(MatchRepository matchRepository,
                        UserRepository userRepository,
                        StatsRepository statsRepository,
                        CountryStatsRepository countryStatsRepository) {
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
        this.statsRepository = statsRepository;
        this.countryStatsRepository = countryStatsRepository;
    }

    /**
     * Inicia e persiste uma partida mínima.
     */
    @Transactional
    public MatchResponse startMatch(MatchStartRequest req) {
        String p1 = req.getPlayer1().trim();
        String p2 = req.getPlayer2().trim();
        if (p1.equalsIgnoreCase(p2)) {
            throw new BadRequestException("players must be different");
        }

        User user1 = userRepository.findByUsername(p1).orElseThrow(() -> new NotFoundException("player1 not found"));
        User user2 = userRepository.findByUsername(p2).orElseThrow(() -> new NotFoundException("player2 not found"));

        Match match = new Match();
        match.setPlayer1(user1);
        match.setPlayer2(user2);
        match.setCountry(req.getCountry());
        match.setCreatedAt(Instant.now());
        Match saved = matchRepository.save(match);

        log.info("Match started id={} {} vs {} country={}", saved.getId(), p1, p2, req.getCountry());
        return new MatchResponse(saved.getId(), user1.getUsername(), user2.getUsername(), saved.getCountry(), saved.getCreatedAt());
    }

    /**
     * Finaliza partida: define winner, atualiza Stats e CountryStats.
     */
    @Transactional
    public void endMatch(MatchEndRequest req) {
        Match match = matchRepository.findById(req.getMatchId()).orElseThrow(() -> new NotFoundException("match not found"));
        String winnerUsername = req.getWinnerUsername().trim();

        User winner = userRepository.findByUsername(winnerUsername).orElseThrow(() -> new NotFoundException("winner not found"));

        // verify winner is one of the players
        Long winnerId = winner.getId();
        if (!(winnerId.equals(match.getPlayer1().getId()) || winnerId.equals(match.getPlayer2().getId()))) {
            throw new BadRequestException("winner must be one of the match players");
        }

        match.setWinner(winner);
        matchRepository.save(match);

        // update global stats
        Stats winnerStats = statsRepository.findByUserId(winner.getId()).orElseGet(() -> {
            Stats s = new Stats();
            s.setUser(winner);
            s.setWins(0);
            s.setLosses(0);
            return s;
        });
        Stats loserStats;
        User loser = match.getPlayer1().getId().equals(winnerId) ? match.getPlayer2() : match.getPlayer1();
        loserStats = statsRepository.findByUserId(loser.getId()).orElseGet(() -> {
            Stats s = new Stats();
            s.setUser(loser);
            s.setWins(0);
            s.setLosses(0);
            return s;
        });

        winnerStats.setWins(winnerStats.getWins() + 1);
        loserStats.setLosses(loserStats.getLosses() + 1);
        statsRepository.save(winnerStats);
        statsRepository.save(loserStats);

        // update country stats (create if absent)
        String country = match.getCountry();
        CountryStats csWinner = countryStatsRepository.findByUserId(winner.getId()).stream()
                .filter(c -> c.getCountry().equalsIgnoreCase(country)).findFirst()
                .orElseGet(() -> {
                    CountryStats c = new CountryStats();
                    c.setUser(winner);
                    c.setCountry(country);
                    c.setWins(0);
                    c.setLosses(0);
                    return c;
                });

        CountryStats csLoser = countryStatsRepository.findByUserId(loser.getId()).stream()
                .filter(c -> c.getCountry().equalsIgnoreCase(country)).findFirst()
                .orElseGet(() -> {
                    CountryStats c = new CountryStats();
                    c.setUser(loser);
                    c.setCountry(country);
                    c.setWins(0);
                    c.setLosses(0);
                    return c;
                });

        csWinner.setWins(csWinner.getWins() + 1);
        csLoser.setLosses(csLoser.getLosses() + 1);
        countryStatsRepository.save(csWinner);
        countryStatsRepository.save(csLoser);

        log.info("Match ended id={} winner={}", match.getId(), winnerUsername);
    }
}
