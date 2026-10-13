package com.bidsphere.security;

import com.bidsphere.repository.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
 private final JwtService jwt; private final UserRepository users;
 public JwtFilter(JwtService jwt, UserRepository users){this.jwt=jwt;this.users=users;}
 @Override protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException{
   String h=req.getHeader("Authorization");
   if(h!=null && h.startsWith("Bearer ") && SecurityContextHolder.getContext().getAuthentication()==null){
     try {
       var claims=jwt.parse(h.substring(7)); var user=users.findByEmailIgnoreCase(claims.getSubject()).orElse(null);
       if(user!=null && user.active){
         var auth=new UsernamePasswordAuthenticationToken(user.email,null,List.of(new SimpleGrantedAuthority("ROLE_"+user.role)));
         SecurityContextHolder.getContext().setAuthentication(auth);
       }
     } catch(Exception ignored) { /* invalid tokens are treated as unauthenticated */ }
   }
   chain.doFilter(req,res);
 }
}
