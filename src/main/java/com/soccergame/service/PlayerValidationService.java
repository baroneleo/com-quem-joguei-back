package main.java.com.soccergame.service;

import com.soccergame.exception.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Serviço que valida jogadas (nomes de jogadores citados).
 * - Verifica se o nome é conhecido (usa GameService)
 * - Impede repetição de nomes na mesma partida
 * - Verifica se jogador atuou no mesmo clube que outro (regras do jogo)
 *
 * Observação: manter estado de usados fora deste serviço (ex.: Room.usedNames).
 */
@Service
public class PlayerValidationService {

    private final GameService gameService;

    public PlayerValidationService(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Valida uma jogada:
     * - nome não nulo/não vazio
     * - não repetido (verificar set de usados)
     * - jogador conhecido (existe no mock)
     * - se prevName for não-nulo, validar que compartilharam clube (regra do jogo)
     *
     * Lança BadRequestException em caso de falha.
     */
    public void validateMove(String name, Set<String> usedNames, String prevName) {
        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("name is required");
        }
        String normalized = gameService.normalize(name);
        if (usedNames.contains(normalized)) {
            throw new BadRequestException("name already used in this match");
        }
        if (!gameService.isKnownPlayer(normalized)) {
            throw new BadRequestException("unknown player: " + name);
        }
        if (prevName != null && !prevName.trim().isEmpty()) {
            if (!gameService.hasPlayedInSameClub(prevName, name)) {
                throw new BadRequestException("players did not play in same club");
            }
        }
    }
}
