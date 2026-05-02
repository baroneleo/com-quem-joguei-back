package main.java.com.soccergame.controller;

import com.soccergame.dto.game.MatchStartRequest;
import com.soccergame.dto.game.MatchEndRequest;
import com.soccergame.dto.game.MatchResponse;
import com.soccergame.service.MatchService;
import com.soccergame.dto.SimpleResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints REST para iniciar e finalizar partidas.
 * - POST /match/start  -> inicia uma partida (persistência mínima) e retorna dados da partida criada
 * - POST /match/end    -> finaliza a partida, atualiza estatísticas e retorna resultado
 */
@RestController
@RequestMapping("/match")
public class MatchController {

    private final MatchService matchService;
    private final Logger log = LoggerFactory.getLogger(MatchController.class);

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/start")
    public ResponseEntity<MatchResponse> startMatch(@Valid @RequestBody MatchStartRequest req) {
        log.info("Start match request: {} vs {} country={}", req.getPlayer1(), req.getPlayer2(), req.getCountry());
        MatchResponse resp = matchService.startMatch(req);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/end")
    public ResponseEntity<SimpleResponse> endMatch(@Valid @RequestBody MatchEndRequest req) {
        log.info("End match request: matchId={} winner={}", req.getMatchId(), req.getWinnerUsername());
        matchService.endMatch(req);
        return ResponseEntity.ok(new SimpleResponse("match ended"));
    }
}
