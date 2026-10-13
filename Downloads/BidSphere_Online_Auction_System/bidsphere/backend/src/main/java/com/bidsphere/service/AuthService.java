package com.bidsphere.service;

import com.bidsphere.entity.User;
import com.bidsphere.repository.UserRepository;
import com.bidsphere.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

@Service
public class AuthService {
 private final UserRepository users; private final PasswordEncoder encoder; private final JwtService jwt;
 public AuthService(UserRepository u,PasswordEncoder e,JwtService j){users=u;encoder=e;jwt=j;}
 public Map<String,Object> register(Map<String,String> in){
   String name=in.getOrDefault("name","").trim(), email=in.getOrDefault("email","").trim().toLowerCase(), password=in.getOrDefault("password","");
   String role=in.getOrDefault("role","BUYER").toUpperCase();
   if(name.isBlank()||!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")||password.length()<8) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Enter a name, valid email and password of at least 8 characters");
   if(!role.equals("BUYER")&&!role.equals("SELLER")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Role must be BUYER or SELLER");
   if(users.findByEmailIgnoreCase(email).isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT,"Email is already registered");
   User u=new User();u.name=name;u.email=email;u.passwordHash=encoder.encode(password);u.role=role;u=users.save(u);
   return Map.of("token",jwt.create(u.email,u.role),"user",safe(u));
 }
 public Map<String,Object> login(Map<String,String> in){
   User u=users.findByEmailIgnoreCase(in.getOrDefault("email","")).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid email or password"));
   if(!u.active||!encoder.matches(in.getOrDefault("password",""),u.passwordHash)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid email or password");
   return Map.of("token",jwt.create(u.email,u.role),"user",safe(u));
 }
 public Map<String,Object> safe(User u){return Map.of("id",u.id,"name",u.name,"email",u.email,"role",u.role);}
}
