package product.api.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import product.api.response.JwtTokenResponse;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;


@Component
public class JwtUtil {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(JwtUtil.class);
    private static final String SECRET_KEY = "@!#%5458980/&)qwtrhmtyjtyjtewvcnvmbgjgh";

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public JwtTokenResponse generateToken(UserDetails userDetails) {
        long expirationMs = 86400000;
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMs);
        List<String> roles = List.of("ROLE_USER", "ROLE_ADMIN");

        String token = Jwts.builder()
                .claim("username", userDetails.getUsername())
                .claim("roles", roles)
                .claim("issued_at", now.getTime())
                .claim("expiration", expiration.getTime())
                .signWith(getSigningKey())
                .compact();

        return new JwtTokenResponse(token, expiration.getTime());
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("username", String.class);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);

            return !isTokenExpired(claims);
        } catch (ExpiredJwtException e) {
            logger.warn("Token has expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.warn("Token is unsupported: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.warn("Token is malformed: {}", e.getMessage());
        } catch (JwtException e) {
            logger.warn("JWT error: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Token authentication error: {}", e.getMessage());
        }
        return false;
    }

    private boolean isTokenExpired(Jws<Claims> claims) {
        return claims.getBody().get("expiration", Long.class) < System.currentTimeMillis();
    }
}
