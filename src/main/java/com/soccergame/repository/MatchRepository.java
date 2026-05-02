package main.java.com.soccergame.repository;

import com.soccergame.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositório para entidade Match.
 */
public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByPlayer1Id(Long player1Id);

    List<Match> findByPlayer2Id(Long player2Id);

    List<Match> findByWinnerId(Long winnerId);
}
