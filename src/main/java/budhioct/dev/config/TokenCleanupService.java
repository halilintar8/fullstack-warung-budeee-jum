package budhioct.dev.config;

import budhioct.dev.entity.Token;
import budhioct.dev.repository.TokenRepository;
import budhioct.dev.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenCleanupService {

    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateExpiredTokens() {
        List<Token> activeTokens = tokenRepository.findAllValidTokens();

        int updatedCount = 0;

        for (Token token : activeTokens) {
            String tokenValue = token.getToken();
            Date expiration = jwtService.isExtractExpiration(tokenValue);

            if (Instant.now().isAfter(expiration.toInstant())) {
                token.setExpired(true);
                token.setRevoked(true);
                tokenRepository.save(token);
                updatedCount++;
            }
        }
        System.out.println("Token update completed. Expired tokens updated: " + updatedCount);
    }

}
