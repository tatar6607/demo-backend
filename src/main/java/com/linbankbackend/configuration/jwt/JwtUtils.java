package com.linbankbackend.configuration.jwt;


import com.linbankbackend.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

   @Value("${bank.app.secret_key}")
   private String SECRET_KEY;

   public String extractSSN(String token) {
      return extractClaim(token, Claims::getSubject);
   }
   public Date extractExpiration(String token) {
      return extractClaim(token, Claims::getExpiration);
   }
   public <T> T extractClaim(String token,
                             Function<Claims, T> claimsResolver) {
      final Claims claims = extractAllClaims(token);
      return claimsResolver.apply(claims);
   }
   private Claims extractAllClaims(String token) {
      return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token)
              .getBody();
   }
   public Boolean isTokenExpired(String token) {
      return extractExpiration(token).before(new Date());
   }
   public String generateToken(Authentication authentication) {
      User user = (User) authentication.getPrincipal();
      Map<String, Object> claims = new HashMap<>();
      return createToken(claims, user.getSsn());
   }
   private String createToken(Map<String, Object> claims, String subject) {
      Calendar issuedCalendar = Calendar.getInstance();
      Calendar expiredCalendar = Calendar.getInstance();
      expiredCalendar.setTime(issuedCalendar.getTime());
      expiredCalendar.add(Calendar.HOUR, 12);
      return Jwts.builder().setClaims(claims).setSubject(subject)
              .setIssuedAt(issuedCalendar.getTime())
              .setExpiration(expiredCalendar.getTime())
              .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
   }
   public Boolean validateToken(String token, UserDetails userDetails) {
      final String ssn = extractSSN(token);
      return (ssn.equals(userDetails.getUsername()) && !isTokenExpired(token));
   }



}
