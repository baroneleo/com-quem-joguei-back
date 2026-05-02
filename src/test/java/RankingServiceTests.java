import com.soccergame.dto.ranking.RankingDto;
import com.soccergame.entity.Stats;
import com.soccergame.entity.User;
import com.soccergame.repository.StatsRepository;
import com.soccergame.repository.UserRepository;
import com.soccergame.service.RankingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes básicos do RankingService.
 */
@SpringBootTest
public class RankingServiceTests {

    @Autowired
    private RankingService rankingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StatsRepository statsRepository;

    @AfterEach
    void cleanup() {
        statsRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void globalRankingShouldSortByScore() {
        // create users and stats
        User u1 = userRepository.save(new User(null, "alice", "$2a$10$hash", null));
        User u2 = userRepository.save(new User(null, "bob", "$2a$10$hash", null));
        User u3 = userRepository.save(new User(null, "carol", "$2a$10$hash", null));

        // scores: u1 wins=3 losses=0 => score=9
        Stats s1 = new Stats(null, u1, 3, 0);
        // u2 wins=2 losses=1 => score=5
        Stats s2 = new Stats(null, u2, 2, 1);
        // u3 wins=0 losses=0 => score=0
        Stats s3 = new Stats(null, u3, 0, 0);

        statsRepository.save(s1);
        statsRepository.save(s2);
        statsRepository.save(s3);

        List<RankingDto> ranking = rankingService.globalRanking();

        assertThat(ranking).isNotNull();
        assertThat(ranking).hasSize(3);
        assertThat(ranking.get(0).getUsername()).isEqualTo("alice");
        assertThat(ranking.get(1).getUsername()).isEqualTo("bob");
        assertThat(ranking.get(2).getUsername()).isEqualTo("carol");

        // verify scores computed correctly
        assertThat(ranking.get(0).getScore()).isEqualTo(3 * 3 - 0);
        assertThat(ranking.get(1).getScore()).isEqualTo(2 * 3 - 1);
        assertThat(ranking.get(2).getScore()).isEqualTo(0);
    }
}
