package com.soccergame.service;

import com.soccergame.dto.ranking.RankingDto;
import com.soccergame.entity.CountryStats;
import com.soccergame.entity.Stats;
import com.soccergame.repository.CountryStatsRepository;
import com.soccergame.repository.StatsRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço de ranking:
 * - globalRanking: usa Stats (wins, losses)
 * - countryRanking: usa CountryStats filtrado por país
 *
 * Score = wins * 3 - losses
 */
@Service
public class RankingService {

    private final StatsRepository statsRepository;
    private final CountryStatsRepository countryStatsRepository;

    public RankingService(StatsRepository statsRepository, CountryStatsRepository countryStatsRepository) {
        this.statsRepository = statsRepository;
        this.countryStatsRepository = countryStatsRepository;
    }

    public List<RankingDto> globalRanking() {
        return statsRepository.findAll().stream()
                .map(this::toDto)
                .sorted(Comparator.comparingInt(RankingDto::getScore).reversed())
                .collect(Collectors.toList());
    }

    public List<RankingDto> countryRanking(String country) {
        return countryStatsRepository.findByCountry(country).stream()
                .map(this::toDto)
                .sorted(Comparator.comparingInt(RankingDto::getScore).reversed())
                .collect(Collectors.toList());
    }

    private RankingDto toDto(Stats s) {
        int score = s.getWins() * 3 - s.getLosses();
        return new RankingDto(s.getUser().getUsername(), s.getWins(), s.getLosses(), score);
    }

    private RankingDto toDto(CountryStats cs) {
        int score = cs.getWins() * 3 - cs.getLosses();
        return new RankingDto(cs.getUser().getUsername(), cs.getWins(), cs.getLosses(), score);
    }
}
