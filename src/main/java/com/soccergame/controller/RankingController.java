package main.java.com.soccergame.controller;

import com.soccergame.dto.ranking.RankingDto;
import com.soccergame.service.RankingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints para consultar o ranking:
 * - GET /ranking/global
 * - GET /ranking/{country}
 */
@RestController
@RequestMapping("/ranking")
public class RankingController {

    private final RankingService rankingService;
    private final Logger log = LoggerFactory.getLogger(RankingController.class);

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping("/global")
    public ResponseEntity<List<RankingDto>> global() {
        log.debug("Fetching global ranking");
        List<RankingDto> list = rankingService.globalRanking();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{country}")
    public ResponseEntity<List<RankingDto>> byCountry(@PathVariable String country) {
        log.debug("Fetching ranking for country={}", country);
        List<RankingDto> list = rankingService.countryRanking(country);
        return ResponseEntity.ok(list);
    }
}
