package tn.esprit.contractmanegement.Config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import tn.esprit.contractmanegement.Service.UserService;
import tn.esprit.contractmanegement.Entity.User;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final UserService userService;

    public JwtUtil(UserService userService) {
        this.userService = userService;
    }

    // ✅ Generate JWT with userId, username, and role
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())  // Username as subject
                .claim("userId", user.getId())   // ✅ Added userId
//                .claim("role", user.getRole().name())  // ✅ Store role in JWT
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1-hour expiry
                .signWith(key)
                .compact();
    }

    // ✅ Extract username (sub) from JWT
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ✅ Extract userId from JWT
    public Long extractUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId", Long.class);
    }

    // ✅ Extract role from JWT
    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    // ✅ Validate token: Check username exists & token is not expired
    public boolean validateToken(String token) {
        String username = extractUsername(token);
        Long userId = extractUserId(token);  // Ensure userId exists

        Optional<User> user = userService.getUsername(username);

        return user.isPresent() && user.get().getId().equals(userId) && !isTokenExpired(token);
    }

    // ✅ Check if token is expired
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}
