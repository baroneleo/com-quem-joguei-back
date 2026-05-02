package main.java.com.soccergame.service;

import com.soccergame.dto.auth.AuthResponse;
import com.soccergame.dto.auth.LoginRequest;
import com.soccergame.dto.auth.RegisterRequest;
import com.soccergame.entity.Stats;
import com.soccergame.entity.User;
import com.soccergame.exception.BadRequestException;
import com.soccergame.exception.ResourceAlreadyExistsException;
import com.soccergame.repository.StatsRepository;
import com.soccergame.repository.UserRepository;
import com.soccergame.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final StatsRepository statsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final Logger log = LoggerFactory.getLogger(AuthService.class);

    public AuthService(UserRepository userRepository,
                       StatsRepository statsRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.statsRepository = statsRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Registra um usuário com senha criptografada e cria estatísticas iniciais.
     * Lança ResourceAlreadyExistsException se username já existir.
     */
    @Transactional
    public void register(RegisterRequest req) {
        String username = req.getUsername().trim();
        if (userRepository.existsByUsername(username)) {
            throw new ResourceAlreadyExistsException("username already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        userRepository.save(user);

        Stats stats = new Stats();
        stats.setUser(user);
        stats.setWins(0);
        stats.setLosses(0);
        statsRepository.save(stats);

        log.info("Registered new user: {}", username);
    }

    /**
     * Autentica usuário e retorna JWT no AuthResponse.
     * Lança BadRequestException em credenciais inválidas.
     */
    public AuthResponse login(LoginRequest req) {
        String username = req.getUsername().trim();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("invalid credentials"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BadRequestException("invalid credentials");
        }

        String token = jwtTokenProvider.generateToken(user.getUsername());
        log.info("User logged in: {}", username);
        return new AuthResponse(token);
    }
}
