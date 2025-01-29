package budhioct.dev.service;

import budhioct.dev.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    UserDTO.RegisterResponse register(UserDTO.RegisterRequest request);
    UserDTO.LoginResponse login(UserDTO.LoginRequest request);
    UserDTO.LoginResponse refreshToken(HttpServletRequest request, HttpServletResponse response);
    void changePassword(UserDTO.ChangePasswordRequest request, UserDetails userDetails);
}
