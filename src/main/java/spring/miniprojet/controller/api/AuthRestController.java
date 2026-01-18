package spring.miniprojet.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import spring.miniprojet.config.JwtProvider;
import spring.miniprojet.entity.User;
import spring.miniprojet.service.UserService;

import java.util.HashMap;
import java.util.Map;

/**
 * REST API Authentication Controller
 * Provides JWT token generation for admin users only
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;

    /**
     * Admin login endpoint - returns JWT token
     * Only admin users can login via this endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        try {
            // Authenticate user with credentials
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));

            // Get user details
            User user = userService.findByUsername(request.getUsername()).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "User not found"));
            }

            // Only admins can access the API
            if (!user.getRole().name().equals("ADMIN")) {
                return ResponseEntity.status(403).body(
                        Map.of("error", "Only administrators can access the REST API"));
            }

            // Generate JWT token
            String token = jwtProvider.generateToken(user.getUsername(), user.getRole().name());

            // Build response
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", user.getUsername());
            response.put("role", user.getRole());
            response.put("expiresIn", 86400); // 24 hours in seconds
            response.put("tokenType", "Bearer");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Invalid credentials or user is not an admin"));
        }
    }

    /**
     * Validate token endpoint
     */
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest().body(
                        Map.of("valid", false, "error", "Missing or invalid Authorization header"));
            }

            String token = authHeader.substring(7);

            if (jwtProvider.validateToken(token)) {
                String username = jwtProvider.getUsernameFromToken(token);
                String role = jwtProvider.getRoleFromToken(token);

                return ResponseEntity.ok(Map.of(
                        "valid", true,
                        "username", username,
                        "role", role));
            } else {
                return ResponseEntity.ok(Map.of("valid", false, "error", "Invalid or expired token"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("valid", false, "error", e.getMessage()));
        }
    }

    /**
     * Request body for login
     */
    public static class LoginRequest {
        public String username;
        public String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
