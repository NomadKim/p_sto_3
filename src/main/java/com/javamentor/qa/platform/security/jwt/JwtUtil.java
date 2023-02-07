package com.javamentor.qa.platform.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private final UserDao userDao;

    public JwtUtil(UserDao userDao) {
        this.userDao = userDao;
    }

    public Algorithm getAlgorithm() {
        return Algorithm.HMAC256(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {

        Algorithm algorithm = getAlgorithm();

        String token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withClaim("role", user.getRole().getId())
                .sign(algorithm);
        return token;
    }

    public String generateLongToken(User user) {

        Algorithm algorithm = getAlgorithm();
        String token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withClaim("role", user.getRole().getId())
                .sign(algorithm);
        return token;
    }

    public DecodedJWT checkToken(String token) {

        Algorithm algorithm = getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT;
    }

    public UsernamePasswordAuthenticationToken getAuthenticationTokenByDecodedJwtToken(DecodedJWT decodedJWT) {

        String username = decodedJWT.getSubject();
        Optional<User> user = userDao.getUserByEmail(username);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.get(), null, user.get().getAuthorities());
        return authenticationToken;
    }
}
