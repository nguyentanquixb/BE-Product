package product.api.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import product.api.entity.Permission;
import product.api.entity.User;
import product.api.repository.UserRepository;
import product.api.response.JwtTokenResponse;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class JwtUtil {

    @Autowired
    private UserRepository userRepository;
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(JwtUtil.class);
    private static final String SECRET_KEY = "@!#%5458980/&)qwtrhmtyjtyjtewvcnvmbgjgh";

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public JwtTokenResponse generateToken(UserDetails userDetails) {
        long expirationMs = 86400000;
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMs);

        User user = userRepository.findByEmail(userDetails.getUsername());
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        List<String> permissions = user.getPermissions().stream()
                .map(Permission::getName)
                .collect(Collectors.toList());

        String token = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("permissions", permissions)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSigningKey())
                .compact();

        return new JwtTokenResponse(token, expiration.getTime());
    }

    public Claims extractClaims(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        System.out.println("Extracted claims: " + claims);
        return claims;
    }

}
