package main.java.com.soccergame.repository;

import com.soccergame.entity.CountryStats;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repositório para estatísticas por país.
 */
public interface CountryStatsRepository extends JpaRepository<CountryStats, Long> {

    List<CountryStats> findByCountry(String country);

    List<CountryStats> findByUserId(Long userId);
}
