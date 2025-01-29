package budhioct.dev.service.impl;

import budhioct.dev.dto.UserDTO;
import budhioct.dev.entity.Token;
import budhioct.dev.entity.User;
import budhioct.dev.repository.TokenRepository;
import budhioct.dev.repository.UserRepository;
import budhioct.dev.security.jwt.JwtService;
import budhioct.dev.security.role.Role;
import budhioct.dev.security.token.TokenType;
import budhioct.dev.service.UserService;
import budhioct.dev.utilities.BCrypt;
import budhioct.dev.utilities.validation.ValidationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ValidationService validation;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Value("${app.jwt.jwtExpirationMs}") private long jwtExpirationMs;

    @Transactional
    public UserDTO.RegisterResponse register(UserDTO.RegisterRequest request) {
        validation.validate(request);

        if (userRepository.findFirstByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole()));
        userRepository.save(user);

        return UserDTO.toRegisterResponse(user);
    }

    @Transactional
    public UserDTO.LoginResponse login(UserDTO.LoginRequest request) {
        validation.validate(request);

        User user = userRepository.findFirstByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        validatePassword(request.getPassword(), user.getPassword());
        authenticateUser(request.getEmail(), request.getPassword());

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        long minutes = TimeUnit.MILLISECONDS.toMinutes(jwtExpirationMs);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return UserDTO.LoginResponse.builder()
                .expiresIn(minutes)
                .role(jwtService.extractRole(jwtToken))
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public UserDTO.LoginResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Forbidden, Bearer is null");
            }

            final String jwt_token = authHeader.substring(7);
            Token store_token = tokenRepository.findFirstByToken(jwt_token)
                    .orElseThrow(() -> new RuntimeException("Forbidden, Bearer token is not found"));

            if (store_token.isExpired() || store_token.isRevoked()) {
                throw new RuntimeException("Forbidden, Bearer token is expired or revoked");
            }

            UserDTO.LoginResponse data = null;
            final String user_email = jwtService.extractUsername(jwt_token);
            if (user_email != null) {
                User user = this.userRepository.findFirstByEmail(user_email)
                        .orElseThrow(() -> new RuntimeException("Forbidden, User is not found"));
                if (jwtService.isTokenValid(jwt_token, user)) {
                    String accessToken = jwtService.generateToken(user);
                    String refreshToken = jwtService.generateRefreshToken(user);
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(jwtExpirationMs);
                    revokeAllUserTokens(user);
                    saveUserToken(user, accessToken);

                    data = UserDTO.LoginResponse.builder()
                            .expiresIn(minutes)
                            .role(jwtService.extractRole(accessToken))
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                }
            }
            return data;

        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Transactional
    public void changePassword(UserDTO.ChangePasswordRequest request, UserDetails userDetails) {
        validation.validate(request);

        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        User user = userRepository.findFirstByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }

        validateNewPasswords(request.getNewPassword(), request.getConfirmationPassword(), user.getPassword());
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }

    private void validatePassword(String rawPassword, String hashedPassword) {
        if (!BCrypt.checkpw(rawPassword, hashedPassword)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
    }

    private void authenticateUser(String email, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication failed");
        }
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = new Token();
        token.setUser(user);
        token.setToken(jwtToken);
        token.setTokenType(TokenType.BEARER);
        token.setExpired(false);
        token.setRevoked(false);
        tokenRepository.save(token);
    }

    private void validateNewPasswords(String newPassword, String confirmationPassword, String currentPassword) {
        if (!newPassword.equals(confirmationPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }
        if (passwordEncoder.matches(newPassword, currentPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password cannot be the same as the current password");
        }
    }

}
