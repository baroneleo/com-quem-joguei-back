package main.java.com.soccergame.repository;

import com.soccergame.entity.Stats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositório para entidade Stats (estatísticas globais do usuário).
 */
public interface StatsRepository extends JpaRepository<Stats, Long> {

    Optional<Stats> findByUserId(Long userId);

    Optional<Stats> findByUser_Username(String username);
}
